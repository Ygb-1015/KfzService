package com.order.main;

import com.pdd.pop.ext.glassfish.tyrus.core.wsadl.model.Application;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication(scanBasePackages = "com.order.main")
@MapperScan("com.order.main.mapper")
@RestController
public class MainApplication extends SpringBootServletInitializer {
	@Override
	public SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MainApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

	@GetMapping("/test")
	public String test() {
		return "Hello World";
	}

}
