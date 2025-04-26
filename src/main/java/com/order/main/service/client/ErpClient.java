package com.order.main.service.client;

import com.dtflys.forest.annotation.*;
import com.order.main.dto.requst.GoodsComparisonRequest;
import com.order.main.dto.requst.UpdateTokenRequest;
import com.order.main.dto.response.ShopVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ErpClient {

    /**
     * 根据店铺Id获取店铺信息
     *
     * @param myURL
     * @param id
     * @return
     */
    @Get(value = "{myURL}/zhishu/shop/getShopInfo", dataType = "json")
    ShopVo getShopInfo(@Var("myURL") String myURL, @Query("id") Long id);

    /**
     * 更新token
     *
     * @param myURL
     * @param request
     * @return
     */
    @Put(value = "{myURL}/zhishu/shop/updateToken", dataType = "json", headers = {"Content-Type: application/json"})
    Boolean updateToken(@Var("myURL") String myURL, @Body UpdateTokenRequest request);

    /**
     * 商品比对
     *
     * @param request
     */
    @Post(value = "{myURL}/zhishu/shopGoods/goodsComparison", dataType = "json", headers = {"Content-Type: application/json"})
    Boolean goodsComparison(@Var("myURL") String myURL, @Body GoodsComparisonRequest request);
}
