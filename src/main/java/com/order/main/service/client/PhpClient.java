package com.order.main.service.client;

import com.dtflys.forest.annotation.Body;
import com.dtflys.forest.annotation.Post;
import com.dtflys.forest.annotation.Query;
import com.dtflys.forest.annotation.Var;
import com.order.main.dto.requst.GetShopGoodsListRequest;
import com.order.main.dto.requst.ItemItemsnUpdateRequest;
import com.order.main.dto.response.GetShopGoodsListResponse;
import com.order.main.dto.response.GetShopInfoResponse;
import com.order.main.dto.response.ItemItemsnUpdateResponse;
import com.order.main.dto.response.KfzBaseResponse;
import org.springframework.stereotype.Service;

@Service
public interface PhpClient {

    @Post(value = "{myURL}/shop/simpleGet", dataType = "json")
    KfzBaseResponse<GetShopInfoResponse> getShopInfo(@Var("myURL") String myURL, @Query("token") String token);

    @Post(value = "{myURL}/shop/itemList", dataType = "json")
    KfzBaseResponse<GetShopGoodsListResponse> getShopGoodsList(@Var("myURL") String myURL, @Body GetShopGoodsListRequest request);

    @Post(value = "{myURL}/shop/itemItemsnUpdate", dataType = "json",contentType = "application/json")
    KfzBaseResponse<ItemItemsnUpdateResponse> itemItemsnUpdate(@Var("myURL") String myURL, @Body ItemItemsnUpdateRequest request);

}
