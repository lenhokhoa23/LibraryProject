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
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome to My Application");
        BookManagement.loadBooksIntoMemory();
        AccountManagement.loadUserIntoMemory();
        try {
            boolean isUsing = true;
            while (isUsing) {
                Menu.showLoginMenu();
                String userAction = br.readLine();
                int num;
                try {
                    num = Integer.parseInt(userAction);
                } catch (NumberFormatException e) {
                    num = -5;
                }
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
                                int ops;
                                try {
                                    ops = Integer.parseInt(adminAct);
                                } catch (NumberFormatException e) {
                                    ops = -5;
                                }
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
                                        controller.modifyBook();
                                        break;
                                    }
                                    case 4: {
                                        boolean finding = true;
                                        while (finding) {
                                            finding = controller.findBook();
                                        }
                                        break;
                                    }
                                    case 5: {
                                        controller.removeUser();
                                        break;
                                    }
                                    case 6: {
                                        controller.findUser();
                                        break;
                                    }
                                    case 7: {
                                        controller.checkCartUser(username.trim(), "admin");
                                        break;
                                    }
                                    case 8: {
                                        controller.CheckBookStatus();
                                        break;
                                    }
                                    case 9: {
                                        isUsing = false;
                                        isAdminUsing = false;
                                        System.out.println("Goodbye...");
                                        break;
                                    }
                                    default: {
                                        System.out.println("Invalid request. Please re-enter your request.");
                                        break;
                                    }
                                }
                            }
                        } else if (flag == 0) {
                            boolean isUserUsing = true;
                            while (isUserUsing) {
                                Menu.showUserMenu();
                                String userAct = br.readLine();
                                int ops;
                                try {
                                   ops = Integer.parseInt(userAct);
                                } catch (NumberFormatException e) {
                                    ops = - 5;
                                }
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
                                    default: {
                                        System.out.println("Invalid request. Please re-enter your request.");
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
                    default: {
                        System.out.println("Invalid request. Please re-enter your request.");
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Lỗi trong quá trình nhập xuất: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Lỗi không xác định: " + e.getMessage());
        }
    }
}
