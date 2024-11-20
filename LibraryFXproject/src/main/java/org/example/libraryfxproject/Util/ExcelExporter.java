package org.example.libraryfxproject.Util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.libraryfxproject.Util.Exception.ExportException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class ExcelExporter implements DataExporter {
    @Override
    public void exportData(List<?> data, String filePath) throws ExportException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Data");
            if (data.isEmpty()) {
                throw new ExportException("No data to export");
            }
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Có thể lấy được bất cứ đối tượng nào :DD
            Field[] fields = data.get(0).getClass().getDeclaredFields();
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < fields.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(fields[i].getName());
                cell.setCellStyle(headerStyle);
            }
            for (int i = 0; i < data.size(); i++) {
                Row row = sheet.createRow(i + 1);
                Object item = data.get(i);
                for (int j = 0; j < fields.length; j++) {
                    fields[j].setAccessible(true);
                    row.createCell(j).setCellValue(String.valueOf(fields[j].get(item)));
                }
            }
            for (int i = 0; i < fields.length; i++) {
                sheet.autoSizeColumn(i);
            }
            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
            }
        } catch (IOException | IllegalAccessException e) {
            throw new ExportException("Failed to export to Excel", e);
        }
    }

    @Override
    public String getFileExtension() {
        return ".xlsx";
    }
}
