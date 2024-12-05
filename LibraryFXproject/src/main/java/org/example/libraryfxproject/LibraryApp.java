package org.example.libraryfxproject;

import javafx.application.Application;
import javafx.stage.Stage;

import org.example.libraryfxproject.View.LoginView;
import org.example.libraryfxproject.Controller.LoginController;
import org.example.libraryfxproject.View.MainMenuView;
import org.example.libraryfxproject.View.UserView;

public class LibraryApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
       LoginView loginView = new LoginView(primaryStage);
//       LoginController loginController = new LoginController(loginView);
//       MainMenuView mainMenuView = new MainMenuView(primaryStage);
//       UserView userMenuView = new UserView(primaryStage, "viettran97");
    }

    
}
