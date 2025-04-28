package com.order.main.service;

import com.order.main.dto.requst.UpdateArtNoRequest;

public interface GoodsService {

    Boolean synchronizationGoods(Long shopId);

    Boolean updateArtNo(UpdateArtNoRequest request);

}
