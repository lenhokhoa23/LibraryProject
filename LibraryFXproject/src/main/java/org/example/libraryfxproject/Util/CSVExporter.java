package org.example.libraryfxproject.Util;

import org.example.libraryfxproject.Util.Exception.ExportException;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CSVExporter implements DataExporter {
    @Override
    public void exportData(List<?> data, String filePath) throws ExportException {
        try (FileWriter writer = new FileWriter(filePath)) {
            if (data.isEmpty()) {
                throw new ExportException("No data to export");
            }

            // Lấy các trường của đối tượng bằng Reflection
            Field[] fields = data.get(0).getClass().getDeclaredFields();

            // Viết header
            writer.write(String.join(",", Arrays.stream(fields)
                    .map(Field::getName)
                    .collect(Collectors.toList())) + "\n");

            // Viết data
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

    @Override
    public String getFileExtension() {
        return ".csv";
    }
}
