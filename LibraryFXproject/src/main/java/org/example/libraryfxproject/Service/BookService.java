package org.example.libraryfxproject.Service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import org.example.libraryfxproject.Dao.BookDAO;
import org.example.libraryfxproject.Model.Account;
import org.example.libraryfxproject.Model.Book;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        LoadService loadService = LoadService.getInstance();
        loadService.loadData(bookDAO);
    }

    public int validateAddBookInput(String title, String author, String pubdateStr, String releaseDateStr,
                                    String ISBN, String price, String subject, String category, String URL,
                                    String bookType, String quantity) {
        String isbnPattern = "^\\d{13}$";
        if (!ISBN.matches(isbnPattern)) {
            return 1;
        }

        String titlePattern = "^[A-Za-z0-9 ]+$";
        if (!title.matches(titlePattern)) {
            return 2;
        }

        try {
            Integer.parseInt(price);
        } catch (NumberFormatException e) {
            return 3; // Invalid price
        }

        String subjectPattern = "^[A-Za-z ]+$";
        if (!subject.matches(subjectPattern)) {
            return 4;
        }

        String categoryPattern = "^[A-Za-z ]+$";
        if (!category.matches(categoryPattern)) {
            return 5;
        }

        String urlPattern = "^(http:\\/\\/|https:\\/\\/)([\\w\\-]+\\.)+[\\w\\-]+(\\/[\\w\\-]*)*$";
        if (!URL.matches(urlPattern)) {
            return 6;
        }

        if (!bookType.matches(titlePattern)) {
            return 7;
        }

        try {
            Integer.parseInt(quantity);
        } catch (NumberFormatException e) {
            return 8;
        }

        String authorPattern = "^[A-Za-z ]+$";
        if (!author.matches(authorPattern)) {
            return 9;
        }

        return 0;
    }

    public void insertBookToDatabase(String title, String author, String pubdateStr, String releaseDateStr,
                                     String ISBN, String price, String subject, String category, String URL,
                                     String bookType, String quantity) {
        bookDAO.insertBookToDatabase(title, author, pubdateStr, releaseDateStr,
                ISBN, price, subject, category, URL,
                bookType, quantity);
    }

    public void modifyBook(String ISBN, String attribute, String newValue) {
        BookDAO.modifyBookAttribute(ISBN, attribute, newValue);
    }

}
