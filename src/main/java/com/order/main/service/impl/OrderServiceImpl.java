package com.order.main.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.order.main.dto.R;
import com.order.main.dto.bo.*;
import com.order.main.dto.requst.OrderListByShopIdRequest;
import com.order.main.dto.response.*;
import com.order.main.enums.*;
import com.order.main.exception.ServiceException;
import com.order.main.service.OrderService;
import com.order.main.service.client.ErpClient;
import com.order.main.service.client.PhpClient;
import com.order.main.util.ClientConstantUtils;
import com.order.main.util.RedisUtils;
import com.order.main.util.ThreadPoolUtils;
import com.order.main.util.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    // 日志记录器
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private ErpClient erpClient;

    @Autowired
    private PhpClient phpClient;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private RedisUtils redisUtils;

    // 可以自定义一个常量作为锁的前缀
    private static final String LOCK_KEY_PREFIX = "lock:fullSynchronizationOrder:";

    // 设置锁的过期时间（单位：秒），防止死锁
    private static final int LOCK_EXPIRE_SECONDS = 120;

    // 每隔多少秒续期一次 Redis 锁（比如 30 秒）
    private static final int INITIAL_DELAY_AND_PERIOD = 30;

    @Override
    public void fullSynchronizationOrder(List<Long> shopIdList) {
        if (ObjectUtil.isEmpty(shopIdList)) {
            System.err.println("店铺ID为空");
            return;
        }
        for (Long shopId : shopIdList) {
            ThreadPoolUtils.execute(() -> {

                // 加上redis锁防止重复调用
                String lockKey = LOCK_KEY_PREFIX + shopId;
                String lockValue = "locked";
                // 尝试获取锁
                String syncOrderLock = (String) redisUtils.getCacheObject(lockKey);
                if (ObjectUtil.isNotNull(syncOrderLock)) return;
                // 若没有锁，存入新的锁，并设置过期时间
                redisUtils.setCacheObject(lockKey, lockValue, LOCK_EXPIRE_SECONDS, TimeUnit.SECONDS);
                ScheduledExecutorService scheduler = null;
                ScheduledFuture<?> future = null;
                try {
                    scheduler = Executors.newScheduledThreadPool(1);
                    // 设置一个定时任务来不断延长锁的过期时间
                    Runnable renewLockTask = () -> {
                        redisUtils.setCacheObject(lockKey, lockValue, LOCK_EXPIRE_SECONDS, TimeUnit.SECONDS);
                        System.out.println("Redis锁过期时间已更新");
                    };
                    // 启动定时任务
                    future = scheduler.scheduleAtFixedRate(renewLockTask, 0, INITIAL_DELAY_AND_PERIOD, TimeUnit.SECONDS);

                    System.out.println("线程ID-" + Thread.currentThread().getId() + ":同步店铺订单-shopId");
                    // 根据shopId获取店铺信息
                    ShopVo shop = erpClient.getShopInfo(ClientConstantUtils.ERP_URL, shopId);

                    // 如果店铺不存在，抛出异常
                    if (shop == null) {
                        // 记录错误日志
                        log.error("店铺不存在，shopId: {}", shopId);
                        // 抛出异常
                        throw new ServiceException("店铺不存在");
                    }
                    try {
                        // 调用孔夫子增量订单接口
                        synOrder(shop);
                    } catch (Exception e) {
                        // 记录错误日志
                        log.error("同步店铺订单失败，shopId: {}", shopId, e);
                        // 抛出异常
                        throw new ServiceException("同步订单失败: " + e.getMessage());
                    }
                } catch (Exception e) {
                    // 捕获其他未知异常
                    log.error("同步店铺订单失败，shopId: {}", shopId, e);
                } finally {
                    // 取消定时任务
                    if (future != null) {
                        future.cancel(false);
                    }
                    // 关闭线程池
                    if (scheduler != null) {
                        scheduler.shutdownNow();
                    }
                    // 确保释放锁
                    redisUtils.deleteCacheObject(lockKey);
                    System.out.println("已释放redis锁");
                }
            });

        }
    }

    private void synOrder(ShopVo shop) throws Exception {

        // 查询erp店铺全部商品
        List<ShopGoodsPublishedVo> shopGoodsList = erpClient.getListByShopId(ClientConstantUtils.ERP_URL, shop.getId());
        List<String> shopGoodsIdList = shopGoodsList.stream().map(ShopGoodsPublishedVo::getPlatformId).collect(Collectors.toList());

        // 定义一个集合用来存储返回的订单数据
        List<PageQueryOrdersResponse.Order> allOrderList = new ArrayList<>();
        // 获取当前时间时间戳
        Instant now = Instant.now();
        // 判断是否还有下一页标识符，默认为true
        boolean isHaveNext = true;
        // 是否刷新token标识符，默认为false
        boolean isRefreshToken = false;

        // 分页查询默认从第一页开始查询
        Integer pageNum = 1;
        // 获取店铺孔网token
        String token = shop.getToken();
        // 获取店铺孔网refreshToken
        String refreshToken = shop.getRefreshToken();
        // 获取店铺Id
        Long shopId = shop.getId();
        // 初始化查询时间
        String startUpdateTime;
        // 获取上次更新订单时间戳
        Long startUpdatedAt = shop.getStartUpdatedAt();
        // 判断是不是第一次同步
        if (ObjectUtil.isNotNull(startUpdatedAt)) {
            // 不是拼接上次同步时间作为筛选条件
            // 将毫秒级时间戳转换为Instant对象
            Instant instant = Instant.ofEpochMilli(startUpdatedAt);
            // 将Instant对象转换为LocalDateTime对象，并设置时区为系统默认时区
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            // 定义日期时间格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            // 使用定义的格式化器格式化LocalDateTime对象为字符串
            startUpdateTime = localDateTime.format(formatter);
        } else {
            // 是第一次同步默认查询近一个月的
            // 获取今天0时时间戳
            Instant today = ZonedDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT, ZoneId.systemDefault()).toInstant();
            // 默认从九十天前的数据开始更新
            Instant startInstant = today.minus(Duration.ofDays(30));
            // 将Instant对象转换为LocalDateTime对象，并设置时区为系统默认时区
            LocalDateTime localDateTime = LocalDateTime.ofInstant(startInstant, ZoneId.systemDefault());
            // 定义日期时间格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            // 使用定义的格式化器格式化LocalDateTime对象为字符串
            startUpdateTime = localDateTime.format(formatter);
        }

        while (isHaveNext) {
            KfzBaseResponse<PageQueryOrdersResponse> ordersResponse = phpClient.pageQueryOrders(ClientConstantUtils.PHP_URL, token, UserTypeEnum.SELLER.getCode(), pageNum, 50, startUpdateTime);
            if (!isRefreshToken && ObjectUtil.isNotEmpty(ordersResponse.getErrorResponse())) {
                log.info("查询孔夫子店铺订单响应失败-{}", JSONObject.toJSONString(ordersResponse.getErrorResponse()));
                List<Long> tokenErrorCode = new ArrayList<>();
                tokenErrorCode.add(1000L);
                tokenErrorCode.add(1001L);
                tokenErrorCode.add(2000L);
                tokenErrorCode.add(2001L);
                if (tokenErrorCode.contains(ordersResponse.getErrorResponse().getCode())) {
                    token = tokenUtils.refreshToken(refreshToken, shopId);
                    isRefreshToken = true;
                } else {
                    throw new ServiceException("查询孔夫子店铺订单异常-" + JSONObject.toJSONString(ordersResponse));
                }
            } else {
                if (ObjectUtil.isNotEmpty(ordersResponse.getSuccessResponse().getList())) {
                    allOrderList.addAll(ordersResponse.getSuccessResponse().getList());
                    if (ordersResponse.getSuccessResponse().getPages() <= ordersResponse.getSuccessResponse().getPageNum())
                        isHaveNext = false;
                    pageNum++;
                } else {
                    isHaveNext = false;
                }
            }
        }
        long startTime = now.toEpochMilli();
        long endTime = Instant.now().toEpochMilli();
        // 获取当前时间时间戳
        log.info("查询孔夫子店铺订单:订单数量-{}，耗时-{}，订单-{}", allOrderList.size(), endTime - startTime, JSONObject.toJSONString(allOrderList));

        // 旧订单列表map
        Map<String, Long> oldOrderMap = new HashMap<>();
        // 若订单列表不为空查询erp订单表近90天该店铺订单数据
        if (ObjectUtil.isNotEmpty(allOrderList)) {
            OrderListByShopIdRequest queryRequest = new OrderListByShopIdRequest();
            queryRequest.setShopId(shop.getId());
            queryRequest.setOrderSnList(allOrderList.stream().map(order -> String.valueOf(order.getOrderId())).filter(Objects::nonNull).collect(Collectors.toList()));
            List<TShopOrderVo> oldOrderList = erpClient.listByShopId(ClientConstantUtils.ERP_URL, queryRequest);
            if (ObjectUtil.isNotEmpty(oldOrderList)) {
                // 将查询出来的旧订单列表转成map，后续用孔夫子订单Id获取erp订单Id，用于更新erp订单
                oldOrderMap = oldOrderList.stream().collect(Collectors.toMap(TShopOrderVo::getOrderSn, TShopOrderVo::getId));
            }

        }

        // 若订单列表不为空
        if (ObjectUtil.isNotEmpty(allOrderList)) {
            List<TShopOrderVo> realOrderList = new ArrayList<>();
            // 根据商品Id过滤订单列表
            for (PageQueryOrdersResponse.Order order : allOrderList) {
                // 商品信息存储实体
                ItemListVo<PageQueryOrdersResponse.Order.Item> realItemList = new ItemListVo<>();
                // 用于存放订单下未知来源异常商品
                List<String> unknownSourceExceptionItemIds = new ArrayList<>();
                for (PageQueryOrdersResponse.Order.Item item : order.getItems()) {
                    if (!shopGoodsIdList.contains(item.getItemId().toString())) {
                        unknownSourceExceptionItemIds.add(item.getItemId().toString());
                    }
                }
                // 判断订单下过滤后的商品是否为空
                // 若为空则跳过该订单
                List<PageQueryOrdersResponse.Order.Item> items = order.getItems();
                if (ObjectUtil.isEmpty(items)) continue;
                // 若不为空保留订单并进行处理
                TShopOrderVo tShopOrderVo = new TShopOrderVo();
                BeanUtils.copyProperties(order, tShopOrderVo);
                PageQueryOrdersResponse.Order.ReceiverInfo receiverInfo = order.getReceiverInfo();
                tShopOrderVo.setOrderSourceType(OrderSourceTypeEnum.KFZ.getCode());
                // 店铺Id
                tShopOrderVo.setShopId(shop.getId().toString());
                // 店铺名称
                tShopOrderVo.setShopName(shop.getShopName());
                // 收件人详细信息对象
                if (ObjectUtil.isNotNull(receiverInfo)) {
                    // 地址
                    tShopOrderVo.setAddress(receiverInfo.getAddress());
                    tShopOrderVo.setAddressMask(receiverInfo.getAddress());
                    tShopOrderVo.setReceiverAddress(receiverInfo.getAddress());
                    tShopOrderVo.setReceiverAddressMask(receiverInfo.getAddress());
                    // 省份
                    tShopOrderVo.setProvince(receiverInfo.getProvName());
                    // 市
                    tShopOrderVo.setCity(receiverInfo.getCityName());
                    // 区，乡镇
                    tShopOrderVo.setTown(receiverInfo.getAreaName());
                    // 收件人姓名
                    tShopOrderVo.setReceiverName(receiverInfo.getReceiverName());
                    tShopOrderVo.setReceiverNameMask(receiverInfo.getReceiverName());
                    // 收件人手机号
                    tShopOrderVo.setReceiverPhone(receiverInfo.getPhoneNum());
                    tShopOrderVo.setReceiverPhoneMask(receiverInfo.getMobile());

                }
                // 售后状态
                if (KfzOrderStatusEnum.PAID_REFUNDING.getCode().equals(order.getOrderStatus())) {
                    tShopOrderVo.setAfterSalesStatus(AfterSalesStatusEnum.BUYER_WAIT_FOR_REFUND.getCode());
                } else if (KfzOrderStatusEnum.PAID_REFUND_REJECTED.getCode().equals(order.getOrderStatus()) || KfzOrderStatusEnum.SHIPPED_REFUND_REJECTED.getCode().equals(order.getOrderStatus())) {
                    tShopOrderVo.setAfterSalesStatus(AfterSalesStatusEnum.REJECT_REFUND_WAIT_FOR_BUYER.getCode());
                } else if (KfzOrderStatusEnum.PAID_REFUNDED.getCode().equals(order.getOrderStatus()) || KfzOrderStatusEnum.SHIPPED_REFUNDED.getCode().equals(order.getOrderStatus()) || KfzOrderStatusEnum.SHIPPED_RETURNED.getCode().equals(order.getOrderStatus())) {
                    tShopOrderVo.setAfterSalesStatus(AfterSalesStatusEnum.REFUND_SUCCESS.getCode());
                } else if (KfzOrderStatusEnum.SHIPPED_REFUNDING.getCode().equals(order.getOrderStatus())) {
                    tShopOrderVo.setAfterSalesStatus(AfterSalesStatusEnum.REFUND_WAIT_FOR_SELLER.getCode());
                } else if (KfzOrderStatusEnum.SHIPPED_RETURNING.getCode().equals(order.getOrderStatus())) {
                    tShopOrderVo.setAfterSalesStatus(AfterSalesStatusEnum.SHIPPED_RETURNING.getCode());
                } else if (KfzOrderStatusEnum.SHIPPED_RETURN_REJECTED.getCode().equals(order.getOrderStatus())) {
                    tShopOrderVo.setAfterSalesStatus(AfterSalesStatusEnum.SHIPPED_RETURN_REJECTED.getCode());
                } else if (KfzOrderStatusEnum.RETURN_PENDING.getCode().equals(order.getOrderStatus())) {
                    tShopOrderVo.setAfterSalesStatus(AfterSalesStatusEnum.RETURN_PENDING.getCode());
                } else if (KfzOrderStatusEnum.RETURNED_TO_RECEIPT.getCode().equals(order.getOrderStatus())) {
                    tShopOrderVo.setAfterSalesStatus(AfterSalesStatusEnum.RETURNED_TO_RECEIPT.getCode());
                } else {
                    tShopOrderVo.setAfterSalesStatus(AfterSalesStatusEnum.NONE.getCode());
                }

                // 成交状态
                if (KfzOrderStatusEnum.SUCCESSFUL.getCode().equals(order.getOrderStatus())) {
                    tShopOrderVo.setConfirmStatus(ConfirmStatusEnum.SOLD.getCode());
                } else if (KfzOrderStatusEnum.BUYER_CANCELLED_BEFORE_CONFIRM.getCode().equals(order.getOrderStatus()) || KfzOrderStatusEnum.BUYER_CANCELLED_BEFORE_PAY.getCode().equals(order.getOrderStatus()) ||
                        KfzOrderStatusEnum.SELLER_CANCELLED_BEFORE_CONFIRM.getCode().equals(order.getOrderStatus()) || KfzOrderStatusEnum.SELLER_CLOSED_BEFORE_PAY.getCode().equals(order.getOrderStatus()) ||
                        KfzOrderStatusEnum.ADMIN_CLOSED_BEFORE_CONFIRM.getCode().equals(order.getOrderStatus()) || KfzOrderStatusEnum.SELLER_CANCELLED_AFTER_PAY.getCode().equals(order.getOrderStatus())) {
                    tShopOrderVo.setConfirmStatus(ConfirmStatusEnum.CANCEL.getCode());
                } else {
                    tShopOrderVo.setConfirmStatus(ConfirmStatusEnum.NOT_SOLD.getCode());
                }
                // 订单状态
                if (KfzOrderStatusEnum.PAID_TO_SHIP.getCode().equals(order.getOrderStatus())) {
                    tShopOrderVo.setOrderStatus(OrderStatusEnum.WAIT_FOR_SHIPMENT.getCode());
                } else if (KfzOrderStatusEnum.SHIPPED_TO_RECEIPT.getCode().equals(order.getOrderStatus())) {
                    tShopOrderVo.setOrderStatus(OrderStatusEnum.SHIPMENT_WAIT_FOR_SIGN.getCode());
                } else if (KfzOrderStatusEnum.SUCCESSFUL.getCode().equals(order.getOrderStatus())) {
                    tShopOrderVo.setOrderStatus(OrderStatusEnum.SIGNED.getCode());
                }
                // 买家留言信息
                tShopOrderVo.setBuyerMemo(order.getBuyerRemark());
                // 成交时间
                tShopOrderVo.setConfirmTime(order.getPayTime());
                // 折扣金额，单位：元，折扣金额=平台优惠+商家优惠+团长免单优惠金额
                tShopOrderVo.setDiscountAmount(new BigDecimal(order.getFavorableMoney()));
                // 商品金额
                tShopOrderVo.setGoodsAmount(new BigDecimal(order.getGoodsAmount()));
                // 订单编号
                tShopOrderVo.setOrderSn(order.getOrderId().toString());
                // 支付金额
                tShopOrderVo.setPayAmount(new BigDecimal(order.getOrderAmount()));
                // 支付时间
                tShopOrderVo.setPayTime(order.getPayTime());
                // 平台优惠金额默认平台优惠为0
                tShopOrderVo.setPlatformDiscount(new BigDecimal("0.00"));
                // 邮费
                tShopOrderVo.setPostage(new BigDecimal(order.getShippingFee()));
                // 确认收货时间
                tShopOrderVo.setReceiveTime(order.getFinishTime());
                // 订单备注
                tShopOrderVo.setRemark(order.getSellerRemarkText());
                // 备注类型, 0:未打标记,1:红,2:黄,3:绿,4:蓝,5:紫
                tShopOrderVo.setRemarkTag(order.getSellerFlagType().toString());
                // 订单审核状态（0-正常订单， 1-审核中订单）
                tShopOrderVo.setRiskControlStatus(0);
                // 商家优惠金额
                tShopOrderVo.setSellerDiscount(new BigDecimal(order.getFavorableMoney()));
                // 快递单号
                tShopOrderVo.setTrackingNumber(order.getShipmentNum());
                // 订单类型 0-普通订单 ，1- 定金订单
                tShopOrderVo.setTradeType(0);
                // 附属额外信息
                tShopOrderVo.setExtraInfo(JSONObject.toJSONString(order.getReceiverInfo()));

                // 封装商品信息转成JSON存储在订单数据实体类中
                realItemList.setOrderItems(items);
                // 封装未知商品来源商品信息
                ItemListVo.ExceptionItem goodsSourceUnknownExceptionItem = new ItemListVo.ExceptionItem();
                goodsSourceUnknownExceptionItem.setOrderExceptionType(OrderExceptionTypeEnum.GOODS_SOURCE_UNKNOWN_EXCEPTION.getCode());
                goodsSourceUnknownExceptionItem.setOrderItemId(unknownSourceExceptionItemIds);
                List<ItemListVo.ExceptionItem> exceptionItemList = new ArrayList<>();
                exceptionItemList.add(goodsSourceUnknownExceptionItem);
                // 尝试去旧订单中获取订单id，看是否能获取到
                Long orderId = oldOrderMap.get(order.getOrderId().toString());
                if (ObjectUtil.isNotNull(orderId)) tShopOrderVo.setId(orderId);
                // 查询订单是否已存在
                // 封装操作库存失败商品信息
                // 用于存放订单下未知来源异常商品
                List<String> inventoryExceptionItemIds = new ArrayList<>();
                OperatingInventoryVo operatingInventoryVo = new OperatingInventoryVo();
                // TODO 暂时只做新订单扣减库存操作
                // 查询旧订单看是否已存在
                if (ObjectUtil.isNull(orderId)) {
                    for (PageQueryOrdersResponse.Order.Item item : items) {
                        try { // 默认拼多多店铺-1
                            operatingInventoryVo.setShopType(2);
                            // 扣减库存类型
                            operatingInventoryVo.setOperationType(2);
                            operatingInventoryVo.setMallId(shop.getMallId());
                            OperatingInventoryVo.GoodsItem goodsItem = new OperatingInventoryVo.GoodsItem();
                            goodsItem.setPlatformId(item.getItemId().toString());
                            goodsItem.setCount(item.getNumber());
                            operatingInventoryVo.setGoodsItems(List.of(goodsItem));
                            erpClient.OperatingInventory(ClientConstantUtils.ERP_URL, operatingInventoryVo);
                        } catch (Exception e) {
                            log.error("收到孔夫子推送订单消息，操作库存失败：孔夫子订单-{},操作库存请求-{}，异常信息-{}", JSONObject.toJSONString(order), JSONObject.toJSONString(operatingInventoryVo), e.getMessage());
                            inventoryExceptionItemIds.add(item.getItemId().toString());
                        }
                    }
                }
                ItemListVo.ExceptionItem inventoryExceptionItem = new ItemListVo.ExceptionItem();
                inventoryExceptionItem.setOrderExceptionType(OrderExceptionTypeEnum.INVENTORY_EXCEPTION.getCode());
                inventoryExceptionItem.setOrderItemId(inventoryExceptionItemIds);
                exceptionItemList.add(inventoryExceptionItem);
                realItemList.setItemList(exceptionItemList);
                tShopOrderVo.setItemList(JSONObject.toJSONString(realItemList));
                // 判断是否存在异常商品
                // 订单异常类型枚举集合
                List<String> exceptionTypeList = new ArrayList<>();
                if (!unknownSourceExceptionItemIds.isEmpty()) {
                    // 商品未知来源异常
                    exceptionTypeList.add(OrderExceptionTypeEnum.GOODS_SOURCE_UNKNOWN_EXCEPTION.getCode());
                }
                if (!inventoryExceptionItemIds.isEmpty()) {
                    // 商品库存操作异常
                    exceptionTypeList.add(OrderExceptionTypeEnum.INVENTORY_EXCEPTION.getCode());
                }
                tShopOrderVo.setOrderExceptionType(JSONObject.toJSONString(exceptionTypeList));
                realOrderList.add(tShopOrderVo);
            }
            // 新增并更新订单
            erpClient.insertOrUpdateOrderBatch(ClientConstantUtils.ERP_URL, realOrderList);
        }
        // 更新最后同步时间
        erpClient.updateTime(ClientConstantUtils.ERP_URL, shop.getId(), now.toEpochMilli());
    }


    @Override
    public List<LogisticsMethodResponse> deliveryMethodList(Long shopId) {
        // 根据shopId获取店铺信息
        ShopVo shop = erpClient.getShopInfo(ClientConstantUtils.ERP_URL, shopId);
        String token = shop.getToken();
        String refreshToken = shop.getRefreshToken();

        boolean flag = true; // 循环标志
        boolean isRefreshToken = false; // 是否已刷新token

        KfzBaseResponse<List<DeliveryMethodResponse>> response = null;

        while (flag) {
            response = phpClient.deliveryMethodList(ClientConstantUtils.PHP_URL, token);
            if (!isRefreshToken && ObjectUtil.isNotEmpty(response.getErrorResponse())) {
                // 若ErrorResponse不为空判断是否需要刷新token
                log.info("查询孔夫子获取配送方式列表响应失败-{}", JSONObject.toJSONString(response.getErrorResponse()));
                List<Long> tokenErrorCode = new ArrayList<>();
                tokenErrorCode.add(1000L);
                tokenErrorCode.add(1001L);
                tokenErrorCode.add(2000L);
                tokenErrorCode.add(2001L);
                if (tokenErrorCode.contains(response.getErrorResponse().getCode())) {
                    // token原因请求失败则刷新token
                    token = tokenUtils.refreshToken(refreshToken, shopId);
                    isRefreshToken = true;
                } else {
                    throw new ServiceException("查询孔夫子获取配送方式列表异常-" + JSONObject.toJSONString(response));
                }
            } else {
                flag = false;
            }
        }
        if (ObjectUtil.isEmpty(response.getSuccessResponse())) return new ArrayList<>();
        List<LogisticsMethodResponse> logisticsMethodResponses = new ArrayList<>();
        for (DeliveryMethodResponse deliveryMethodResponse : response.getSuccessResponse()) {
            if (ObjectUtil.isNotEmpty(deliveryMethodResponse.getCompanies())) {
                for (DeliveryMethodResponse.company company : deliveryMethodResponse.getCompanies()) {
                    LogisticsMethodResponse logisticsMethod = LogisticsMethodResponse.builder()
                            .methodId(deliveryMethodResponse.getShippingId() + "_" + company.getShippingCom())
                            .methodName(deliveryMethodResponse.getShippingName() + "——" + company.getShippingComName())
                            .build();
                    logisticsMethodResponses.add(logisticsMethod);
                }
            } else {
                LogisticsMethodResponse logisticsMethod = LogisticsMethodResponse.builder()
                        .methodId(deliveryMethodResponse.getShippingId())
                        .methodName(deliveryMethodResponse.getShippingName())
                        .build();
                logisticsMethodResponses.add(logisticsMethod);
            }
        }
        return logisticsMethodResponses;
    }

    @Override
    public R<Boolean> orderDelivery(Long shopId, Long orderId, String shippingId, String shippingCom, String shipmentNum, String userDefined, String moreShipmentNum) {
        // 根据shopId获取店铺信息
        ShopVo shop = erpClient.getShopInfo(ClientConstantUtils.ERP_URL, shopId);
        String token = shop.getToken();
        String refreshToken = shop.getRefreshToken();

        boolean flag = true; // 循环标志
        boolean isRefreshToken = false; // 是否已刷新token

        KfzBaseResponse<OrderDeliveryResponse> response = null;

        while (flag) {
            response = phpClient.orderDelivery(ClientConstantUtils.PHP_URL, token, orderId, shippingId, shippingCom, shipmentNum, userDefined, moreShipmentNum);
            if (!isRefreshToken && ObjectUtil.isNotEmpty(response.getErrorResponse())) {
                // 若ErrorResponse不为空判断是否需要刷新token
                log.info("查询孔夫子获取配送方式列表响应失败-{}", JSONObject.toJSONString(response.getErrorResponse()));
                List<Long> tokenErrorCode = new ArrayList<>();
                tokenErrorCode.add(1000L);
                tokenErrorCode.add(1001L);
                tokenErrorCode.add(2000L);
                tokenErrorCode.add(2001L);
                if (tokenErrorCode.contains(response.getErrorResponse().getCode())) {
                    // token原因请求失败则刷新token
                    token = tokenUtils.refreshToken(refreshToken, shopId);
                    isRefreshToken = true;
                } else {
                    return new R<Boolean>(Integer.getInteger(response.getErrorResponse().getSubCode()), response.getErrorResponse().getSubMsg(), Boolean.FALSE);
                }
            } else {
                flag = false;
            }
        }
        return R.ok(Boolean.TRUE);
    }

}
