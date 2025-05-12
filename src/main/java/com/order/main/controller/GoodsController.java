package com.order.main.controller;

import com.order.main.dto.requst.GoodsItemAddRequest;
import com.order.main.dto.requst.UpdateArtNoRequest;
import com.order.main.service.GoodsService;
import com.order.main.util.InterfaceUtils;
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

        GoodsItemAddRequest request = new GoodsItemAddRequest();

        request.setToken(map.get("token").toString());
        request.setTpl(map.get("tpl").toString());
        request.setCatId("43000000000000000");
        request.setMyCatId(map.get("myCatId") == null ? "" : map.get("myCatId").toString());
        request.setItemName(map.get("itemName") == null ? "" : map.get("itemName").toString());
        request.setImportantDesc(map.get("importantDesc") == null ? "" : map.get("importantDesc").toString());
        request.setPrice(new BigDecimal(map.get("price").toString()).divide(new BigDecimal(100)).toString());
        request.setNumber(map.get("number") == null ? "" : map.get("number").toString());
        request.setQuality(map.get("quality") == null ? "" : map.get("quality").toString());
        request.setQualityDesc(map.get("qualityDesc") == null ? "" : map.get("qualityDesc").toString());
        request.setItemSn(map.get("itemSn") == null ? "" : map.get("itemSn").toString());
        request.setImgUrl(map.get("imgUrl") == null ? "" : map.get("imgUrl").toString());

        //获取实拍图网路路径
        String[] imagesArr = map.get("images") == null ? new String[0] : map.get("images").toString().split(";");

        String images = "";
        for(int i=0;i<imagesArr.length;i++){

            String iamge = imagesArr[i];

            Map dataMap;
            try{
                dataMap = JsonUtil.transferToObj(goodsService.upload(iamge,request.getToken()), Map.class);
            }catch (Exception e){
                System.out.println("上传图片异常---------------");
                e.printStackTrace();
                continue;
            }

            Map errorResponse = (Map) dataMap.get("errorResponse");
            if(errorResponse != null){
                System.out.println("上传图片报错---------errorResponse------："+errorResponse);
                continue;
            }

            Map successResponse = (Map) dataMap.get("successResponse");
            Map kongkzImage = (Map) successResponse.get("image");

            if(i==0){
                images = kongkzImage.get("url").toString();
            }else{
                images = images + ";" + kongkzImage.get("url").toString();
            }
            //当图片数量不足八张时
            if(i == imagesArr.length-1 && i < 8){
                for(int j=i;j<7;j++){
                    images = images + ";" + kongkzImage.get("url").toString();
                }
            }
        }

        request.setImages(images);

        request.setItemDesc(map.get("itemDesc") == null ? "" : map.get("itemDesc").toString());
        request.setBearShipping(map.get("bearShipping").toString());
        request.setMouldId(Long.parseLong(map.get("mouldld").toString()));
        request.setWeight(map.get("weight") == null ? BigDecimal.ZERO : new BigDecimal(map.get("weight").toString()));
        request.setWeightPiece(map.get("weightPiece") == null ? BigDecimal.ZERO : new BigDecimal(map.get("weightPiece").toString()));

        request.setIsbn(map.get("isbn") == null ? "" : map.get("isbn").toString());
        request.setAuthor(map.get("author") == null ? "" : map.get("author").toString());
        request.setPress(map.get("press") == null ? "" : map.get("press").toString());
        request.setPubDate(map.get("pubDate") == null ? "" : map.get("pubDate").toString());
        request.setBinding(map.get("binding") == null ? "" : map.get("binding").toString());



        Map dataMap = JsonUtil.transferToObj(goodsService.itemAdd(request), Map.class);

        Map errorResponse  = (Map) dataMap.get("errorResponse");
        if(errorResponse != null){
            System.out.println("---------------------上传报错");
            System.out.println(JsonUtil.transferToJson(errorResponse)+"------------");
        }else{
            System.out.println("---------------------上传成功");
            Map successResponse = (Map) dataMap.get("successResponse");
            Map item = (Map) successResponse.get("item");

            Map callBackMap = new HashMap();
            callBackMap.put("shopId", map.get("shopId").toString());
            callBackMap.put("goodId", map.get("goodId").toString());
            callBackMap.put("itemId", item.get("itemId"));
            callBackMap.put("userId", map.get("userId").toString());
            //调用接口
            InterfaceUtils.getInterfacePost("/api/kongfz/goodAddCallBack", callBackMap);
        }
    }

    /**
     * 修改商品库存
     */
    @PostMapping("/itemNumberUpdate")
    public void itemNumberUpdate(@RequestBody Map map){
        goodsService.itemNumberUpdate(map.get("token").toString(),map.get("itemId").toString(),map.get("number").toString());
    }
}
