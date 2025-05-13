package com.order.main.util;

import org.springframework.stereotype.Component;

@Component
public final class BookFilterUtil {

    public static final String[] BOOKNAMEFILTERSTR = {
//            "\\d+-\\d+册","\\d+、\\d+册","\\d+册",
//            "上册","上中册","上中下册","中册","中下册","下册"
//            ,"全\\d+册","全\\d+本","全\\d+部","全\\d+集","全\\d+卷","全\\d+辑"
//            ,"共\\d+册","共\\d+本","共\\d+部","共\\d+集","共\\d+卷","共\\d+辑"
            };

    public static final String AUTHORFILTERSTR = "";

    public static final String PUBLISHERFILTERSTR = "";

    public static final String PUBLICATIONTIMEFILTERSTR = "";

    public static final String ISBNFILTERSTR = "";

}
