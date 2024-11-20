package org.example.libraryfxproject.Service;

import org.example.libraryfxproject.Model.User;
import org.example.libraryfxproject.Util.DataExporter;
import org.example.libraryfxproject.Util.Exception.ExportException;
import org.example.libraryfxproject.Util.ExporterFactory;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExportService {
    private final DataExporter exporter;

    public ExportService(ExporterFactory.ExportType exportType) {
        this.exporter = ExporterFactory.createExporter(exportType);
    }

    public void exportStudentData(List<User> userList, String baseFilePath) throws ExportException {
        String fileName = "students_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) +
                exporter.getFileExtension();
        String fullPath = baseFilePath + File.separator + fileName;

        exporter.exportData(userList, fullPath);
    }
}
