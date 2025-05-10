package com.order.main.util;

import org.springframework.stereotype.Component;

@Component
public final class UrlUtil {

//    public static String getUrl(){
//        return "/zhishu/";
//    }

    public static String getUrl(){
        return "./file/";
    }

    public static String getPath(){
        return "https://api.buzhiyushu.cn";
//        return "http://localhost:8080";
    }
}
