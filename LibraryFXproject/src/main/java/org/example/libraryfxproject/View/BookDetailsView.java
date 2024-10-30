package org.example.libraryfxproject.View;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.libraryfxproject.Model.Book;

public class BookDetailsView {
    private final Stage dialog;
    public BookDetailsView(Book book, Stage parentStage) {
        dialog = new Stage();
        dialog.initOwner(parentStage);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setTitle("Book Details");
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(
                new Label("Title: " + book.getTitle()),
                new Label("Author: " + book.getAuthor()),
                new Label("Genre: " + book.getSubject()),
                new Label("Status: " + book.getBookType()),
                new Label("Quantity: " + book.getQuantity())
        );
        Scene scene = new Scene(vbox, 300, 200);
        dialog.setScene(scene);
    }
    public void show() {
        dialog.showAndWait();
    }
}
