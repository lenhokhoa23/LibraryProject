package org.example.libraryfxproject.Controller;

import javafx.scene.Node;
import javafx.stage.Stage;
import org.example.libraryfxproject.Dao.BookDAO;
import org.example.libraryfxproject.Service.BookService;
import org.example.libraryfxproject.Service.LoginService;
import org.example.libraryfxproject.View.AddBookView;
import org.example.libraryfxproject.View.LoginView;

import org.example.libraryfxproject.Model.Book;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddBookController {
    private final AddBookView addBookView;
    private final BookService bookService = new BookService();
    private final BookDAO bookDAO =new BookDAO();
    public AddBookController(AddBookView addBookView) {
        this.addBookView = addBookView;
    }
    public void registerEvent() {
        addBookView.getAddBookButton().setOnAction(event -> {
            try {
                String title = addBookView.getTitle().getText();
                String author = addBookView.getAuthor().getText();
                // Get dates from DatePicker and format them to "dd-MMM-yy"
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yy");
                // Format pubdate
                LocalDate pubdateLocal = addBookView.getPubdate().getValue();
                String pubdate = pubdateLocal != null ? pubdateLocal.format(formatter) : null;

                // Format releaseDate
                LocalDate releaseDateLocal = addBookView.getReleaseDate().getValue();
                String releaseDate = releaseDateLocal != null ? releaseDateLocal.format(formatter) : null;

                String ISBN = addBookView.getISBN().getText();
                String price = addBookView.getPrice().getText();
                String subject = addBookView.getSubject().getText();
                String category = addBookView.getCategory().getText();
                String URL = addBookView.getURL().getText();
                String bookType = addBookView.getBookType().getText();
                String quantity = addBookView.getQuantity().getText();

                bookDAO.insertBookToDatabase(title, author, pubdate, releaseDate, ISBN, price, subject, category, URL, bookType, quantity);

                // Show success message after saving
                addBookView.showSuccessMessage();
            } catch (Exception e) {
                e.printStackTrace();
                addBookView.showErrorMessFill();
            }
        });
        addBookView.getBackButton().setOnAction(event -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        });
    }
}

