package com.order.main.service;

import com.order.main.dto.response.DeliveryMethodResponse;
import com.order.main.dto.response.LogisticsMethodResponse;

import java.util.List;

public interface OrderService {

    void fullSynchronizationOrder(List<Long> shopIdList);

    List<LogisticsMethodResponse> deliveryMethodList(Long shopId);
}
