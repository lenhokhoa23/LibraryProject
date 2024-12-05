package org.example.libraryfxproject.Export;

import org.example.libraryfxproject.Util.Exception.ExportException;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CSVExporter implements DataExporter {

    /**
     * Xuất dữ liệu sang tệp CSV tại đường dẫn chỉ định.
     * @param data danh sách dữ liệu cần xuất
     * @param filePath đường dẫn tệp CSV
     * @throws ExportException nếu có lỗi trong quá trình xuất
     */
    @Override
    public void exportData(List<?> data, String filePath) throws ExportException {
        try (FileWriter writer = new FileWriter(filePath)) {
            if (data.isEmpty()) {
                throw new ExportException("No data to export");
            }
            List<Field> fields = getAllFields(data.get(0).getClass());

            writer.write(String.join(",", fields.stream()
                    .map(Field::getName)
                    .collect(Collectors.toList())) + "\n");

            for (Object item : data) {
                List<String> values = new ArrayList<>();
                for (Field field : fields) {
                    field.setAccessible(true);
                    values.add(String.valueOf(field.get(item)));
                }
                writer.write(String.join(",", values) + "\n");
            }
        } catch (IOException | IllegalAccessException e) {
            throw new ExportException("Failed to export to CSV", e);
        }
    }

    /**
     * Lấy phần mở rộng của tệp xuất.
     * @return phần mở rộng tệp (".csv")
     */
    @Override
    public String getFileExtension() {
        return ".csv";
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
