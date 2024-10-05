package org.example.javaapplicationproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        BookManagement bookManagement = new BookManagement();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome to My Application");
        bookManagement.loadBooksIntoMemory();
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

                        int flag = Controller.login();

                        if (flag == 1) {
                            Librarian librarian = new Librarian();
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
                                    controller.findBook();
                                    break;
                                }
                                case 4: {
                                    controller.removeUser();
                                    break;
                                }
                                case 5: {
                                    isUsing = false;
                                    System.out.println("Goodbye...");
                                    break;
                                }
                            }
                        } else if (flag == 0) {
                            String userAct = br.readLine();
                            int ops = Integer.parseInt(userAct);
                            switch (ops) {
                                case 1: {
                                    controller.findBook();
                                }
                                case 2: {
                                    isUsing = false;
                                    System.out.println("Goodbye...");
                                    break;
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
