package org.example.libraryfxproject.View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.libraryfxproject.Controller.AddBookController;
import org.example.libraryfxproject.Controller.LoginController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddBookView implements Initializable {
    @FXML
    private TextField title;
    @FXML
    private TextField author;
    @FXML
    private DatePicker pubdate;
    @FXML
    private DatePicker releaseDate;
    @FXML
    private TextField ISBN;
    @FXML
    private TextField price;
    @FXML
    private TextField subject;
    @FXML
    private TextField category;
    @FXML
    private TextField URL; // For the book URL or link
    @FXML
    private TextField bookType;
    @FXML
    private TextField quantity;
    @FXML
    private Button addBookButton;
    @FXML
    private Button backButton;

    private final Stage stage;

    public AddBookView(Stage stage) {
        this.stage = new Stage();
        this.stage.setTitle("Add Book");
        initializeAddBookView();
    }
    public void initializeAddBookView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/libraryfxproject/addBookView.fxml"));
            loader.setController(this);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            this.stage.setScene(scene);
            this.stage.initModality(Modality.NONE);
            this.stage.initOwner(stage.getOwner());
            this.stage.show();
            AddBookController addBookController = new AddBookController(this);
            addBookController.registerEvent();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void showSuccessMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Message");
        alert.setHeaderText(null);
        alert.setContentText("Book added successfully!");
        alert.show();
    }

    public void showErrorMessFill() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText("Please fill all blank fields!");
        alert.show();
    }

    public TextField getTitle() {
        return title;
    }

    public TextField getAuthor() {
        return author;
    }

    public DatePicker getPubdate() {
        return pubdate;
    }

    public DatePicker getReleaseDate() {
        return releaseDate;
    }

    public TextField getISBN() {
        return ISBN;
    }

    public TextField getPrice() {
        return price;
    }

    public TextField getSubject() {
        return subject;
    }

    public TextField getCategory() {
        return category;
    }

    public TextField getURL() {
        return URL;
    }

    public TextField getBookType() {
        return bookType;
    }

    public TextField getQuantity() {
        return quantity;
    }

    public Button getAddBookButton() {
        return addBookButton;
    }

    public Button getBackButton() {
        return backButton;
    }
}
