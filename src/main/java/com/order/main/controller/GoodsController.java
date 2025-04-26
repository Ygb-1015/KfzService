package com.order.main.controller;

import com.order.main.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kfz")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;


    @GetMapping("/synchronizationGoods")
    public Boolean synchronizationGoods(Long shopId){
        return goodsService.synchronizationGoods(shopId);
    }


}
