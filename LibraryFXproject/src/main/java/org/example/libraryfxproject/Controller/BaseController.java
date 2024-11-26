package org.example.libraryfxproject.Controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.example.libraryfxproject.Util.AlertDisplayer;

public abstract class BaseController {
    protected final AlertDisplayer alertDisplayer;

    public BaseController(AlertDisplayer alertDisplayer) {
        this.alertDisplayer = alertDisplayer;
    }

    protected void showSuccessMessage(String message) {
        alertDisplayer.showInformationAlert("Success", message);
    }

    protected void showErrorMessage(String message) {
        alertDisplayer.showErrorAlert("Error", message);
    }

    protected boolean showConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
}
