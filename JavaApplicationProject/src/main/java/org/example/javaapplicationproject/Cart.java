package org.example.javaapplicationproject;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<Book> books;

    public Cart() {
        this.books = new ArrayList<>();
    }

    // Thêm sách vào giỏ
    public void addBook(Book book) {
        books.add(book);
        System.out.println("Đã thêm sách \"" + book.getTitle() + "\" vào giỏ.");
    }

    // Hiển thị danh sách sách trong giỏ
    public void showCart() {
        if (books.isEmpty()) {
            System.out.println("Giỏ sách đang trống.");
            return;
        }

        System.out.println("Sách trong giỏ:");
        for (Book book : books) {
            System.out.println("- " + book.getTitle());
        }
    }

    // Xóa sách khỏi giỏ
    public void removeBook(Book book) {
        if (books.remove(book)) {
            System.out.println("Đã xóa sách \"" + book.getTitle() + "\" khỏi giỏ.");
        } else {
            System.out.println("Sách không có trong giỏ.");
        }
    }

    // Lấy danh sách sách trong giỏ
    public List<Book> getBooks() {
        return books;
    }
}

