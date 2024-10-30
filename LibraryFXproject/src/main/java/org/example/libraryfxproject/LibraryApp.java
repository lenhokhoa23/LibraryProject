package org.example.libraryfxproject;

import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.example.libraryfxproject.Controller.LoginController;
import org.example.libraryfxproject.Controller.MainMenuController;
import org.example.libraryfxproject.Dao.AccountDAO;
import org.example.libraryfxproject.Dao.BookDAO;
import org.example.libraryfxproject.Model.Account;
import org.example.libraryfxproject.Model.Book;
import org.example.libraryfxproject.Service.LoadService;
import org.example.libraryfxproject.View.LoginView;
import org.example.libraryfxproject.View.MainMenuView;
import org.example.libraryfxproject.View.UserView;

import java.util.HashMap;

public class LibraryApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
//        LoginView loginView = new LoginView(primaryStage);
//        LoginController loginController = new LoginController(loginView);
//        MainMenuView mainMenuView = new MainMenuView(primaryStage);
        UserView userMenuView = new UserView(primaryStage);
    }
}
