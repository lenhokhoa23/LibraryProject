package org.example.javaapplicationproject;

public class Librarian extends Person {
    private int LibrarianID;
    private String workShift;

    public Librarian(String username, String name, String email, String phoneNumber, int id, String workShift) {
        super(username, name, email, phoneNumber);
        this.LibrarianID = id;
        this.workShift = workShift;
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
        System.out.println("Employee ID " + LibrarianID + ", works in the " + workShift);
    }

}
