package org.example.libraryfxproject.Export;


public class ExporterFactory {

    /**
     * Tạo đối tượng DataExporter dựa trên loại xuất dữ liệu.
     * @param type loại xuất dữ liệu (CSV hoặc EXCEL)
     * @return đối tượng DataExporter tương ứng
     * @throws IllegalArgumentException nếu loại xuất không được hỗ trợ
     */
    public static DataExporter createExporter(ExportType type) {
        return switch (type) {
            case CSV -> new CSVExporter();
            case EXCEL -> new ExcelExporter();
            default -> throw new IllegalArgumentException("Unsupported export type: " + type);
        };
    }

    /**
     * Enum các loại xuất dữ liệu hỗ trợ.
     */
    public enum ExportType {
        CSV, EXCEL
    }
}

