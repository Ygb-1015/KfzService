package com.order.main.service;

import com.order.main.dto.R;
import com.order.main.dto.response.LogisticsMethodResponse;

import java.util.List;

public interface OrderService {

    void fullSynchronizationOrder(Integer days, String type,List<Long> shopIdList);

    List<LogisticsMethodResponse> deliveryMethodList(Long shopId);

    R<Boolean> orderDelivery(Long shopId, Long orderId, String shippingId, String shippingCom, String shipmentNum, String userDefined, String moreShipmentNum);
}
