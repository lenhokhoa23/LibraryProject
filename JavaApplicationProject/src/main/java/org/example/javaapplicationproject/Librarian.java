package org.example.javaapplicationproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Librarian extends Person {
    private int LibrarianID;
    private String workShift;
    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    public Librarian(Account account, String name, String email, String phoneNumber, int id) {
        super(account, name, email, phoneNumber);
        this.LibrarianID = id;
    }

    public int getLibrarianID() {
        return this.LibrarianID;
    }

    public void setLibrarianID(int id) {
        this.LibrarianID = id;
    }

    public String getWorkShift() {
        return workShift;
    }

    public void setWorkShift(String workShift) {
        this.workShift = workShift;
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("Employee ID " + LibrarianID + "works in the " + workShift);
    }

    public void removeBook() {
        try {
            System.out.println("Enter the name of the book you want to remove:");
            String bookName;
            synchronized(br) {
                bookName = br.readLine();
            }
            BookManagement.deleteBook(bookName);

        } catch (IOException e) {
            System.out.println("An error occurred while reading the book name: " + e.getMessage());
        }
    }

}
