package org.example.javaapplicationproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Main {
    public static void main(String[] args) throws IOException {
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
                System.out.println("Let's choose your option!!!");
                System.out.println("[0] Login");
                System.out.println("[1] Register");
                System.out.println("[2] Exit");
                String userAction = br.readLine();
                int num = Integer.parseInt(userAction);
                switch (num) {
                    case 0: {
                        StringBuilder usernameBuilder = new StringBuilder();
                        StringBuilder passwordBuilder = new StringBuilder();
                        int flag = Controller.login(usernameBuilder, passwordBuilder);
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
                                        Controller.checkCartUser(username, "admin");
                                        break;
                                    }
                                    case 7: {
                                        Controller.CheckBookStatus();
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
                                            found = Controller.borrowBook(bookManagement, cartManagement, username);
                                        }
                                        break;
                                    }
                                    case 4: {
                                        boolean found = false;
                                        while (!found) {
                                            System.out.println("Nhập tên sách bạn muốn hủy mượn: ");
                                            String bookTitle = br.readLine();
                                            String isbn1 = bookManagement.fetchISBNFromBooks(bookTitle);
                                            int cart_id = AccountManagement.fetchCartIdByUsername(username);
                                            String isbn2 = cartManagement.fetchISBNFromCart(bookTitle, cart_id);
                                            if (isbn1 == null) {
                                                System.out.println("Không tìm thấy sách trong thư viện!");
                                            } else if (isbn2 == null) {
                                                System.out.println("Không tìm thấy sách trong giỏ hàng hiện tại!");
                                            } else {
                                                found = true;
                                                BookManagement.updateQuantity(bookTitle, "RETURN");
                                                cartManagement.deleteCart(isbn2, cart_id);
                                                System.out.println("Hủy mượn sách thành công!");
                                            }
                                        }
                                        break;
                                    }
                                    case 5: {
                                        Controller.checkCartUser(username, "user");
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
                        System.out.println("Enter username: ");
                        String username = br.readLine();
                        System.out.println("Enter password: ");
                        String password = br.readLine();

                        Account account = new Account(username, password, "user");
                        AccountManagement.addAccount(account);
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
