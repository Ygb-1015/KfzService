package com.order.main.util;


import org.springframework.stereotype.Component;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Component
public final class UploadUtil {

    /**
     * 获取文件路径
     *
     * @param bookName 书名
     * @return
     */
    public static String getFiles(String imgVesselUrl, String bookName) {
        if (imgVesselUrl.equals("")) {
            imgVesselUrl = "https://img.buzhiyushu.cn/zhishu1";
//            imgVesselUrl = "http://111.229.25.150:9000/zhishu1";
            //获取加密后打首字母大写
            String result = getMd5FirstChart(bookName);

            return imgVesselUrl + "/" + result + "/";
        } else {
            return imgVesselUrl;
        }
    }

    /**
     * 根据MD5加密后获取大写首字母
     * @param bookName
     * @return
     */
    public static String getMd5FirstChart(String bookName){
        //书名进行md5加密
        String bookNameMd5 = encryptByMd5(bookName);
        //获取加密后打首字母大写
        return getFirstCharToUpper(bookNameMd5);
    }

    public static String getFirstCharToUpper(String input) {
        if (input == null || input.isEmpty()) {
            return ""; // 如果字符串为空或长度为0，返回空字符串
        }
        // 获取第一个字符并转为大写
        return String.valueOf(input.charAt(0)).toUpperCase();
    }

    public static String encryptByMd5(String input) {
        try {
            // 创建 MessageDigest 实例，指定 MD5 算法
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 将输入字符串转换为字节数组并进行加密
            byte[] digest = md.digest(input.getBytes());
            // 将字节数组转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 加密失败", e);
        }
    }

}
