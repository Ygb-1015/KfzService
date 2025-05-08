package com.order.main.service;

import com.dtflys.forest.annotation.Query;
import com.dtflys.forest.annotation.Var;
import com.order.main.dto.requst.GoodsItemAddRequest;
import com.order.main.dto.requst.UpdateArtNoRequest;
import com.order.main.dto.response.ItemItemAddResponse;
import com.order.main.dto.response.KfzBaseResponse;

import java.util.Map;

public interface GoodsService {

    Boolean synchronizationGoods(Long shopId);

    Boolean updateArtNo(UpdateArtNoRequest request);

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

    public void zanTing(String threadId);

    public void huanXing(String threadId);

}
