package com.order.main.service;

import com.order.main.dto.requst.GoodsItemAddRequest;
import com.order.main.dto.requst.UpdateArtNoRequest;
import com.order.main.dto.response.GetShopInfoResponse;

import java.util.Map;

public interface GoodsService {

    Boolean synchronizationGoods(Long shopId);

    Boolean updateArtNo(UpdateArtNoRequest request);

    /**
     * 获取店铺详情
     */
    public String getShopInfo(String token);

    /**
     * 获取运费模板
     */
    String getTemplateSimpleList(String token);

    /**
     * 获取分类
     * @param token
     * @return
     */
    String getCategory(String token);

    /**
     * 单个商品发布接口
     * @param request
     * @return
     */
    String itemAdd(GoodsItemAddRequest request);

    /**
     * 商品发布接口
     * @param map
     */
    void goodsAddOne(Map map);

    /**
     * 发布商品接口
     * @param map
     */
    void goodsAddMain(Map map, Map dataMap);

    /**
     * 图片上传
     * @param file
     * @return
     */
    String upload(String file,String token);

    /**
     * 修改商品库存
     * @param token
     * @param itemId    孔夫子平台的商品id
     * @param number    库存数量
     * @return
     */
    String itemNumberUpdate(String token,String itemId,String number);

    /**
     * 调用php接口，根据isbn查询孔夫子书籍数据
     */
    String getBookInfoF(String isbn);

    void zanTing(String threadId);

    void huanXing(String threadId);

}
