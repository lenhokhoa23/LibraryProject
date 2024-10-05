package org.example.javaapplicationproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Controller {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public Controller() {

    }

    public static int login() {
        try {
            System.out.print("Nhập tài khoản: ");
            String username = br.readLine();
            System.out.print("Nhập mật khẩu: ");
            String password = br.readLine();
            System.out.println();
            String sql = "SELECT * FROM accounts WHERE username = ? AND password = ?";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, password);

                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String role = resultSet.getString("role");
                    System.out.println("Đăng nhập thành công với vai trò " + role);
                    if (role.equals("admin")) {
                        return 1;
                    } else {
                        return 0;
                    }
                } else {
                    System.out.println("Sai tên đăng nhập hoặc mật khẩu!");
                }
            } catch (SQLException e) {
                System.out.println("Lỗi khi đăng nhập!");
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void addBook() {
        Book book = new Book(3335, "The Great Gatsby", "F. Scott Fitzgerald",
                "31-Jan-81", "1925-04-10",
                "9780743273565", "10.99",
                "Classic", "Fiction",
                "http://example.com/gatsby",
                "Novel", "5");
        BookManagement.addBook(book);
    }

    public void removeBook() {
        try {
            System.out.println("Enter the name of the book you want to remove:");
            String bookName;
            bookName = br.readLine();
            BookManagement.deleteBook(bookName);

        } catch (IOException e) {
            System.out.println("Không tồn tại cuốn sách này!");
        }
    }

    public void findBook() {
        try {
            System.out.println("Enter the name of the book you want to find:");
            String bookName;
            bookName = br.readLine();
            BookManagement.findBookByTitleInMemory(bookName);

        } catch (IOException e) {
            System.out.println("An error occurred while finding the book name: " + e.getMessage());
        }
    }

    public void removeUser() {
        try {
            System.out.println("Enter the username that you want to remove:");
            String usernameRemove = br.readLine();
            System.out.println("Are you sure you want to delete this account?, Type 'yes' or 'no'");
            String userDecision = br.readLine();
            if (userDecision.equals("yes")) {
                AccountManagement.deleteAccount(usernameRemove);
            }
        } catch (IOException e) {
            System.out.println();
        }
    }

    public void findUser() {
        try {
            System.out.println("Enter the username that you want to find:");
            String username = br.readLine();
            AccountManagement.findUser(username);
        } catch (IOException e) {
            System.out.println();
        }
    }

}