package org.example.libraryfxproject.Service;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import org.example.libraryfxproject.Dao.BookDAO;
import org.example.libraryfxproject.Model.Book;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;

public class BookService {
    private final BookDAO bookDAO = new BookDAO();;
    public BookService() {
        LoadService loadService = new LoadService();
        loadService.loadData(bookDAO);
    }
    public HashMap<String, Book> loadData() {
        bookDAO.loadData(); // Gọi phương thức loadData để tải dữ liệu
        return bookDAO.getDataMap(); // Lấy dữ liệu từ HashMap
    }

}
