package com.order.main.mapper;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
@SpringBootTest
class PddShopMapperTest {

    @Autowired
    private PddShopMapper pddShopMapper; // PddShopMapper对象



    @Test
    void testFindAll() {
        List<PddShop> shop = pddShopMapper.findAll();
        System.out.println("====================================================");
        System.out.println(shop);
        System.out.println("====================================================");
    }
}


