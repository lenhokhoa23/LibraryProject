package org.example.libraryfxproject.Util;

public interface AlertDisplayer {

    // Triển khai các abstract method.
    void showInformationAlert(String title, String content);

    void showErrorAlert(String title, String content);

    void showConfirmationAlert(String title, String content);
}
