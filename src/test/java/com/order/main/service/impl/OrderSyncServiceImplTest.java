package com.order.main.service.impl;

import com.pdd.pop.sdk.http.PopHttpClient;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class OrderSyncServiceImplTest {

    @Autowired
    private PopHttpClient popHttpClient;

    @Autowired
    private PddShopMapper pddShopMapper;

    @Autowired
    private PddOrderMapper pddOrderMapper;

    @Autowired
    private OrderSyncServiceImpl orderSyncService;

    private static final Logger log = LoggerFactory.getLogger(OrderSyncServiceImplTest.class);

    private PddShop shop;

    @Test
    public void testSyncOrders() throws Exception {
        orderSyncService.fullSynchronizationOrder(null);
    }
}

