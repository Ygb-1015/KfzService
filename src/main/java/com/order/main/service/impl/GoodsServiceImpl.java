package com.order.main.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.exceptions.ForestNetworkException;
import com.order.main.dto.requst.*;
import com.order.main.dto.response.*;
import com.order.main.service.GoodsService;
import com.order.main.service.client.ErpClient;
import com.order.main.service.client.PhpClient;
import com.order.main.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GoodsServiceImpl implements GoodsService {

    // 日志记录器
    private static final Logger log = LoggerFactory.getLogger(GoodsServiceImpl.class);

    /**
     * 锁对象
     */
    private final Map<String, Object> lockMap = new HashMap<>();
    private final Map<String, Boolean> runMap = new HashMap<>();

    @Autowired
    private ErpClient erpClient;

    @Autowired
    private PhpClient phpClient;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private RedisUtils redisUtils;
    ;

    @Override
    public Boolean synchronizationGoods(Long shopId) {
        // 根据店铺Id查询店铺信息
        ShopVo shopInfo = erpClient.getShopInfo(ClientConstantUtils.ERP_URL, shopId);
        Assert.isTrue(ObjectUtil.isNotEmpty(shopInfo), "查询不到店铺信息");
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 定义格式：YYYY-MM-DD HH:mm:ss
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 格式化当前时间
        String currentDateTime = now.format(formatter);
        // 从Redis中获取上次同步时间
        String lastGoodsSynTime = (String) redisUtils.getCacheObject("lastGoodsSynTime_" + shopInfo.getId());
        ThreadPoolUtils.execute(() -> {
            List<GetShopGoodsListResponse.ShopGoods> shopGoodsList = queryShopGoods(shopInfo.getToken(), shopInfo.getRefreshToken(), shopId, lastGoodsSynTime);
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
            redisUtils.setCacheObject("lastGoodsSynTime_" + shopInfo.getId(), currentDateTime);
        });

        return true;
    }

    public List<GetShopGoodsListResponse.ShopGoods> queryShopGoods(String token, String refreshToken, Long shopId, String lastGoodsSynTime) {
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
            // 判断是不是第一次，不是拼接上次同步时间作为筛选条件
            if (ObjectUtil.isNotNull(lastGoodsSynTime)) {
                getShopGoodsListRequest.setAddTimeBegin(lastGoodsSynTime);
            }

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

    @Override
    public String getTemplateSimpleList(String token) {
        return phpClient.getTemplateSimpleList(ClientConstantUtils.PHP_URL, token);
    }

    @Override
    public String getCategory(String token) {
        return phpClient.getCategory(ClientConstantUtils.PHP_URL, token);
    }

    @Override
    public String itemAdd(GoodsItemAddRequest request){
        return phpClient.itemAdd(ClientConstantUtils.PHP_URL, request);
    }

    @Override
    public String upload(String file,String token){
        return phpClient.upload(ClientConstantUtils.PHP_URL, file,token);
    }

    @Override
    public void goodsAddMain(Map map, Map dataMap) {

        // 页面填写数据Map
        Map htmlData = (Map) map.get("htmlData");
        // 任务信息对象
        Map taskBo = (Map) map.get("taskBo");
        // 店铺数据List
        List<Map> shopVoMapList = (List<Map>) map.get("shopVoList");
        // 数据过滤Map
        Map filterMap = (Map) dataMap.get("filterMap");

        // 上架状态
        String listStatus = htmlData.get("listStatus").toString();
        // 图书类目
        String bookCategory = htmlData.get("bookCategory").toString();
        //自选类目
        String bookCategoryAppoint = htmlData.get("bookCategoryAppoint") == null ? "" : htmlData.get("bookCategoryAppoint").toString();

        // 图书数据List
        List<Map> bookBaseInfoVoList = (List<Map>) map.get("bookBaseInfoVoList");
        // 线程锁对象初始化
        lockMap.put(String.valueOf(Thread.currentThread().getId()), new Object());
        runMap.put(String.valueOf(Thread.currentThread().getId()), false);

        // 创建表头
        Map<String, String> headMap = new HashMap<>();
        headMap.put("bookNum", "书号");
        headMap.put("price", "价格");
        headMap.put("stock", "库存");
        headMap.put("logMsg", "日志");
        // 创建执行日志
        EasyExcelUtil.writeJsonToFile(taskBo.get("id") + "-msg.txt", "开始执行文件" + System.lineSeparator() +
                "开始进行数据加载" + System.lineSeparator() +
                "数据加载完毕" + System.lineSeparator() +
                "正在执行" + System.lineSeparator() +
                "已执行数据：0条" + System.lineSeparator() +
                "待执行数据：" + bookBaseInfoVoList.size() + "条");
        // 获取系统设置白名单/黑名单
        String systemWhiteStr = filterMap.get("System-whiteStr") != null ? filterMap.get("System-whiteStr").toString() : "";
        String systemBlackStr = filterMap.get("System-blackStr") != null ? filterMap.get("System-blackStr").toString() : "";
        for (Map shopVoMap : shopVoMapList) {
            if(!shopVoMap.get("shopType").equals("1")){
                continue;
            }
            // 创建文件生成路径
            String filePath = UrlUtil.getUrl() + taskBo.get("id") + shopVoMap.get("id") + ".xlsx";
            // 生成excel文件
            EasyExcelUtil.writeExcel(filePath, new ArrayList<>(), null);
            // 获取用户自定义白名单/黑名单
            String whiteStr = filterMap.get(shopVoMap.get("id") + "-whiteStr") != null ? filterMap.get(shopVoMap.get("id") + "-whiteStr").toString() : "";
            String blackStr = filterMap.get(shopVoMap.get("id") + "-blackStr") != null ? filterMap.get(shopVoMap.get("id") + "-blackStr").toString() : "";

            //增加数据
//            goodsAdd(shopVoMap,bookBaseInfoVoList,listStatus,bookCategoryAppoint,filePath,String.valueOf(taskBo.get("id")),map,systemWhiteStr,systemBlackStr,whiteStr,blackStr,bookCategory);
        }

        // 写入文件
        EasyExcelUtil.writeJsonToFile(taskBo.get("id") + "-msg.txt", "开始执行文件" + System.lineSeparator() +
                "开始进行数据加载" + System.lineSeparator() +
                "数据加载完毕" + System.lineSeparator() +
                "正在执行" + System.lineSeparator() +
                "已执行数据：" + bookBaseInfoVoList.size() + "条" + System.lineSeparator() +
                "待执行数据：0 条 " + System.lineSeparator() +
                "文件执行完成");
        // 调用完成任务接口
        InterfaceUtils.getInterface("/zhishu/task/finishTash/" + taskBo.get("id"));
    }


    public void taskWait(String threadId){
        if(runMap.get(threadId)){
            try {
                synchronized (lockMap.get(threadId)){
                    lockMap.get(threadId).wait();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Override
    public void zanTing(String threadId){
        runMap.put(threadId,true);
    }

    @Override
    public void huanXing(String threadId){
        synchronized (lockMap.get(threadId)){
            runMap.put(threadId,false);
            lockMap.get(threadId).notify();
        }
    }

}
