package org.example.libraryfxproject.Service;
import org.example.libraryfxproject.Dao.BookDAO;
import org.example.libraryfxproject.Model.Book;
import org.example.libraryfxproject.Model.Comment;
import org.example.libraryfxproject.Util.ValidationUtils;

import java.util.List;


public class BookService {
    private final BookDAO bookDAO = new BookDAO();
    private static BookService bookService;

    public static synchronized BookService getInstance() {
        if (bookService == null) {
            bookService = new BookService();
        }
        return bookService;
    }

    public BookDAO getBookDAO() {
        return bookDAO;
    }

    private BookService() {
        LoadService.loadData(bookDAO);
    }

    public int validateAddBookInput(String title, String author, String pubdateStr, String releaseDateStr,
                                    String ISBN, String price, String subject, String category, String URL,
                                    String bookType, String quantity) {

        // Kiểm tra ISBN
        if (!ValidationUtils.isValidISBN(ISBN)) {
            return 1; // Invalid ISBN
        }

        // Kiểm tra tiêu đề sách
        if (!ValidationUtils.isValidTitle(title)) {
            return 2; // Invalid title
        }

        // Kiểm tra giá
        try {
            Double.parseDouble(price);
        } catch (NumberFormatException e) {
            return 3; // Invalid price
        }

        // Kiểm tra thể loại
        if (!ValidationUtils.isValidSubject(subject)) {
            return 4; // Invalid subject
        }

        // Kiểm tra danh mục
        if (!ValidationUtils.isValidCategory(category)) {
            return 5; // Invalid category
        }

        // Kiểm tra URL
        if (!ValidationUtils.isValidURL(URL)) {
            return 6; // Invalid URL
        }

        // Kiểm tra loại sách
        if (!ValidationUtils.isValidTitle(bookType)) {
            return 7; // Invalid bookType
        }

        // Kiểm tra số lượng
        try {
            Integer.parseInt(quantity);
        } catch (NumberFormatException e) {
            return 8; // Invalid quantity
        }

        if (!ValidationUtils.isValidAuthor(author)) {
            return 9; // Invalid author
        }

        return 0; // Tất cả hợp lệ
    }

    public void insertBookToDatabase(String title, String author, String pubdateStr, String releaseDateStr,
                                     String ISBN, String price, String subject, String category, String URL,
                                     String bookType, String quantity) {
        bookDAO.insertBookToDatabase(title, author, pubdateStr, releaseDateStr,
                ISBN, price, subject, category, URL,
                bookType, quantity);
    }

    public void deleteBookFromDatabase(String title) {
        bookDAO.deleteBookFromDatabase(title);
    }

    public void modifyBook(String ISBN, String attribute, String newValue) {
        BookDAO.modifyBookAttribute(ISBN, attribute, newValue);
    }

    public boolean hasEnoughQuantity(String ISBN) {
        int quantity = bookDAO.fetchQuantityFromBooks(ISBN, 3);
        return quantity > 0;
    }

    public Book getBookByTitle(String title) {
        return bookDAO.getBookByBookname(title);
    }

    public boolean hasBookWithISBN(String ISBN) {
        Book book = bookDAO.findBookByDistinctAttribute(ISBN, 3);
        return book != null;
    }

    public String fetchISBNByTitle(String title) {
        return bookDAO.fetchISBNFromBooks(title, 2);
    }

    public void addComment(Book book, Comment comment) throws RuntimeException {
        bookDAO.addNewComment(book, comment);
    }

    public List<Comment> getAllComment(Book book) {
        String ISBN = book.getISBN();
        return bookDAO.getBookComment(ISBN);
    }
}
