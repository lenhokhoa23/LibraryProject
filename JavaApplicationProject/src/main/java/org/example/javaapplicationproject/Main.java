package org.example.javaapplicationproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        BookManagement bookManagement = new BookManagement();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome to My Application");
        try {
            boolean isUsing = true;
            while (isUsing) {
                System.out.println("Let's choose your option!!!");
                System.out.println("[0] Login");
                System.out.println("[1] Register");
                System.out.println("[2] Add document");
                System.out.println("[3] Remove document");
                System.out.println("[4] Find document");
                System.out.println("[5] Remove user");
                System.out.println("[6] Exit");
                String userAction = br.readLine();
                int num = Integer.parseInt(userAction);
                switch (num) {
                    case 0: {
                        System.out.println("hello");
                        break;
                    }
                    case 2: {
                        Book book = new Book(3334, "The Great Gatsby", "F. Scott Fitzgerald",
                                "31-Jan-81", "1925-04-10",
                                "9780743273565", "10.99",
                                "Classic", "Fiction",
                                "http://example.com/gatsby",
                                "Novel", "5");
                        BookManagement.addBook(book);
                        break;
                    }
                    case 1: {
                        System.out.println("Enter username: ");
                        String username = br.readLine();
                        System.out.println("Enter password: ");
                        String password = br.readLine();

                        Account account = new Account(username, password);
                        AccountManagement.addAccount(account);
                        break;
                    }
                    case 3: {
                        System.out.println("Enter the name of book you want to remove:");
                        String bookName = br.readLine();
                        BookManagement.deleteBook(bookName);
                        break;
                    }
                    case 5: {
                        System.out.println("Enter the username that you want to remove:");
                        String username = br.readLine();
                        System.out.println("Are you sure you want to delete this account?, Type 'yes' or 'no'");
                        String userDecision = br.readLine();
                        if (userDecision.equals("yes")) {
                            AccountManagement.deleteAccount(username);
                        }
                        break;
                    }
                    case 6: {
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
