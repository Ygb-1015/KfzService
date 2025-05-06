package com.order.main.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.exceptions.ForestNetworkException;
import com.order.main.dto.requst.*;
import com.order.main.dto.response.GetShopGoodsListResponse;
import com.order.main.dto.response.ItemItemsnUpdateResponse;
import com.order.main.dto.response.KfzBaseResponse;
import com.order.main.dto.response.ShopVo;
import com.order.main.service.GoodsService;
import com.order.main.service.client.ErpClient;
import com.order.main.service.client.PhpClient;
import com.order.main.util.ClientConstantUtils;
import com.order.main.util.ThreadPoolUtils;
import com.order.main.util.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GoodsServiceImpl implements GoodsService {

    // 日志记录器
    private static final Logger log = LoggerFactory.getLogger(GoodsServiceImpl.class);

    @Autowired
    private ErpClient erpClient;

    @Autowired
    private PhpClient phpClient;

    @Autowired
    private TokenUtils tokenUtils;

    @Override
    public Boolean synchronizationGoods(Long shopId) {
        // 根据店铺Id查询店铺信息
        ShopVo shopInfo = erpClient.getShopInfo(ClientConstantUtils.ERP_URL, shopId);
        Assert.isTrue(ObjectUtil.isNotEmpty(shopInfo), "查询不到店铺信息");
        ThreadPoolUtils.execute(() -> {
            List<GetShopGoodsListResponse.ShopGoods> shopGoodsList = queryShopGoods(shopInfo.getToken(), shopInfo.getRefreshToken(), shopId);
            List<ZhishuShopGoodsRequest> zhishuShopGoodsRequestList = shopGoodsList.stream().map(shopGoods -> {
                ZhishuShopGoodsRequest zhishuShopGoodsRequest = new ZhishuShopGoodsRequest();
                zhishuShopGoodsRequest.setUserId(shopInfo.getCreateBy().toString());
                zhishuShopGoodsRequest.setProductId(shopGoods.getItemId().toString());
                zhishuShopGoodsRequest.setGoodsName(shopGoods.getItemName());
                zhishuShopGoodsRequest.setIsbn(shopGoods.getIsbn());
                zhishuShopGoodsRequest.setArtNo(shopGoods.getItemSn());
                zhishuShopGoodsRequest.setStock(0L);
                zhishuShopGoodsRequest.setPrice(shopGoods.getPrice());
                zhishuShopGoodsRequest.setConditionCode(shopGoods.getQuality());
                zhishuShopGoodsRequest.setCreateBy(shopInfo.getCreateBy());
                zhishuShopGoodsRequest.setItemNumber(shopGoods.getItemId().toString());
                zhishuShopGoodsRequest.setItemNumber(shopGoods.getItemId().toString());
                zhishuShopGoodsRequest.setFixPrice(shopGoods.getOriPrice());
                zhishuShopGoodsRequest.setInventory(shopGoods.getNumber());
                zhishuShopGoodsRequest.setBookPic("https://www0.kfzimg.com/" + shopGoods.getImgUrl());
                // zhishuShopGoodsBo.setIsArtNoConversion(1);
                return zhishuShopGoodsRequest;
            }).collect(Collectors.toList());
            GoodsComparisonRequest request = new GoodsComparisonRequest();
            request.setShopId(shopId);
            request.setUserId(shopInfo.getCreateBy());
            request.setZhishuShopGoodsRequestList(zhishuShopGoodsRequestList);
            erpClient.goodsComparison(ClientConstantUtils.ERP_URL, request);
        });

        return true;
    }

    public List<GetShopGoodsListResponse.ShopGoods> queryShopGoods(String token, String refreshToken, Long shopId) {
        Boolean isHaveNext = true;
        Integer pageNum = 1;
        List<GetShopGoodsListResponse.ShopGoods> shopGoodsList = new ArrayList<>();

        Boolean isRefreshToken = false;

        while (isHaveNext) {
            // 构建查询店铺商品请求参数
            GetShopGoodsListRequest getShopGoodsListRequest = new GetShopGoodsListRequest();
            getShopGoodsListRequest.setToken(token);
            getShopGoodsListRequest.setPageNum(pageNum);
            getShopGoodsListRequest.setPageSize(100);

            KfzBaseResponse<GetShopGoodsListResponse> shopGoodsResponse = phpClient.getShopGoodsList(ClientConstantUtils.PHP_URL, getShopGoodsListRequest);
            log.info("查询孔夫子店铺商品响应-{}", JSONObject.toJSONString(shopGoodsResponse));
            if (!isRefreshToken && ObjectUtil.isNotEmpty(shopGoodsResponse.getErrorResponse())) {
                List<Long> tokenErrorCode = new ArrayList<>();
                tokenErrorCode.add(1000L);
                tokenErrorCode.add(1001L);
                tokenErrorCode.add(2000L);
                tokenErrorCode.add(2001L);
                if (tokenErrorCode.contains(shopGoodsResponse.getErrorResponse().getCode())) {
                    token = tokenUtils.refreshToken(refreshToken, shopId);
                    isRefreshToken = true;
                } else {
                    throw new RuntimeException("查询孔夫子店铺商品异常-" + JSONObject.toJSONString(shopGoodsResponse));
                }
            } else {
                if (ObjectUtil.isNotEmpty(shopGoodsResponse.getSuccessResponse().getList())) {
                    shopGoodsList.addAll(shopGoodsResponse.getSuccessResponse().getList());
                    if (shopGoodsResponse.getSuccessResponse().getPages() <= shopGoodsResponse.getSuccessResponse().getPageNum())
                        isHaveNext = false;
                    pageNum++;
                } else {
                    isHaveNext = false;
                }
            }
        }
        return shopGoodsList;
    }

    @Override
    public Boolean updateArtNo(UpdateArtNoRequest request) {
        // 根据店铺Id查询店铺信息
        ShopVo shopInfo = erpClient.getShopInfo(ClientConstantUtils.ERP_URL, request.getShopId());
        Assert.isTrue(ObjectUtil.isNotEmpty(shopInfo), "查询不到店铺信息");
        String token = shopInfo.getToken();
        String refreshToken = shopInfo.getRefreshToken();
        for (int i = 0; i < 2; i++) {
            ItemItemsnUpdateRequest updateRequest = new ItemItemsnUpdateRequest();
            updateRequest.setToken(token);
            updateRequest.setItemId(request.getProductId());
            updateRequest.setItemSn(request.getArtNo());
            KfzBaseResponse<ItemItemsnUpdateResponse> response = new KfzBaseResponse<>();
            try {
                response = phpClient.itemItemsnUpdate(ClientConstantUtils.PHP_URL, updateRequest);
                if (ObjectUtil.isNotEmpty(response.getErrorResponse())) {
                    List<Long> tokenErrorCode = new ArrayList<>();
                    tokenErrorCode.add(1000L);
                    tokenErrorCode.add(1001L);
                    tokenErrorCode.add(2000L);
                    tokenErrorCode.add(2001L);
                    if (tokenErrorCode.contains(response.getErrorResponse().getCode())) {
                        token = tokenUtils.refreshToken(refreshToken, request.getShopId());
                    } else {
                        throw new RuntimeException("更新孔夫子商品货号异常-" + JSONObject.toJSONString(response));
                    }
                } else {
                    Assert.isTrue(ObjectUtil.isNotEmpty(response.getSuccessResponse()), "更新孔夫子商品货号异常-" + JSONObject.toJSONString(response));
                    i++;
                }
            } catch (ForestNetworkException e) {
                throw new ForestNetworkException(e.getMessage(), e.getStatusCode(), e.getResponse());
            }
        }
        return true;
    }
}
