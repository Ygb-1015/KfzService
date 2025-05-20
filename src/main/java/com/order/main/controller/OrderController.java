package com.order.main.controller;

import com.order.main.dto.response.LogisticsMethodResponse;
import com.order.main.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

}
