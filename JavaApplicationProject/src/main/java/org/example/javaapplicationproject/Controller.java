package org.example.javaapplicationproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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

    public void addBook() throws IOException {
        System.out.println("Hãy nhập ID của sách:");
        int no = Integer.parseInt(br.readLine());

        System.out.println("Hãy nhập tiêu đề của sách:");
        String title = br.readLine();

        System.out.println("Hãy nhập tên tác giả:");
        String author = br.readLine();

        System.out.println("Hãy nhập ngày xuất bản :");
        String pubdate = br.readLine();

        System.out.println("Hãy nhập ngày phát hành :");
        String releaseDate = br.readLine();

        System.out.println("Hãy nhập ISBN:");
        String isbn = br.readLine();

        System.out.println("Hãy nhập giá:");
        String price = br.readLine();

        System.out.println("Hãy nhập môn học:");
        String subject = br.readLine();

        System.out.println("Hãy nhập danh mục:");
        String category = br.readLine();

        System.out.println("Hãy nhập URL:");
        String url = br.readLine();

        System.out.println("Hãy nhập loại sách:");
        String bookType = br.readLine();

        System.out.println("Hãy nhập số lượng:");
        String quantity = br.readLine();

        // Sau khi nhập các thông tin, khởi tạo đối tượng Book
        Book book = new Book(no, title, author, pubdate, releaseDate, isbn, price, subject, category, url, bookType, quantity);
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
    public static void checkCartUser(String username, String role) {
        int cartId = 0;
        if (role.equals("user")) {
            cartId = fetchCartIdByUsername(username);
        } else if (role.equals("admin")) {
            System.out.println("Nhập tên người dùng bạn muốn xem: ");
            try {
                username = br.readLine(); // Admin nhập tên người dùng

                String query = "SELECT username FROM accounts WHERE username = ?";
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, username);
                    ResultSet rs = stmt.executeQuery();
                    if (!rs.next()) {
                        System.out.println("Người dùng \"" + username + "\" không tồn tại.");
                        return;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    return;
                }
                cartId = fetchCartIdByUsername(username);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        // Lấy tổng số sách đã mượn và thông tin từng sách
        String countQuery = "SELECT COUNT(*) AS totalBooks FROM cart WHERE Cart_ID = ?";
        String query = "SELECT * FROM cart WHERE Cart_ID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement countStmt = conn.prepareStatement(countQuery);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Lấy tổng số sách đã mượn
            countStmt.setInt(1, cartId);
            ResultSet countRs = countStmt.executeQuery();
            if (countRs.next()) {
                int totalBooks = countRs.getInt("totalBooks");
                System.out.println("Số sách đã mượn: " + totalBooks);
            }
            // Lấy thông tin chi tiết từng sách
            stmt.setInt(1, cartId);
            ResultSet rs = stmt.executeQuery();
            // Kiểm tra nếu giỏ hàng trống
            if (!rs.isBeforeFirst()) {
                System.out.println("Giá sách cá nhân của người dùng \"" + username + "\" trống.");
                return;
            }
            // Duyệt qua tất cả các sách trong giỏ
            System.out.println("Cart ID: " + cartId);
            System.out.println("------------------------------");
            while (rs.next()) {
                Cart cart = new Cart(
                        rs.getInt("Cart_ID"),
                        rs.getString("startDate"),
                        rs.getString("endDate"),
                        rs.getString("ISBN"),
                        rs.getString("title")
                );
                cart.printInfo(); // In ra thông tin
                // Hiển thị trạng thái sách
                System.out.println(CartManagement.getBookStatus(cartId));
                System.out.println("+------------------------+");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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


    public static boolean borrowBook(BookManagement bookManagement, CartManagement cartManagement, String username) throws IOException {
        System.out.println("Nhập tên sách bạn muốn mượn: ");
        String bookTitle = br.readLine();
        String isbn = bookManagement.fetchISBNFromBooks(bookTitle);
        int currentQuantity = bookManagement.fetchQuantityFromBooks(bookTitle);
        int cart_id = AccountManagement.fetchCartIdByUsername(username);
        String isbn2 = cartManagement.fetchISBNFromCart(bookTitle, cart_id);
        if (isbn2 != null) {
            System.out.println("Bạn đã mượn sách này rồi!");
            return true;
        } else if (currentQuantity <= 0) {
            System.out.println("Sách này hiện trong kho đã hết, vui lòng thực hiện lại.");
            return true;
        } else if (isbn != null) {
            BookManagement.updateQuantity(bookTitle, "BORROW");
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String startDate = dateFormat.format(calendar.getTime());
            Menu.showBorrowingPeriod();
            int choice = Integer.parseInt(br.readLine());
            switch (choice) {
                case 1:
                    calendar.add(Calendar.WEEK_OF_YEAR, 1);
                    break;
                case 2:
                    calendar.add(Calendar.WEEK_OF_YEAR, 2);
                    break;
                case 3:
                    calendar.add(Calendar.MONTH, 1);
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
                    return false;
            }
            String endDate = dateFormat.format(calendar.getTime());
            System.out.println("Ngày bắt đầu: " + startDate);
            System.out.println("Ngày kết thúc: " + endDate);
            if (cart_id != -1) {
                Cart cart = new Cart(cart_id, startDate, endDate, bookTitle, isbn);
                cartManagement.addCart(cart);
            } else {
                System.out.println("Không tìm thấy Cart_ID cho username: " + username);
            }
        } else {
            System.out.println("Không tìm thấy cuốn sách '" + bookTitle + "' trong cơ sở dữ liệu. Vui lòng nhập lại: ");
        }
        return true;
    }
    public static boolean returnBook(BookManagement bookManagement, CartManagement cartManagement, String username) throws IOException {
        System.out.println("Nhập tên sách bạn muốn hủy mượn: ");
        String bookTitle = br.readLine();
        String isbn1 = bookManagement.fetchISBNFromBooks(bookTitle);
        int cart_id = AccountManagement.fetchCartIdByUsername(username);
        String isbn2 = cartManagement.fetchISBNFromCart(bookTitle, cart_id);
        if (isbn1 == null) {
            System.out.println("Không tìm thấy sách trong thư viện!");
        }
        else if (isbn2 == null) {
            System.out.println("Không tìm thấy sách trong giỏ hàng hiện tại!");
        }
        else {
            bookManagement.updateQuantity(bookTitle, "RETURN");
            cartManagement.deleteCart(isbn2, cart_id);
            System.out.println("Hủy mượn sách thành công!");

        }
        return true;
    }
}
