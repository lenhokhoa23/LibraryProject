package org.example.libraryfxproject;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.libraryfxproject.Dao.AccountDAO;
import org.example.libraryfxproject.Dao.BookDAO;
import org.example.libraryfxproject.Model.Account;
import org.example.libraryfxproject.Model.Book;
import org.example.libraryfxproject.Service.LoadService;
import org.example.libraryfxproject.View.LoginView;

import java.util.HashMap;

public class LibraryApp extends Application {

    public static void main(String[] args) {
        launch(args);
        AccountDAO accountDao = new AccountDAO();
        BookDAO bookDao = new BookDAO();

        LoadService loadService = new LoadService();

        //Đa hình!
        loadService.loadData(accountDao);
        loadService.loadData(bookDao);

        System.out.println("Account Data: " + accountDao.getDataMap());
        System.out.println("Book Data: " + bookDao.getDataMap());
    }

    @Override
    public void start(Stage primaryStage) {
        LoginView loginView = new LoginView(primaryStage);
    }
}
