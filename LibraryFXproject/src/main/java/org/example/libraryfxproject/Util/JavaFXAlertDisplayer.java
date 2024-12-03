package org.example.libraryfxproject.Util;

import javafx.scene.control.Alert;

public class JavaFXAlertDisplayer implements AlertDisplayer {
    private static JavaFXAlertDisplayer javaFXAlertDisplayer;
    private JavaFXAlertDisplayer () {

    }
    public static synchronized JavaFXAlertDisplayer getInstance() {
        if (javaFXAlertDisplayer == null) {
            javaFXAlertDisplayer = new JavaFXAlertDisplayer();
        }
        return javaFXAlertDisplayer;
    }
    @Override
    public void showInformationAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public void showConfirmationAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
