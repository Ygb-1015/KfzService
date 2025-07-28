package com.order.main.controller;

import cn.hutool.core.util.ObjectUtil;
import com.order.main.dto.R;
import com.order.main.dto.requst.SoldOutRequest;
import com.order.main.dto.requst.UpdateArtNoRequest;
import com.order.main.dto.response.ItemDelistingResponse;
import com.order.main.dto.response.KfzBaseResponse;
import com.order.main.service.GoodsService;
import com.order.main.service.client.PhpClient;
import com.order.main.threads.KongfzTaskRunnable;
import com.order.main.util.*;
import com.pdd.pop.sdk.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/kfz")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private PhpClient phpClient;

    /**
     * 同步拉取孔夫子商品
     *
     * @param shopId
     * @return
     */
    @GetMapping("/synchronizationGoods")
    public Boolean synchronizationGoods(Long shopId) {
        return goodsService.synchronizationGoods(shopId);
    }

    @GetMapping("getShopInfo/{token}")
    public String getShopInfo(@PathVariable("token") String token) {
        return goodsService.getShopInfo(token);
    }

    /**
     * 更新商品货号
     *
     * @param request
     * @return
     */
    @PostMapping("/updateArtNo")
    public Boolean updateArtNo(@RequestBody UpdateArtNoRequest request) {
        return goodsService.updateArtNo(request);
    }

    @GetMapping("/getTemplateSimpleList/{token}")
    public String getTemplateSimpleList(@PathVariable("token") String token) {
        Map map = JsonUtil.transferToObj(goodsService.getTemplateSimpleList(token), Map.class);
        if (map.get("errorResponse") == null) {
            List list = (List) map.get("successResponse");
            return JsonUtil.transferToJson(list);
        } else {
            return "获取模板失败";
        }
    }

    @GetMapping("/getCategory/{token}")
    public String getCategory(@PathVariable("token") String token) {
        Map map = JsonUtil.transferToObj(goodsService.getCategory(token), Map.class);

        return JsonUtil.transferToJson(map);
    }


    /**
     * 发布商品接口
     *
     * @param map
     * @return
     * @throws Exception
     */
    @PostMapping("/goodAddOne")
    public void goodAddOne(@RequestBody Map map) {
        goodsService.goodsAddOne(map);
    }

    /**
     * 修改商品库存
     */
    @PostMapping("/itemNumberUpdate")
    public void itemNumberUpdate(@RequestBody Map map) {
        goodsService.itemNumberUpdate(map.get("token").toString(), map.get("itemId").toString(), map.get("number").toString());
    }

    /**
     * 调用php接口，根据isbn查询孔夫子书籍数据
     */
    @GetMapping("/getBookInfoF/{isbn}")
    public String getBookInfoF(@PathVariable("isbn") String isbn) {
        try {
            System.out.println("调用php接口，根据isbn查询孔夫子书籍数据：" + isbn);
            String mark = goodsService.getBookInfoF(isbn);
            mark = StringUtils.convertUnicodeToChinese(mark);
            return mark;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 列表勾选上传商品
     */
    @PostMapping("/goodAddList")
    public String goodAddList(@RequestBody Map<String, Object> fiterMap) {
        String fileName = fiterMap.get("fileName").toString();
        // 读取数据
        Map map = EasyExcelUtil.readFileContentMap(UrlUtil.getUrl() + fileName + ".txt");
        // 创建并启动线程
        KongfzTaskRunnable taskRunnable = new KongfzTaskRunnable(goodsService, map, fiterMap);
        Thread thread = new Thread(taskRunnable);
        thread.start();
        System.out.println(thread.getId());
        return String.valueOf(thread.getId());
    }

    /**
     * 暂停线程
     *
     * @param threadId
     */
    @GetMapping("/zanTing/{threadId}")
    public void zanTing(@PathVariable String threadId) {
        goodsService.zanTing(threadId);
    }

    /**
     * 唤醒
     *
     * @param threadId
     */
    @GetMapping("/huanXing/{threadId}")
    public void huanXing(@PathVariable String threadId) {
        goodsService.huanXing(threadId);
    }

    /**
     * 下架商品
     *
     * @param request
     * @return
     */
    @PostMapping("/soldOut")
    public R<Boolean> soldOut(@Validated @RequestBody SoldOutRequest request) {
        KfzBaseResponse<ItemDelistingResponse> itemDelistingResponseKfzBaseResponse = phpClient.itemDelisting(ClientConstantUtils.PHP_URL, request.getToken(), request.getItemId());
        if (ObjectUtil.isNotEmpty(itemDelistingResponseKfzBaseResponse.getErrorResponse())) {
            return R.fail(itemDelistingResponseKfzBaseResponse.getErrorResponse().getSubMsg(), false);
        }
        return R.ok(true);
    }

    /**
     * 合并核价Excel文件
     *
     * @param sourceDirectory 要合并的文件目录
     * @param outputPath      最终导出路径
     * @return
     */
    @PostMapping("/verifyPriceExcelMerger")
    public R<Boolean> verifyPriceExcelMerger(@NotNull(message = "要合并的文件目录不能为空") @RequestParam("sourceDirectory") String sourceDirectory, @NotNull(message = "最终导出路径不能为空") @RequestParam("outputPath") String outputPath) {
        try {
            VerifyPriceExcelMerger.mergeExcelFiles(sourceDirectory, outputPath);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("合并Excel文件时出错: " + e.getMessage());
        }
        return R.ok(true);
    }
}
