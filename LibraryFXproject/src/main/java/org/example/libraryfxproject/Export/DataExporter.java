package org.example.libraryfxproject.Export;

import org.example.libraryfxproject.Util.Exception.ExportException;

import java.util.List;

/**
 * Interface cho các lớp xuất dữ liệu.
 * Các lớp triển khai sẽ thực hiện việc xuất dữ liệu sang các định dạng tệp khác nhau.
 */
public interface DataExporter {

    /**
     * Xuất dữ liệu sang tệp tại đường dẫn chỉ định.
     * @param data danh sách dữ liệu cần xuất
     * @param filePath đường dẫn tệp
     * @throws ExportException nếu có lỗi trong quá trình xuất
     */
    void exportData(List<?> data, String filePath) throws ExportException;

    /**
     * Lấy phần mở rộng của tệp xuất.
     * @return phần mở rộng tệp
     */
    String getFileExtension();
}
