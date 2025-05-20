package com.order.main.controller;

import cn.hutool.core.util.ObjectUtil;
import com.order.main.dto.response.LogisticsMethodResponse;
import com.order.main.exception.ServiceException;
import com.order.main.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/kfz/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 同步近30天历史存量订单
     */
    @PostMapping("/fullSynchronizationOrder")
    public String fullSynchronizationOrder(@RequestBody List<Long> shopIdList) {
        orderService.fullSynchronizationOrder(shopIdList);
        return "susses";
    }

    /**
     * 获取配送方式列表
     */
    @GetMapping("/delivery/methodList")
    public List<LogisticsMethodResponse> deliveryMethodList(Long shopId) {
        return orderService.deliveryMethodList(shopId);
    }

    /**
     * 订单发货
     */
    @GetMapping("/deliver")
    public Boolean orderDelivery(
            @NotNull(message = "shopId不能为空") Long shopId, @NotNull(message = "orderId不能为空") Long orderId,
            @NotNull(message = "shippingId不能为空") String shippingId, String shippingCom, String shipmentNum,
            String userDefined, String moreShipmentNum
    ) {
        if (!shippingId.equals("noLogistics")){
            if (ObjectUtil.isEmpty(shippingCom)) throw new ServiceException("快递公司(shippingCom)不能为空");
            if (ObjectUtil.isEmpty(shipmentNum)) throw new ServiceException("快递单号(shipmentNum)不能为空");
        }
        if ("other".equals(shippingCom)){
            if (ObjectUtil.isEmpty(userDefined)) throw new ServiceException("用户自定义物流公司(userDefined)不能为空");
        }
        return orderService.orderDelivery(shopId, orderId, shippingId, shippingCom, shipmentNum, userDefined, moreShipmentNum);
    }

}
