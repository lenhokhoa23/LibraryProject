
package org.example.libraryfxproject.Controller;

import javafx.scene.Node;
import javafx.stage.Stage;
import org.example.libraryfxproject.Dao.BookDAO;
import org.example.libraryfxproject.Service.BookService;
import org.example.libraryfxproject.View.AddBookView;
import org.example.libraryfxproject.Model.Book;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddBookController {
    private final AddBookView addBookView;
    private final BookService bookService = BookService.getInstance();
    private final BookDAO bookDAO = new BookDAO();

    public AddBookController(AddBookView addBookView) {
        this.addBookView = addBookView;
    }

    public void registerEvent() {
        // Handle Add Book button click
        addBookView.getAddBookButton().setOnAction(event -> {
            try {
                // Extract input values
                String title = addBookView.getTitle().getText();
                String author = addBookView.getAuthor().getText();

                // Format pubdate using DatePicker
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yy");
                LocalDate pubdateLocal = addBookView.getPubdate().getValue();
                String pubdate = pubdateLocal != null ? pubdateLocal.format(formatter) : null;

                // Format releaseDate using DatePicker
                LocalDate releaseDateLocal = addBookView.getReleaseDate().getValue();
                String releaseDate = releaseDateLocal != null ? releaseDateLocal.format(formatter) : null;

                // Extract other book details
                String ISBN = addBookView.getISBN().getText();
                String price = addBookView.getPrice().getText();
                String subject = addBookView.getSubject().getText();
                String category = addBookView.getCategory().getText();
                String URL = addBookView.getURL().getText();
                String bookType = addBookView.getBookType().getText();
                String quantity = addBookView.getQuantity().getText();

                // Insert the book into the database
                bookService.insertBookToDatabase(title, author, pubdate, releaseDate, ISBN, price, subject, category, URL, bookType, quantity);

                // Show success message
                addBookView.showSuccessMessage();

            } catch (Exception e) {
                e.printStackTrace();
                // Show error message if an exception occurs
                addBookView.showErrorMessFill();
            }
        });

        // Handle Back button click
        addBookView.getBackButton().setOnAction(event -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        });
    }
}
