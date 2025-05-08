package com.order.main.util;


import com.alibaba.fastjson.JSON;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * EasyExcel工具类
 *
 * @author CWW
 * @date 2024/12/18 16:46
 */
public class EasyExcelUtil {

    private static final String UTF_8 = "UTF-8";


    /**
     * 将数据列表写入 Excel 文件，并设置表头
     *
     * @param filePath 文件路径
     * @param data     数据列表
     * @param headMap  表头配置，键为字段名，值为表头显示名称
     */
    public static void writeExcel(String filePath, List<Map<String,String>> data, Map<String, String> headMap) {
        try (
            Workbook workbook = new XSSFWorkbook();
            FileOutputStream fileOut = new FileOutputStream(filePath)) {
            Sheet sheet = workbook.createSheet("Sheet1");
            // 创建表头
            Row headerRow = sheet.createRow(0);
            Cell headerCell = headerRow.createCell(0);
            headerCell.setCellValue("书号");
            Cell headerCell4 = headerRow.createCell(1);
            headerCell4.setCellValue("书名");
            Cell headerCell1 = headerRow.createCell(2);
            headerCell1.setCellValue("价格");
            Cell headerCell2 = headerRow.createCell(3);
            headerCell2.setCellValue("库存");
            Cell headerCell3 = headerRow.createCell(4);
            headerCell3.setCellValue("日志");
            Cell headerCell5 = headerRow.createCell(5);
            headerCell5.setCellValue("三方平台商品id");
            workbook.write(fileOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将 Map 列表转换为 JSON 字符串并存储到文本文件中
     *
     * @param fileName 文件名称
     * @param data     数据列表
     */
    public static void writeJsonToFile(String fileName,List<Map<String, Object>> data) {
        try {
            String filePath = UrlUtil.getUrl()+fileName;
            // 获取文件路径的父目录
            Path parentDir = Paths.get(filePath).getParent();
            // 如果父目录不存在，则创建它
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }

            // 将 Map 列表转换为 JSON 字符串
            String jsonString = JSON.toJSONString(data);

            // 将 JSON 字符串写入文件
            try (FileWriter fileWriter = new FileWriter(filePath)) {
                fileWriter.write(jsonString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeJsonToFile(String fileName,String jsonString) {
        try {
            String filePath = UrlUtil.getUrl()+fileName;
            // 获取文件路径的父目录
            Path parentDir = Paths.get(filePath).getParent();
            // 如果父目录不存在，则创建它
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }

            // 将 JSON 字符串写入文件
            try (FileWriter fileWriter = new FileWriter(filePath)) {
                fileWriter.write(jsonString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取text文本文件中的字符串
     * @param filePath
     * @return
     */
    public static List<Map<String,Object>> readFileContent(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try {
            Path path = Paths.get(filePath);
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                contentBuilder.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Map<String,Object>> mapList = JSON.parseObject(contentBuilder.toString(), List.class);
        return mapList;
    }

    public static Map<String,Object> readFileContentMap(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try {
            Path path = Paths.get(filePath);
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                contentBuilder.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String,Object> map = JSON.parseObject(contentBuilder.toString(), Map.class);
        return map;
    }

    public static String readFileContentString(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try {
            Path path = Paths.get(filePath);
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                contentBuilder.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }


    /**
     * 根据文件路径持续写入数据
     * @param excelFilePath 文件路径
     */
    public static void continuousWriting(String excelFilePath,List<String> list) {
        try (
            FileInputStream fis = new FileInputStream(excelFilePath);
            Workbook workbook = new XSSFWorkbook(fis)) {
            // 获取第一个工作表
            Sheet sheet = workbook.getSheetAt(0);
            // 创建一行
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);
            // 创建单元格并写入数据
            for(int i=0;i<list.size();i++){
                Cell cell = row.createCell(i);
                cell.setCellValue(list.get(i));
            }
            // 写入文件
            try (FileOutputStream fos = new FileOutputStream(excelFilePath)) {
                workbook.write(fos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
