package org.example.libraryfxproject.Model;

public class Book {
    private int no;          // Mã số sách
    private String title;       // Tên sách
    private String author;      // Tác giả
    private String pubdate;     // Ngày xuất bản
    private String releaseDate; // Ngày phát hành
    private String ISBN;        // Số ISBN
    private String price;       // Giá
    private String subject;     // Chủ đề
    private String category;     // Danh mục sách
    private String URL;         // Đường dẫn URL
    private String bookType;    // Loại sách
    private String quantity;     // Số lượng

    // Constructor
    public Book(int no, String title, String author, String pubdate, String releaseDate,
                String ISBN, String price, String subject, String category,
                String URL, String bookType, String quantity) {
        this.no = no;
        this.title = title;
        this.author = author;
        this.pubdate = pubdate;
        this.releaseDate = releaseDate;
        this.ISBN = ISBN;
        this.price = price;
        this.subject = subject;
        this.category = category;
        this.URL = URL;
        this.bookType = bookType;
        this.quantity = quantity;
    }

    public Book() {

    }


    // Getters and Setters
    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getBookType() {
        return bookType;
    }

    public void setBookType(String bookType) {
        this.bookType = bookType;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
