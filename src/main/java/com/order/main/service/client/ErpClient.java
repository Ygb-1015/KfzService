package com.order.main.service.client;

import com.dtflys.forest.annotation.*;
import com.order.main.dto.R;
import com.order.main.dto.bo.*;
import com.order.main.dto.requst.GoodsComparisonRequest;
import com.order.main.dto.requst.OperatingSoldOutRequest;
import com.order.main.dto.requst.OrderListByShopIdRequest;
import com.order.main.dto.requst.UpdateTokenRequest;
import com.order.main.dto.response.ShopVo;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    /**
     * 修改店铺订单更新时间
     *
     * @param myURL
     * @param shopId
     * @param startUpdatedAt
     * @return
     */
    @Put(value = "{myURL}/zhishu/shop/updateTime", dataType = "json")
    Boolean updateTime(@Var("myURL") String myURL, @Query("shopId") Long shopId, @Query("startUpdatedAt") Long startUpdatedAt);

    /**
     * 根据店铺Id查询商品列表
     */
    @Post(value = "{myURL}/zhishu/shopGoodsPublished/getListByShopId", dataType = "json", headers = {"Content-Type: application/json"})
    List<ShopGoodsPublishedVo> getListByShopId(@Var("myURL") String baseUrl, @Query("shopId") Long shopId, @Body List<String> platformId);

    /**
     * 根据区划名称查询区划列表
     */
    @Post(value = "{myURL}/district/queryListByName", dataType = "json", headers = {"Content-Type: application/json"})
    List<TDistrictVo> queryListByName(@Var("myURL") String baseUrl, @Body List<String> districtNames);

    /**
     * 根据店铺Id查询订单列表
     */
    @Post(value = "{myURL}/zhishu/shopOrder/listByShopId", dataType = "json", headers = {"Content-Type: application/json"})
    List<TShopOrderVo> listByShopId(@Var("myURL") String baseUrl, @Body OrderListByShopIdRequest request);

    /**
     * 批量插入/更新订单
     */
    @Post(value = "{myURL}/zhishu/shopOrder/insertOrUpdateOrderBatch", headers = {"Content-Type: application/json"})
    Boolean insertOrUpdateOrderBatch(@Var("myURL") String baseUrl, @Body List<TShopOrderVo> orderList);

    /**
     * 操作库存
     *
     * @param myURL
     * @param operatingInventoryVo
     * @return
     */
    @Post(value = "{myURL}/zhishu/shopGoods/operatingInventory", dataType = "json", headers = {"Content-Type: application/json"})
    Boolean OperatingInventory(@Var("myURL") String myURL, @Body OperatingInventoryVo operatingInventoryVo);

    /**
     * 通过平台订单Id查询库存操作日志
     */
    @Get(value = "{myURL}/zhishu/shopGoods/queryStockChangeLogByOrderSn", dataType = "json")
    R<StockChangeLog> queryStockChangeLogByOrderSn(@Var("myURL") String myURL, @Query("orderSn") String orderSn, @Query("type") Integer type);

    /**
     * 通过平台订单Id查询库存操作日志
     */
    @Get(value = "{myURL}/zhishu/shopGoodsPublishedLog/queryPublishedLogByOrderSn", dataType = "json")
    R<ShopGoodsPublishedLog> queryPublishedLogByOrderSn(@Var("myURL") String myURL,
                                                        @Query("platformId") String platformId,
                                                        @Query("aboutId") String aboutId,
                                                        @Query("platformType") Integer platformType,
                                                        @Query("logType") Integer logType,
                                                        @Query("operationType") Integer operationType);

    /**
     * 操作库存
     *
     * @param myURL
     * @param request
     * @return
     */
    @Post(value = "{myURL}/zhishu/shopGoods/operatingSoldOut", dataType = "json", headers = {"Content-Type: application/json"})
    Boolean operatingSoldOut(@Var("myURL") String myURL, @Body OperatingSoldOutRequest request);
}
