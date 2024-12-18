package org.example.javaapplicationproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
        try {
            System.out.println("Hãy nhập ID của sách:");
            int no = Integer.parseInt(br.readLine());

            System.out.println("Hãy nhập tiêu đề của sách:");
            String title = br.readLine();

            System.out.println("Hãy nhập tên tác giả:");
            String author = br.readLine();

            System.out.println("Hãy nhập ngày xuất bản:");
            String pubdate = br.readLine();

            System.out.println("Hãy nhập ngày phát hành:");
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

            Book book = new Book(no, title, author, pubdate, releaseDate, isbn, price, subject, category, url, bookType, quantity);
            BookManagement.addBook(book);

            System.out.println("Thêm sách thành công!");

        } catch (NumberFormatException e) {
            System.err.println("Lỗi: ID, giá hoặc số lượng phải là số hợp lệ. Vui lòng thử lại.");
        } catch (IllegalArgumentException e) {
            System.err.println("Lỗi: Giá trị đầu vào không hợp lệ. " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Lỗi không xác định: " + e.getMessage());
        }
    }


    public void modifyBook() throws IOException {
        System.out.println("Hãy nhập ISBN của sách:");
        String ISBN = br.readLine();

        System.out.println("Hãy nhập thuộc tính bạn muốn sửa:");
        String attribute = br.readLine();

        System.out.println("Hãy nhập thông tin cho thuộc tính đó:");
        String newValue = br.readLine();

        BookManagement.modifyBookAttribute(ISBN, attribute, newValue);
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
            int ops;
            try {
                ops = Integer.parseInt(br.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
                ops = -5;
            }
            switch (ops) {
                case 1: {
                    System.out.println("Enter the name of the book you want to find:");
                    String bookName = br.readLine();
                    BookManagement.findBookByTitleInMemory(bookName);
                    break;
                }
                case 2: {
                    System.out.println("Enter the category you want to find:");
                    String bookType = br.readLine();
                    BookManagement.findBookByCategory(bookType);
                    break;
                }
                case 3: {
                    System.out.println("Enter the author you want to find:");
                    String bookAuthor = br.readLine();
                    BookManagement.findBookByAuthor(bookAuthor);
                    break;
                }
                case 4: {
                    System.out.println("Enter the component you remember:");
                    String component = br.readLine();
                    BookManagement.findBookByComponentOfName(component);
                    break;
                }
                case 5: {
                    return false; // Thoát tìm kiếm
                }
                default: {
                    System.out.println("Invalid request. Please re-enter your request.");
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while finding the book: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
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

        // Truy vấn thông tin sách
        String countQuery = "SELECT COUNT(*) AS totalBooks FROM cart WHERE Cart_ID = ?";
        String query = "SELECT c.Cart_ID, c.startDate, c.endDate, c.ISBN, b.title " +
                "FROM cart c " +
                "JOIN books b ON c.ISBN = b.ISBN " +
                "WHERE c.Cart_ID = ?";

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
                        rs.getString("title"),
                        rs.getString("ISBN")
                );
                cart.printInfo(); // In ra thông tin
                // Kiểm tra trạng thái của từng sách
                LocalDate eventDate = LocalDate.parse(cart.getEndDate());
                LocalDate currentDate = LocalDate.now();
                String status = currentDate.isBefore(eventDate)
                        ? "\033[32mCòn hạn\033[0m"
                        : "\033[31mQuá hạn\033[0m";

                System.out.printf("Trạng thái: %s\n", status);
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
            System.out.println(BookManagement.getBooksStatus(book_title.trim()));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    public static boolean borrowBook(BookManagement bookManagement, CartManagement cartManagement, String username) {
        try {
            System.out.println("Nhập tên sách bạn muốn mượn: ");
            String bookTitle = br.readLine();
            String isbn = bookManagement.fetchISBNFromBooks(bookTitle);
            int currentQuantity = bookManagement.fetchQuantityFromBooks(bookTitle);
            int cart_id = AccountManagement.fetchCartIdByUsername(username);
            String isbn2 = cartManagement.fetchISBNFromCart(bookTitle, cart_id);

            if (isbn2 != null) {
                System.out.println("Bạn đã mượn sách này rồi!");
                return true;
            } else if (currentQuantity <= 0 && isbn != null) {
                System.out.println("Sách này hiện trong kho đã hết, vui lòng thực hiện lại.");
                return true;
            } else if (isbn != null) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String startDate = dateFormat.format(calendar.getTime());
                String endDate = null;

                boolean validChoice = false;
                while (!validChoice) {
                    Menu.showBorrowingPeriod();
                    try {
                        int choice = Integer.parseInt(br.readLine());
                        switch (choice) {
                            case 1:
                                calendar.add(Calendar.WEEK_OF_YEAR, 1);
                                validChoice = true;
                                break;
                            case 2:
                                calendar.add(Calendar.WEEK_OF_YEAR, 2);
                                validChoice = true;
                                break;
                            case 3:
                                calendar.add(Calendar.MONTH, 1);
                                validChoice = true;
                                break;
                            default:
                                System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Vui lòng nhập một số hợp lệ.");
                    }
                }

                endDate = dateFormat.format(calendar.getTime());
                System.out.println("Ngày bắt đầu: " + startDate);
                System.out.println("Ngày kết thúc: " + endDate);

                // Thực hiện cập nhật chỉ khi lựa chọn hợp lệ
                BookManagement.updateQuantity(username, bookTitle, "BORROW");
                UserManagement.updateBorrowedBooks(username, 1);

                if (cart_id != -1) {
                    Cart cart = new Cart(cart_id, startDate, endDate, bookTitle, isbn);
                    cartManagement.addCart(cart);
                } else {
                    System.out.println("Không tìm thấy Cart_ID cho username: " + username);
                }
            } else {
                System.out.println("Không tìm thấy cuốn sách '" + bookTitle + "' trong cơ sở dữ liệu. Vui lòng nhập lại: ");
            }
        } catch (IOException e) {
            System.out.println("Đã xảy ra lỗi trong quá trình mượn sách: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Lỗi không xác định: " + e.getMessage());
            return false;
        }
        return true;
    }



    public static boolean returnBook(BookManagement bookManagement, CartManagement cartManagement, String username) {
        try {
            System.out.println("Nhập tên sách bạn muốn hủy mượn: ");
            String bookTitle = br.readLine();

            // Tìm ISBN của sách trong thư viện
            String isbn1 = bookManagement.fetchISBNFromBooks(bookTitle);
            int cart_id = AccountManagement.fetchCartIdByUsername(username);

            // Tìm ISBN của sách trong giỏ hàng của người dùng
            String isbn2 = cartManagement.fetchISBNFromCart(bookTitle, cart_id);

            if (isbn1 == null) {
                System.out.println("Không tìm thấy sách trong thư viện!");
            } else if (isbn2 == null) {
                System.out.println("Không tìm thấy sách trong giỏ hàng hiện tại!");
            } else {
                // Cập nhật số lượng sách và trạng thái
                BookManagement.updateQuantity(username, bookTitle, "RETURN");
                UserManagement.updateBorrowedBooks(username, -1);
                cartManagement.deleteCart(isbn2, cart_id);
                System.out.println("Hủy mượn sách thành công!");
            }
        } catch (IOException e) {
            System.out.println("Lỗi nhập/xuất khi hủy mượn sách: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Đã xảy ra lỗi không xác định: " + e.getMessage());
            return false;
        }
        return true;
    }

}
