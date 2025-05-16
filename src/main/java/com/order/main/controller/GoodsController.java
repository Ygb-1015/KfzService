package com.order.main.controller;

import com.order.main.dto.requst.GoodsItemAddRequest;
import com.order.main.dto.requst.UpdateArtNoRequest;
import com.order.main.service.GoodsService;
import com.order.main.threads.KongfzTaskRunnable;
import com.order.main.util.EasyExcelUtil;
import com.order.main.util.InterfaceUtils;
import com.order.main.util.StringUtils;
import com.order.main.util.UrlUtil;
import com.pdd.pop.sdk.common.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/kfz")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

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
    public String getShopInfo(@PathVariable("token") String token){
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
    public String getTemplateSimpleList(@PathVariable("token") String token){
        Map map = JsonUtil.transferToObj(goodsService.getTemplateSimpleList(token), Map.class);
        if(map.get("errorResponse") == null){
            List list = (List) map.get("successResponse");
            return JsonUtil.transferToJson(list);
        }else{
            return "获取模板失败";
        }
    }

    @GetMapping("/getCategory/{token}")
    public String getCategory(@PathVariable("token") String token){
        Map map = JsonUtil.transferToObj(goodsService.getCategory(token), Map.class);

        return JsonUtil.transferToJson(map);
    }

    /**
     * 发布商品
     */
    /**
     * 发布商品接口
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
    public void itemNumberUpdate(@RequestBody Map map){
        goodsService.itemNumberUpdate(map.get("token").toString(),map.get("itemId").toString(),map.get("number").toString());
    }

    /**
     * 调用php接口，根据isbn查询孔夫子书籍数据
     */
    @GetMapping("/getBookInfoF/{isbn}")
    public String getBookInfoF(@PathVariable("isbn") String isbn){
        try{
            System.out.println("调用php接口，根据isbn查询孔夫子书籍数据："+isbn);
            String mark = goodsService.getBookInfoF(isbn);
            mark = StringUtils.convertUnicodeToChinese(mark);
            return mark;
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 列表勾选上传商品
     */
    @PostMapping("/goodAddList")
    public String goodAddList(@RequestBody Map<String, Object> fiterMap){
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
     * @param threadId
     */
    @GetMapping("/zanTing/{threadId}")
    public void zanTing(@PathVariable String threadId) {
        goodsService.zanTing(threadId);
    }

    /**
     * 唤醒
     * @param threadId
     */
    @GetMapping("/huanXing/{threadId}")
    public void huanXing(@PathVariable String threadId) {
        goodsService.huanXing(threadId);
    }
}
