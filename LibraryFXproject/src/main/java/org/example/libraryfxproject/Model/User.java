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

    public void setCart_ID(int Cart_ID) {
        Cart_ID = Cart_ID;
    }

    public int getBorrowedBook() {
        return borrowedBooks;
    }

    public void setBorrowedBook(int borrowedBook) {
        this.borrowedBooks = borrowedBook;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }
}
