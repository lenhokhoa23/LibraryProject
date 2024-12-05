package org.example.libraryfxproject.Service;

import org.example.libraryfxproject.Export.DataExporter;
import org.example.libraryfxproject.Export.ExporterFactory;
import org.example.libraryfxproject.Model.User;
import org.example.libraryfxproject.Util.Exception.ExportException;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class  ExportService {
    private final DataExporter exporter;

    public ExportService(ExporterFactory.ExportType exportType) {
        this.exporter = ExporterFactory.createExporter(exportType);
    }

    public void exportStudentData(List<User> userList, String baseFilePath, ExportCallback exportCallback) {
        String fileName = "students_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) +
                exporter.getFileExtension();
        String fullPath = baseFilePath + File.separator + fileName;
        try {
            exporter.exportData(userList, fullPath);
            exportCallback.onSuccess(fullPath);
        } catch (ExportException exportException) {
            exportCallback.onError(exportException.getMessage());
        }
    }

    public interface ExportCallback {
        void onSuccess(String filePath);
        void onError(String errorMessage);
    }
}
