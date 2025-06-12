package com.order.main.util;

import org.springframework.stereotype.Component;

@Component
public final class UrlUtil {

    // public static String getUrl() {
    //     return "/zhishu/";
    // }

    public static String getUrl(){
        return "/www/wwwroot/baseApi/file/";
        //       return "D:/work/erp/file/";
    }

    /**
     * 获取孔夫子静态图片文件存放的域名
     * 主要用于生成的 水印图和白图文件存放
     * @return
     */
    public static String getImageUrl(){
        return "https://temp.img.buzhiyushu.cn/";
    }

    public static String getImageUrlStatic(){
        return "/www/wwwroot/temp/img/";
    }

    public static String getPath(){
        return ClientConstantUtils.ERP_URL;
    }
}
