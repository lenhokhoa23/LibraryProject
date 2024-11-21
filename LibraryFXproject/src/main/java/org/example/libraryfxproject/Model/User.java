package org.example.libraryfxproject.Model;

public class User extends Person{
    private int Cart_ID;
    private int borrowedBooks;
    private String membershipType;

    public User(String username, String name, String email, String phoneNumber, int id,
                int borrowedBooks, String membershipType) {
        super(username, name, email, phoneNumber);
        this.Cart_ID = id;
        this.borrowedBooks = borrowedBooks;
        this.membershipType = membershipType;
    }

    public int getUserID() {
        return Cart_ID;
    }

    public int getCart_ID() {
        return Cart_ID;
    }

    public int getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(int borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    public void setCart_ID(int Cart_ID) {
        this.Cart_ID = Cart_ID;
    }


    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }


}
