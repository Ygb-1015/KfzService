package com.order.main.controller;

import com.order.main.dto.requst.UpdateArtNoRequest;
import com.order.main.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kfz")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 同步拉取孔夫子商品
     * @param shopId
     * @return
     */
    @GetMapping("/synchronizationGoods")
    public Boolean synchronizationGoods(Long shopId){
        return goodsService.synchronizationGoods(shopId);
    }

    /**
     * 更新商品货号
     * @param request
     * @return
     */
    @PostMapping("/updateArtNo")
    public Boolean updateArtNo(@RequestBody UpdateArtNoRequest request){
        return goodsService.updateArtNo(request);
    }


}
