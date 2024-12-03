package org.example.libraryfxproject.Controller;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.example.libraryfxproject.Dao.CartDAO;
import org.example.libraryfxproject.Model.Book;
import org.example.libraryfxproject.Service.BookService;
import org.example.libraryfxproject.Util.AlertDisplayer;
import org.example.libraryfxproject.View.BookDetailsView;

public class ContextMenuController extends BaseController {
    private final TableView<Book> catalogTableView;
    private final CartDAO cartDAO;
    private final BookService bookService;
    public ContextMenuController(TableView<Book> catalogTableView, AlertDisplayer alertDisplayer) {
        super(alertDisplayer);
        this.catalogTableView = catalogTableView;
        this.cartDAO = new CartDAO();
        this.bookService = BookService.getInstance();
        setupContextMenu();
    }

    private void setupContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event -> handleDeleteAction());

        MenuItem detailsItem = new MenuItem("Book Details");
        detailsItem.setOnAction(event -> handleDetailsAction());
        contextMenu.getItems().addAll(deleteItem, detailsItem);

        catalogTableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                Book selectedBook = catalogTableView.getSelectionModel().getSelectedItem();
                if (selectedBook != null) {
                    contextMenu.show(catalogTableView, event.getScreenX(), event.getScreenY());
                } else {
                    contextMenu.hide();
                }
            }
        });
    }

    private void handleDeleteAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("This action cannot be undone.");
        alert.showAndWait().ifPresent(response1 -> {
            if (response1 == ButtonType.OK) {
                showConfirmation("Are you sure you want to delete this record?");
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        Book selectedBook = catalogTableView.getSelectionModel().getSelectedItem();
                        if (selectedBook != null) {
                            bookService.deleteBookFromDatabase(selectedBook.getTitle());
                            catalogTableView.getItems().remove(selectedBook);
                        }
                    }
                });
            }
        });
    }

    private void handleDetailsAction() {
        Book selectedBook = catalogTableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            new BookDetailsView(selectedBook);
        }
    }

}
