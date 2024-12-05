package org.example.libraryfxproject.Service;

import org.example.libraryfxproject.Export.DataExporter;
import org.example.libraryfxproject.Export.ExporterFactory;
import org.example.libraryfxproject.Model.User;
import org.example.libraryfxproject.Util.Exception.ExportException;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExportService {
    private final DataExporter exporter;

    /**
     * Constructor để tạo một thể hiện của dịch vụ xuất dữ liệu với loại xuất đã chọn.
     * @param exportType Loại xuất (ví dụ: CSV, XML, JSON) từ {@link ExporterFactory.ExportType}.
     */
    public ExportService(ExporterFactory.ExportType exportType) {
        this.exporter = ExporterFactory.createExporter(exportType);
    }

    /**
     * Xuất dữ liệu sinh viên ra tệp với tên và định dạng được tạo tự động, lưu trữ tại vị trí chỉ định.
     * Sau khi hoàn thành, thông báo sẽ được gửi qua callback.
     * @param userList Danh sách sinh viên (User) cần xuất
     * @param baseFilePath Đường dẫn thư mục nơi tệp sẽ được lưu
     * @param exportCallback Callback để thông báo kết quả xuất dữ liệu (thành công hoặc thất bại)
     */
    public void exportStudentData(List<User> userList, String baseFilePath, ExportCallback exportCallback) {
        String fileName = "students_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) +
                exporter.getFileExtension(); // Tạo tên tệp với ngày giờ hiện tại và phần mở rộng của tệp
        String fullPath = baseFilePath + File.separator + fileName; // Tạo đường dẫn đầy đủ cho tệp xuất
        try {
            exporter.exportData(userList, fullPath); // Xuất dữ liệu
            exportCallback.onSuccess(fullPath); // Gọi callback khi xuất thành công
        } catch (ExportException exportException) {
            exportCallback.onError(exportException.getMessage()); // Gọi callback khi có lỗi trong quá trình xuất
        }
    }

    /**
     * Interface callback để thông báo kết quả của quá trình xuất dữ liệu.
     */
    public interface ExportCallback {
        /**
         * Phương thức gọi khi xuất dữ liệu thành công.
         * @param filePath Đường dẫn đến tệp xuất đã hoàn thành
         */
        void onSuccess(String filePath);

        /**
         * Phương thức gọi khi có lỗi trong quá trình xuất dữ liệu.
         * @param errorMessage Thông báo lỗi chi tiết
         */
        void onError(String errorMessage);
    }
}
