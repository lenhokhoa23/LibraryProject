package org.example.libraryfxproject.Model;

public class Librarian extends Person{
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
}
