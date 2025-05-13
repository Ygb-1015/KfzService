package com.order.main.threads;

import com.order.main.service.GoodsService;

import java.util.Map;

public class KongfzTaskRunnable implements Runnable {

    private final GoodsService goodsService;


    private final Map map;
    private final Map dataMap;


    public KongfzTaskRunnable(GoodsService goodsService, Map map, Map dataMap) {
        this.goodsService = goodsService;
        this.map = map;
        this.dataMap = dataMap;
    }
    @Override
    public void run() {
        // 执行任务
        goodsService.goodsAddMain(map,dataMap);
    }
}


