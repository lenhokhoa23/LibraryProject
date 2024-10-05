package org.example.javaapplicationproject;

public class User extends Person {
    private int UserID;
    private int borrowedBooks;
    private String membershipType;

    public User(String username, String name, String email, String phoneNumber, int id,
                int borrowedBooks, String membershipType) {
        super(username, name, email, phoneNumber);
        this.UserID = id;
        this.borrowedBooks = borrowedBooks;
        this.membershipType = membershipType;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
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

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("User ID " + UserID);
        System.out.println("Membership type: " + membershipType);
        System.out.println("Number of borrowed books: " + borrowedBooks);
    }
}
