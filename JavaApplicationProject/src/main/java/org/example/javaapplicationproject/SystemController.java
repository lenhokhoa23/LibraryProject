package org.example.javaapplicationproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SystemController {
    public void start() {
        BookManagement bookManagement = new BookManagement();
        CartManagement cartManagement = new CartManagement();
        Controller controller = new Controller();
        AccountManagement accountManagement = new AccountManagement();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome to My Application");
        BookManagement.loadBooksIntoMemory();
        AccountManagement.loadUserIntoMemory();
        try {
            boolean isUsing = true;

            while (isUsing) {
                Menu.showLoginMenu();
                String userAction = br.readLine();
                int num = Integer.parseInt(userAction);
                switch (num) {
                    case 0: {
                        StringBuilder usernameBuilder = new StringBuilder();
                        StringBuilder passwordBuilder = new StringBuilder();
                        int flag = controller.login(usernameBuilder, passwordBuilder);
                        String username = usernameBuilder.toString();
                        String password = passwordBuilder.toString();
                        if (flag == 1) {
                            boolean isAdminUsing = true;
                            while (isAdminUsing) {
                                Menu.showAdminMenu();
                                String adminAct = br.readLine();
                                int ops = Integer.parseInt(adminAct);
                                switch (ops) {
                                    case 1: {
                                        controller.addBook();
                                        break;
                                    }
                                    case 2: {
                                        controller.removeBook();
                                        break;
                                    }
                                    case 3: {
                                        boolean finding = true;
                                        while (finding) {
                                            finding = controller.findBook();
                                        }
                                        break;
                                    }
                                    case 4: {
                                        controller.removeUser();
                                        break;
                                    }
                                    case 5 : {
                                        controller.findUser();
                                        break;
                                    }
                                    case 6: {
                                        controller.checkCartUser(username, "admin");
                                        break;
                                    }
                                    case 7: {
                                        controller.CheckBookStatus();
                                        break;
                                    }
                                    case 8: {
                                        isUsing = false;
                                        isAdminUsing = false;
                                        System.out.println("Goodbye...");
                                        break;
                                    }
                                }
                            }
                        } else if (flag == 0) {
                            boolean isUserUsing = true;
                            while (isUserUsing) {
                                Menu.showUserMenu();
                                String userAct = br.readLine();
                                int ops = Integer.parseInt(userAct);
                                switch (ops) {
                                    case 1: {
                                        controller.findBook();
                                        break;
                                    }
                                    case 2: {
                                        controller.findUser();
                                        break;
                                    }
                                    case 3: {
                                        boolean found = false;
                                        while (!found) {
                                            System.out.println("Nhập tên sách bạn muốn mượn: ");
                                            String bookTitle = br.readLine();
                                            String isbn = bookManagement.fetchISBNFromBooks(bookTitle);
                                            int currentQuantity = bookManagement.fetchQuantityFromBooks(bookTitle);
                                            if (currentQuantity <= 0) {
                                                System.out.println("Sách này hiện trong kho đã hết, vui lòng thực hiện lại.");
                                            } else if (isbn != null) {
                                                found = true;
                                                bookManagement.updateQuantity(bookTitle, "BORROW");
                                                Calendar calendar = Calendar.getInstance();
                                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                                String startDate = dateFormat.format(calendar.getTime());
                                                System.out.println("Chọn thời gian mượn sách:");
                                                System.out.println("1. 1 tuần");
                                                System.out.println("2. 2 tuần");
                                                System.out.println("3. 1 tháng");
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
                                                        return;
                                                }
                                                String endDate = dateFormat.format(calendar.getTime());
                                                System.out.println("Ngày bắt đầu: " + startDate);
                                                System.out.println("Ngày kết thúc: " + endDate);
                                                int cart_id = accountManagement.fetchCartIdByUsername(username);
                                                if (cart_id != -1) {
                                                    Cart cart = new Cart(cart_id, startDate, endDate, bookTitle, isbn);
                                                    cartManagement.addCart(cart);
                                                } else {
                                                    System.out.println("Không tìm thấy Cart_ID cho username: " + username);
                                                }
                                            } else {
                                                System.out.println("Không tìm thấy cuốn sách '" + bookTitle + "' trong cơ sở dữ liệu. Vui lòng nhập lại: ");
                                            }
                                        }
                                        break;
                                    }
                                    case 4: {
                                        boolean found = false;
                                        while (!found) {
                                            found = Controller.returnBook(bookManagement, cartManagement, username);
                                        }
                                        break;
                                    }
                                    case 5: {
                                        controller.checkCartUser(username, "user");
                                        break;
                                    }
                                    case 6: {
                                        isUsing = false;
                                        isUserUsing = false;
                                        System.out.println("Goodbye...");
                                        break;
                                    }
                                }
                            }
                        }
                        break;
                    }
                    case 1: {
                        AccountManagement.register();
                        break;
                    }
                    case 2: {
                        isUsing = false;
                        System.out.println("Goodbye...");
                        break;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}