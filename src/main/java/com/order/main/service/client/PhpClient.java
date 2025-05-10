package com.order.main.service.client;

import com.dtflys.forest.annotation.*;
import com.order.main.dto.requst.GetShopGoodsListRequest;
import com.order.main.dto.requst.ItemItemsnUpdateRequest;
import com.order.main.dto.requst.PageQueryOrdersRequest;
import com.order.main.dto.response.*;
import org.springframework.stereotype.Service;

@Service
public interface PhpClient {

    @Post(value = "{myURL}/shop/simpleGet", dataType = "json")
    KfzBaseResponse<GetShopInfoResponse> getShopInfo(@Var("myURL") String myURL, @Query("token") String token);

    @Post(value = "{myURL}/shop/itemList", dataType = "json")
    KfzBaseResponse<GetShopGoodsListResponse> getShopGoodsList(@Var("myURL") String myURL, @Body GetShopGoodsListRequest request);

    @Post(value = "{myURL}/shop/itemItemsnUpdate", dataType = "json",contentType = "application/json")
    KfzBaseResponse<ItemItemsnUpdateResponse> itemItemsnUpdate(@Var("myURL") String myURL, @Body ItemItemsnUpdateRequest request);

    @Get(value = "{myURL}/order/getList", dataType = "json")
    KfzBaseResponse<PageQueryOrdersResponse> pageQueryOrders(@Var("myURL") String myURL,
                                                             @Query("token") String token,
                                                             @Query("userType") String userType,
                                                             @Query("pageNum") Integer pageNum,
                                                             @Query("pageSize") Integer pageSize,
                                                             @Query("startUpdateTime") String startUpdateTime);

}
