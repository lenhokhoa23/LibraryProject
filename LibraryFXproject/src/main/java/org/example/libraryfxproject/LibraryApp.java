package org.example.libraryfxproject;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.libraryfxproject.View.LoginView;

public class LibraryApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        LoginView loginView = new LoginView(primaryStage);
    }
}
