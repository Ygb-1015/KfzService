package com.order.main.service.client;

import com.dtflys.forest.annotation.*;
import com.order.main.dto.requst.GetShopGoodsListRequest;
import com.order.main.dto.requst.GoodsItemAddRequest;
import com.order.main.dto.requst.ItemItemsnUpdateRequest;
import com.order.main.dto.response.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PhpClient {

    /**
     * 获取店铺信息
     *
     * @param myURL
     * @param token
     * @return
     */
    @Post(value = "{myURL}/shop/simpleGet", dataType = "json")
    KfzBaseResponse<GetShopInfoResponse> getShopInfo(@Var("myURL") String myURL, @Query("token") String token);

    /**
     * 获取商品列表
     *
     * @param myURL
     * @param request
     * @return
     */
    @Post(value = "{myURL}/shop/itemList", dataType = "json")
    KfzBaseResponse<GetShopGoodsListResponse> getShopGoodsList(@Var("myURL") String myURL, @Body GetShopGoodsListRequest request);

    /**
     * 修改商品库存
     *
     * @param myURL
     * @param request
     * @return
     */
    @Post(value = "{myURL}/shop/itemItemsnUpdate", dataType = "json", contentType = "application/json")
    KfzBaseResponse<ItemItemsnUpdateResponse> itemItemsnUpdate(@Var("myURL") String myURL, @Body ItemItemsnUpdateRequest request);

    /**
     * 订单列表
     *
     * @param myURL
     * @param token
     * @param userType
     * @param pageNum
     * @param pageSize
     * @param startUpdateTime
     * @return
     */
    @Get(value = "{myURL}/order/getList", dataType = "json")
    KfzBaseResponse<PageQueryOrdersResponse> pageQueryOrders(@Var("myURL") String myURL, @Query("token") String token,
                                                             @Query("userType") String userType, @Query("pageNum") Integer pageNum,
                                                             @Query("pageSize") Integer pageSize, @Query("startUpdateTime") String startUpdateTime);

    /**
     * 商品新增
     *
     * @param myURL
     * @param request
     * @return
     */
    @Post(value = "{myURL}/shop/itemAdd", dataType = "json", contentType = "application/json")
    String itemAdd(@Var("myURL") String myURL, @Body GoodsItemAddRequest request);

    /**
     * 运费模板
     */
    @Get(value = "{myURL}/delivery/templateSimpleList", dataType = "json")
    String getTemplateSimpleList(@Var("myURL") String myURL, @Query("token") String token);

    /**
     * 公共分类数据
     */
    @Get(value = "{myURL}/common/category", dataType = "json")
    String getCategory(@Var("myURL") String myURL, @Query("token") String token);

    /**
     * 图片上传
     */
    @Post(value = "{myURL}/image/upload", dataType = "json")
    String upload(@Var("myURL") String myURL, @Query("file") String file, @Query("token") String token);

    /**
     * * 修改商品库存
     *
     * @param myURL
     * @param token  token
     * @param itemId 孔夫子平台的商品id
     * @param number 库存数量
     * @return
     */
    @Post(value = "{myURL}/shop/itemNumberUpdate", dataType = "json")
    String itemNumberUpdate(@Var("myURL") String myURL, @Query("token") String token, @Query("itemId") String itemId, @Query("number") String number);

    /**
     * 调用php接口，根据isbn查询孔夫子书籍数据
     */
    @Get(value = "{myURL}/bookBase/getBookInfoF", dataType = "json")
    String getBookInfoF(@Var("myURL") String myURL, @Query("isbn") String isbn);

    /**
     * 获取物流公司列表
     *
     * @param myURL
     * @param token
     * @return
     */
    @Get(value = "{myURL}/delivery/methodList", dataType = "json")
    KfzBaseResponse<List<DeliveryMethodResponse>> deliveryMethodList(@Var("myURL") String myURL, @Query("token") String token);


    @Post(value = "{myURL}/order/deliver", dataType = "json")
    KfzBaseResponse<OrderDeliveryResponse> orderDelivery(@Var("myURL") String myURL, @Query("token") String token,
                                                         @Query("orderId") Long orderId, @Query("shippingId") String shippingId, @Query("shippingCom") String shippingCom,
                                                         @Query("shipmentNum") String shipmentNum, @Query("userDefined") String userDefined, @Query("moreShipmentNum") String moreShipmentNum);


}
