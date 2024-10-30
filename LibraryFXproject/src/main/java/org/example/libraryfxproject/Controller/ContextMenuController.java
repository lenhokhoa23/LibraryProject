package org.example.libraryfxproject.Controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.example.libraryfxproject.Dao.BookDAO;
import org.example.libraryfxproject.Model.Book;
import org.example.libraryfxproject.View.BookDetailsView;

public class ContextMenuController {
    private final BookDAO bookDAO;
    private final TableView<Book> catalogTableView;
    public ContextMenuController(TableView<Book> catalogTableView) {
        this.catalogTableView = catalogTableView;
        this.bookDAO = new BookDAO();
        setupContextMenu();
    }

    private void setupContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event -> handleDeleteAction());

        MenuItem detailsItem = new MenuItem("Details");
        detailsItem.setOnAction(event -> handleDetailsAction());
        contextMenu.getItems().addAll(deleteItem, detailsItem);
        catalogTableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(catalogTableView, event.getScreenX(), event.getScreenY());
            }
        });
    }

    private void handleDeleteAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to delete this record?");
        alert.setContentText("This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Thực hiện hành động xóa ở đây
                Book selectedBook = catalogTableView.getSelectionModel().getSelectedItem();
                if (selectedBook != null) {
                    bookDAO.deleteBookFromDatebase(selectedBook.getTitle());
                    catalogTableView.getItems().remove(selectedBook);
                }
            }
        });
    }

    private void handleDetailsAction() {
        Book selectedBook = catalogTableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            // Hiển thị cửa sổ chi tiết
            Stage parentStage = (Stage) catalogTableView.getScene().getWindow(); // Ép kiểu thành Stage
            new BookDetailsView(selectedBook, parentStage).show();
        }
    }
}
