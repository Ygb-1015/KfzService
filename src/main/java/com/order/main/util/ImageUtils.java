package com.order.main.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class ImageUtils {

    private static final String IMAGE_PATH = "/www/wwwroot/temp/img/"; // 图片完整路径
//    private static final String IMAGE_PATH = "D:/zhishu/";
    /**
     * 生成图片并保存到缓存文件夹
     *
     * @param text   要显示的字符串
     * @param width  图片宽度
     * @param height 图片高度
     * @param fileName 文件名称  例如 123.jpg
     */
    public static String generateImage(String text, int width, int height,String fileName) {
        // 创建一个白底的BufferedImage
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        // 设置字体和颜色
        Font font = new Font("Alibaba PuHuiTi", Font.BOLD, 40);
        g2d.setFont(font);
        g2d.setColor(Color.BLACK);

        // 计算字符串的宽度和高度
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();

        // 计算字符串的绘制位置
        int x = (width - textWidth) / 2;
        int y = (height - textHeight) / 2 + fm.getAscent();

        // 绘制字符串
        g2d.drawString(text, x, y);

        // 释放资源
        g2d.dispose();

        // 保存图片到指定目录下
        try {
            File outputFile = new File(IMAGE_PATH+fileName);
            ImageIO.write(image, "jpg", outputFile);
            return UrlUtil.getImageUrl()+fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void deleteImage(String absolutePath) {
        File file = new File(absolutePath);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("文件删除成功: " + absolutePath);
            } else {
                System.out.println("文件删除失败: " + absolutePath);
            }
        } else {
            System.out.println("文件不存在: " + absolutePath);
        }
    }


    public static boolean isImageExists(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 将主图和水印图片合成一张，水印图片完美覆盖主图
     *
     * @param mainImageUrl    主图的 URL
     * @param watermarkImageUrl 水印图片的 URL
     * @param fileName  文件名称
     */
    public static String mergeImages(String mainImageUrl, String watermarkImageUrl,String fileName) throws IOException {
        // 从 URL 读取主图
        BufferedImage mainImage = ImageIO.read(new URL(mainImageUrl));
        // 从 URL 读取水印图片
        BufferedImage watermarkImage = ImageIO.read(new URL(watermarkImageUrl));

        // 获取主图的宽高
        int mainWidth = mainImage.getWidth();
        int mainHeight = mainImage.getHeight();

        // 将水印图片缩放到主图的尺寸
        Image scaledWatermark = watermarkImage.getScaledInstance(mainWidth, mainHeight, Image.SCALE_SMOOTH);

        // 创建一个新的 BufferedImage，类型为 ARGB（支持透明度）
        BufferedImage combined = new BufferedImage(
                mainWidth, mainHeight, BufferedImage.TYPE_INT_ARGB);

        // 获取 Graphics2D 对象
        Graphics2D g2d = combined.createGraphics();

        // 绘制主图
        g2d.drawImage(mainImage, 0, 0, null);
        // 绘制缩放后的水印图片
        g2d.drawImage(scaledWatermark, 0, 0, null);

        // 释放资源
        g2d.dispose();

        File outputFile = new File(IMAGE_PATH+fileName);
        ImageIO.write(combined, "PNG", outputFile);

        // 返回临时文件的路径
        return UrlUtil.getImageUrl()+fileName;
    }

}
