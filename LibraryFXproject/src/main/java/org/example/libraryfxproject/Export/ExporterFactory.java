package org.example.libraryfxproject.Export;

public class ExporterFactory {
    public static DataExporter createExporter(ExportType type) {
        return switch (type) {
            case CSV -> new CSVExporter();
            case EXCEL -> new ExcelExporter();
            default -> throw new IllegalArgumentException("Unsupported export type: " + type);
        };
    }

    public enum ExportType {
        CSV, EXCEL
    }
}
