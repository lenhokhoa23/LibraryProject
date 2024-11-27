package org.example.libraryfxproject.View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import org.example.libraryfxproject.Dao.CartDAO;
import org.example.libraryfxproject.Model.Book;
import org.example.libraryfxproject.Model.Cart;
import org.example.libraryfxproject.Service.CartService;

import java.io.IOException;
import java.util.List;

public class BookDetailsView {
    private final Stage dialog;
    private final CartService cartService;

    public BookDetailsView(Book book, Stage parentStage) {
        cartService = CartService.getInstance();
        dialog = new Stage();
        dialog.initOwner(parentStage);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/libraryfxproject/BookDetailsView.fxml"));
            Parent root = loader.load();

            // Tìm danh sách các Label trong FXML
            VBox statusContainer = (VBox) root.lookup("#statusContainer"); // VBox chứa trạng thái sách

            // Lấy danh sách trạng thái sách từ CartDAO
            List<Cart> cartList = CartDAO.getBooksStatus(book.getTitle());

            // Thêm trạng thái sách vào VBox
            if (cartList.isEmpty()) {
                Label noBookLabel = new Label("Không có sách nào được mượn.");
                statusContainer.getChildren().add(noBookLabel);
            } else {
                for (Cart cart : cartList) {
                    // Sử dụng hàm CartService để xác định trạng thái sách
                    String status = cartService.getBookStatus(cart.getEndDate());

                    // Tạo Label cho từng Cart
                    Label statusLabel = new Label(
                            "Cart ID: " + cart.getCart_ID() +
                                    "\nStatus: " + status +  // Hiển thị trạng thái "Còn hạn" hoặc "Quá hạn"
                                    "\nDue Date: " + cart.getEndDate()
                                    
                    );
                    statusContainer.getChildren().add(statusLabel);
                }
            }

            Scene scene = new Scene(root);
            dialog.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void show() {
        dialog.showAndWait();
    }
}
