package org.example.libraryfxproject.Controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.example.libraryfxproject.Util.AlertDisplayer;

/**
 * Lớp cơ sở cho các controller trong ứng dụng, cung cấp các phương thức tiện ích để hiển thị thông báo thành công, lỗi và xác nhận.
 * Các lớp kế thừa có thể sử dụng các phương thức này để hiển thị các thông báo cho người dùng.
 */
public abstract class BaseController {
    protected final AlertDisplayer alertDisplayer;

    /**
     * Khởi tạo lớp BaseController với đối tượng AlertDisplayer.
     *
     * @param alertDisplayer đối tượng dùng để hiển thị thông báo.
     */
    public BaseController(AlertDisplayer alertDisplayer) {
        this.alertDisplayer = alertDisplayer;
    }

    /**
     * Hiển thị thông báo thành công.
     *
     * @param message thông điệp thành công cần hiển thị.
     */
    protected void showSuccessMessage(String message) {
        alertDisplayer.showInformationAlert("Success", message);
    }

    /**
     * Hiển thị thông báo lỗi.
     *
     * @param message thông điệp lỗi cần hiển thị.
     */
    protected void showErrorMessage(String message) {
        alertDisplayer.showErrorAlert("Error", message);
    }

    /**
     * Hiển thị một thông báo xác nhận với người dùng và trả về kết quả lựa chọn của người dùng.
     *
     * @param message thông điệp cần hiển thị trong thông báo xác nhận.
     * @return true nếu người dùng chọn "OK", false nếu chọn "Cancel".
     */
    protected boolean showConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
}
