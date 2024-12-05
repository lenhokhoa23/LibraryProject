package org.example.libraryfxproject.Export;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.libraryfxproject.Util.Exception.ExportException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ExcelExporter implements DataExporter {

    /**
     * Xuất dữ liệu sang tệp Excel tại đường dẫn chỉ định.
     * @param data danh sách dữ liệu cần xuất
     * @param filePath đường dẫn tệp Excel
     * @throws ExportException nếu có lỗi trong quá trình xuất
     */
    @Override
    public void exportData(List<?> data, String filePath) throws ExportException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Data");

            if (data.isEmpty()) {
                throw new ExportException("No data to export");
            }
            List<Field> allFields = getAllFields(data.get(0).getClass());
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < allFields.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(allFields.get(i).getName());
                cell.setCellStyle(headerStyle);
            }

            for (int i = 0; i < data.size(); i++) {
                Row row = sheet.createRow(i + 1);
                Object item = data.get(i);
                for (int j = 0; j < allFields.size(); j++) {
                    Field field = allFields.get(j);
                    field.setAccessible(true);
                    row.createCell(j).setCellValue(String.valueOf(field.get(item)));
                }
            }
            for (int i = 0; i < allFields.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
            }
        } catch (IOException | IllegalAccessException e) {
            throw new ExportException("Failed to export to Excel", e);
        }
    }

    /**
     * Lấy phần mở rộng của tệp xuất.
     * @return phần mở rộng tệp (".xlsx")
     */
    @Override
    public String getFileExtension() {
        return ".xlsx";
    }

    /**
     * Lấy tất cả các trường của lớp (bao gồm các trường kế thừa).
     * @param clazz lớp cần lấy trường
     * @return danh sách các trường
     */
    public List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        if (clazz != null) {
            fields.addAll(getAllFields(clazz.getSuperclass()));
            for (Field field : clazz.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    fields.add(field);
                }
            }
        }
        return fields;
    }
}

