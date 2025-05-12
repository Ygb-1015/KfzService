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
        return ClientConstantUtils.ERP_URL;
    }
}
