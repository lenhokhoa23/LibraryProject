package org.example.libraryfxproject.Controller;

import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.control.TableView;
import org.example.libraryfxproject.Dao.CartDAO;
import org.example.libraryfxproject.Model.Book;
import org.example.libraryfxproject.Service.BookService;
import org.example.libraryfxproject.Util.AlertDisplayer;
import org.example.libraryfxproject.View.BookDetailsView;

/**
 * Controller xử lý menu ngữ cảnh cho bảng sách, cho phép người dùng thực hiện các hành động như
 * xem chi tiết sách và xóa sách.
 */
public class ContextMenuController extends BaseController {
    private final TableView<Book> catalogTableView;
    private final CartDAO cartDAO;
    private final BookService bookService;

    /**
     * Khởi tạo ContextMenuController với các đối tượng cần thiết.
     *
     * @param catalogTableView bảng sách chứa các sách cần thực hiện các hành động.
     * @param alertDisplayer đối tượng hiển thị thông báo cho người dùng.
     * @param username tên người dùng hiện tại.
     */
    public ContextMenuController(TableView<Book> catalogTableView, AlertDisplayer alertDisplayer, String username) {
        super(alertDisplayer);
        this.catalogTableView = catalogTableView;
        this.cartDAO = CartDAO.getInstance();
        this.bookService = BookService.getInstance();
        setupContextMenu(username);
    }

    /**
     * Thiết lập menu ngữ cảnh với các mục như xóa sách và xem chi tiết sách.
     *
     * @param username tên người dùng hiện tại.
     */
    private void setupContextMenu(String username) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event -> handleDeleteAction());

        MenuItem detailsItem = new MenuItem("Book Details");
        detailsItem.setOnAction(event -> handleDetailsAction(username));
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

    /**
     * Xử lý hành động xóa sách khỏi danh sách và cơ sở dữ liệu.
     */
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

    /**
     * Xử lý hành động mở chi tiết sách cho sách được chọn.
     *
     * @param username tên người dùng hiện tại.
     */
    private void handleDetailsAction(String username) {
        Book selectedBook = catalogTableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            new BookDetailsView(selectedBook, username);
        }
    }
}
