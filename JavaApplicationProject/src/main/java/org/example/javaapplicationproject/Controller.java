package org.example.javaapplicationproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.example.javaapplicationproject.CartManagement.fetchCartByCartID;
import static org.example.javaapplicationproject.CartManagement.fetchCartIdByUsername;

public class Controller {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public Controller() {

    }

    public static int login(StringBuilder username, StringBuilder password) {
        try {
            System.out.print("Nhập tài khoản: ");
            String userN = br.readLine();
            username.append(userN);
            System.out.print("Nhập mật khẩu: ");
            String passwordN = br.readLine();
            password.append(passwordN);
            System.out.println();
            String sql = "SELECT * FROM accounts WHERE username = ? AND password = ?";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username.toString());
                statement.setString(2, password.toString());

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

    public boolean findBook() {
        try {
            Menu.showFindMenu();
            int ops = Integer.parseInt(br.readLine());
            switch (ops) {
                case 1: {
                    System.out.println("Enter the name of the book you want to find:");
                    String bookName;
                    bookName = br.readLine();
                    BookManagement.findBookByTitleInMemory(bookName);
                    break;
                }
                case 2: {
                    System.out.println("Enter the category you want to find:");
                    String bookType;
                    bookType = br.readLine();
                    BookManagement.findBookByCategory(bookType);
                    break;
                }
                case 3: {
                    System.out.println("Enter the author you want to find:");
                    String bookAuthor;
                    bookAuthor = br.readLine();
                    BookManagement.findBookByAuthor(bookAuthor);
                    break;
                }
                case 4: {
                    System.out.println("Enter the component you remember:");
                    String component;
                    component = br.readLine();
                    BookManagement.findBookByComponentOfName(component);
                    break;
                }
                case 5: {
                    return false;
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while finding the book name: " + e.getMessage());
        }
        return true;
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
    public static void checkCart(String username, String role, BufferedReader br) {
        int cartId = 0;
        if (role.equals("user")) {
            cartId = fetchCartIdByUsername(username);
        } else if (role.equals("admin")) {
            System.out.println("Nhập tên người dùng bạn muốn xem: ");
            try {
                username = br.readLine(); // Admin nhập tên người dùng
                Connection conn = DatabaseConnection.getConnection();
                try {
                    String query = "SELECT borrowedBooks FROM cart WHERE Cart_ID = ?";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setInt(1, cartId); // Lấy số sách mượn từ cart có ID tương ứng.
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        System.out.println(rs.getInt("borrowedBooks"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                cartId = fetchCartIdByUsername(username);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        Cart cart = fetchCartByCartID(cartId);
        if (cart != null) {
            cart.printInfo(); // In ra giá sách người dùng
        } else {
            System.out.println("Giá sách cá nhân của người dùng \"" + username + "\"trống.");
        }
        System.out.println(CartManagement.getBookStatus(cartId));
    }

    public static void CheckBookStatus() {
        try {
            System.out.println("Nhập tên sách bạn muốn kiêm tra: ");
            String book_title = br.readLine(); // Admin nhập tên sách
            System.out.println(BookManagement.getBooksStatus(book_title));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}
