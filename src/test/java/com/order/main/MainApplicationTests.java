package com.order.main;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@MapperScan("com.order.main.mapper")
class MainApplicationTests {

	@Test
	void contextLoads() {
	}

}
