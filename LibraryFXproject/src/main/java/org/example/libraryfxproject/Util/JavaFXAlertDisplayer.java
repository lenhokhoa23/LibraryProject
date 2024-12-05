package org.example.libraryfxproject.Util;

import javafx.scene.control.Alert;

/**
 * Lớp này triển khai giao diện `AlertDisplayer` để hiển thị cảnh alert JavaFX.
 * Phương thức để hiển thị các loại cảnh báo, lỗi, xác nhận.
 */
public class JavaFXAlertDisplayer implements AlertDisplayer {

    // Biến lưu trữ thể hiện duy nhất của JavaFXAlertDisplayer
    private static JavaFXAlertDisplayer javaFXAlertDisplayer;

    // Constructor riêng để ngăn chặn việc tạo nhiều thể hiện của lớp này
    private JavaFXAlertDisplayer () {

    }

    /**
     * Lấy thể hiện duy nhất của lớp JavaFXAlertDisplayer.
     *
     * @return Thể hiện duy nhất của JavaFXAlertDisplayer
     */
    public static synchronized JavaFXAlertDisplayer getInstance() {
        if (javaFXAlertDisplayer == null) {
            javaFXAlertDisplayer = new JavaFXAlertDisplayer();
        }
        return javaFXAlertDisplayer;
    }

    /**
     * Hiển thị một hộp thoại thông báo với loại cảnh báo thông tin.
     *
     * @param title Tiêu đề của hộp thoại thông báo
     * @param content Nội dung của hộp thoại thông báo
     */
    @Override
    public void showInformationAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Hiển thị một hộp thoại thông báo với loại cảnh báo lỗi.
     *
     * @param title Tiêu đề của hộp thoại thông báo lỗi
     * @param content Nội dung của hộp thoại thông báo lỗi
     */
    @Override
    public void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Hiển thị một hộp thoại thông báo với loại cảnh báo xác nhận.
     *
     * @param title Tiêu đề của hộp thoại thông báo xác nhận
     * @param content Nội dung của hộp thoại thông báo xác nhận
     */
    @Override
    public void showConfirmationAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
