package com.order.main.util;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class VerifyPriceExcelMerger {

    /**
     * 合并指定目录下的所有Excel文件
     * @param sourceDirectory 源目录路径
     * @param outputPath 输出文件路径
     * @throws IOException 如果发生IO错误
     */
    public static void mergeExcelFiles(String sourceDirectory, String outputPath) throws IOException {
        // 获取目录下所有Excel文件
        File dir = new File(sourceDirectory);
        File[] files = dir.listFiles((d, name) ->
                name.endsWith(".xls") || name.endsWith(".xlsx"));

        if (files == null || files.length == 0) {
            System.out.println("目录中没有找到Excel文件");
            return;
        }

        // 创建新的工作簿用于合并结果
        Workbook mergedWorkbook = new XSSFWorkbook();
        Sheet mergedSheet = mergedWorkbook.createSheet("合并结果");

        // 创建表头
        String[] headers = {"ISBN", "孔总价（书价+运费）", "原始售价", "差价", "图片链接", "在售数量"};
        createHeaderRow(mergedSheet, headers);

        int currentRow = 1; // 从第2行开始（表头在第1行）

        // 遍历所有Excel文件
        for (File file : files) {
            try (FileInputStream fis = new FileInputStream(file);
                 Workbook workbook = WorkbookFactory.create(fis)) {

                // 获取第一个工作表
                Sheet sheet = workbook.getSheetAt(0);

                // 遍历所有行（跳过表头）
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row sourceRow = sheet.getRow(i);
                    if (sourceRow == null) continue;

                    // 创建新行
                    Row newRow = mergedSheet.createRow(currentRow++);

                    // 复制单元格数据
                    for (int j = 0; j < 6; j++) { // A-F列
                        Cell sourceCell = sourceRow.getCell(j);
                        if (sourceCell != null) {
                            Cell newCell = newRow.createCell(j);
                            copyCellValue(sourceCell, newCell);
                        }
                    }
                }
            }
        }

        // 自动调整列宽
        for (int i = 0; i < headers.length; i++) {
            mergedSheet.autoSizeColumn(i);
        }

        // 保存合并后的文件
        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            mergedWorkbook.write(fos);
        }

        System.out.println("Excel文件合并完成，保存至: " + outputPath);
    }

    /**
     * 创建表头行
     * @param sheet 工作表
     * @param headers 表头数组
     */
    private static void createHeaderRow(Sheet sheet, String[] headers) {
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
    }

    /**
     * 复制单元格值
     * @param sourceCell 源单元格
     * @param newCell 目标单元格
     */
    private static void copyCellValue(Cell sourceCell, Cell newCell) {
        switch (sourceCell.getCellType()) {
            case STRING:
                newCell.setCellValue(sourceCell.getStringCellValue());
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(sourceCell)) {
                    newCell.setCellValue(sourceCell.getDateCellValue());
                } else {
                    newCell.setCellValue(sourceCell.getNumericCellValue());
                }
                break;
            case BOOLEAN:
                newCell.setCellValue(sourceCell.getBooleanCellValue());
                break;
            case FORMULA:
                newCell.setCellFormula(sourceCell.getCellFormula());
                break;
            default:
                newCell.setCellValue("");
        }
    }
}
