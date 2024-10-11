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
