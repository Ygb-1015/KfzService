package com.order.main.util;


import org.springframework.web.client.RestTemplate;

import java.util.Map;


public class InterfaceUtils {

    public static void getInterface(String url) {
        // 目标接口 URL
        url = UrlUtil.getPath()+ url;
        // 创建 RestTemplate 实例
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getForEntity(url, String.class);
    }

    public static void getInterfacePost(String url, Map params) {
        // 目标接口 URL
        url = UrlUtil.getPath()+ url;
        // 创建 RestTemplate 实例
        RestTemplate restTemplate = new RestTemplate();
        // 发起 Post 请求
        restTemplate.postForEntity(url,params,String.class);

    }

    public static String getPath() {
        return UrlUtil.getPath();
    }
}

