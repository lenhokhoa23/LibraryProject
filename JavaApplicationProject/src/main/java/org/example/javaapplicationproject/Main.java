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
                        System.out.print("Nhập tài khoản: ");
                        String username = br.readLine();
                        System.out.print("Nhập mật khẩu: ");
                        String password = br.readLine();
                        System.out.println();
                        int flag = LoginManagement.login(username, password);
                        if (flag == 1) {
                            String adminAct = br.readLine();
                            int ops = Integer.parseInt(adminAct);
                            switch (ops) {
                                case 1: {
                                    Book book = new Book(3335, "The Great Gatsby", "F. Scott Fitzgerald",
                                            "31-Jan-81", "1925-04-10",
                                            "9780743273565", "10.99",
                                            "Classic", "Fiction",
                                            "http://example.com/gatsby",
                                            "Novel", "5");
                                    BookManagement.addBook(book);
                                    break;
                                }
                                case 2: {
                                    System.out.println("Enter the name of book you want to remove:");
                                    String bookName = br.readLine();
                                    BookManagement.deleteBook(bookName);
                                    break;
                                }
                                case 3: {
                                    System.out.println("Enter the name of book you want to find: ");
                                    String nameBook = br.readLine();
                                    bookManagement.findBookByTitleInMemory(nameBook);
                                    break;
                                }
                                case 4: {
                                    System.out.println("Enter the username that you want to remove:");
                                    String usernameRemove = br.readLine();
                                    System.out.println("Are you sure you want to delete this account?, Type 'yes' or 'no'");
                                    String userDecision = br.readLine();
                                    if (userDecision.equals("yes")) {
                                        AccountManagement.deleteAccount(usernameRemove);
                                    }
                                    break;
                                }
                                case 5: {
                                    isUsing = false;
                                    System.out.println("....");
                                    break;
                                }
                            }
                        } else if (flag == 0) {
                            String userAct = br.readLine();
                            int ops = Integer.parseInt(userAct);
                            switch (ops) {
                                case 1: {
                                    System.out.println("Enter the name of book you want to find: ");
                                    String nameBook = br.readLine();
                                    bookManagement.findBookByTitleInMemory(nameBook);
                                    break;
                                }
                                case 2: {
                                    isUsing = false;
                                    System.out.println("....");
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
                        System.out.println("....");
                        break;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
