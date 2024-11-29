package org.example.libraryfxproject.View;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;
import org.example.libraryfxproject.Controller.BookDetailsController;
import org.example.libraryfxproject.Controller.MainMenuController;
import org.example.libraryfxproject.Model.Book;
import org.example.libraryfxproject.Util.AlertDisplayer;
import org.example.libraryfxproject.Util.JavaFXAlertDisplayer;

import java.io.IOException;

public class BookDetailsView {
    private Stage stage;

    private AlertDisplayer alertDisplayer;

    @FXML
    Label titleLabel;
    @FXML
    Label authorLabel;
    @FXML
    Label isbnLabel;
    @FXML
    Label pubDateLabel;
    @FXML
    Label releaseDateLabel;
    @FXML
    Label priceLabel;
    @FXML
    Label subjectLabel;
    @FXML
    Label categoryLabel;
    @FXML
    Label bookTypeLabel;
    @FXML
    Label quantityLabel;
    @FXML
    Hyperlink urlHyperlink;
    @FXML
    ImageView qrCodeImageView;
    @FXML
    Button saveQrCodeButton;

    public ImageView getQrCodeImageView() {
        return qrCodeImageView;
    }

    public void setQrCodeImageView(ImageView qrCodeImageView) {
        this.qrCodeImageView = qrCodeImageView;
    }

    public Button getSaveQrCodeButton() {
        return saveQrCodeButton;
    }

    public void setSaveQrCodeButton(Button saveQrCodeButton) {
        this.saveQrCodeButton = saveQrCodeButton;
    }

    public Hyperlink getUrlHyperlink() {
        return urlHyperlink;
    }

    public void setUrlHyperlink(Hyperlink urlHyperlink) {
        this.urlHyperlink = urlHyperlink;
    }

    public Label getTitleLabel() {
        return titleLabel;
    }

    public void setTitleLabel(Label titleLabel) {
        this.titleLabel = titleLabel;
    }

    public Label getAuthorLabel() {
        return authorLabel;
    }

    public void setAuthorLabel(Label authorLabel) {
        this.authorLabel = authorLabel;
    }

    public Label getIsbnLabel() {
        return isbnLabel;
    }

    public void setIsbnLabel(Label isbnLabel) {
        this.isbnLabel = isbnLabel;
    }

    public Label getPubDateLabel() {
        return pubDateLabel;
    }

    public void setPubDateLabel(Label pubDateLabel) {
        this.pubDateLabel = pubDateLabel;
    }

    public Label getReleaseDateLabel() {
        return releaseDateLabel;
    }

    public void setReleaseDateLabel(Label releaseDateLabel) {
        this.releaseDateLabel = releaseDateLabel;

    }

    public Label getPriceLabel() {
        return priceLabel;
    }

    public void setPriceLabel(Label priceLabel) {
        this.priceLabel = priceLabel;
    }

    public Label getSubjectLabel() {
        return subjectLabel;
    }

    public void setSubjectLabel(Label subjectLabel) {
        this.subjectLabel = subjectLabel;
    }

    public Label getCategoryLabel() {
        return categoryLabel;
    }

    public void setCategoryLabel(Label categoryLabel) {
        this.categoryLabel = categoryLabel;
    }

    public Label getBookTypeLabel() {
        return bookTypeLabel;
    }

    public void setBookTypeLabel(Label bookTypeLabel) {
        this.bookTypeLabel = bookTypeLabel;
    }

    public Label getQuantityLabel() {
        return quantityLabel;
    }

    public void setQuantityLabel(Label quantityLabel) {
        this.quantityLabel = quantityLabel;
    }

    public BookDetailsView(Book book) {
        initializeBookDetailsView(book);
    }

    public void initializeBookDetailsView(Book book) {
        stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/libraryfxproject/BookDetailView.fxml"));
        fxmlLoader.setController(this);
        alertDisplayer = new JavaFXAlertDisplayer();
        try {
            Parent bookDetailsParent = fxmlLoader.load();
            Scene scene = new Scene(bookDetailsParent);
            stage.setScene(scene);
            stage.show();
            BookDetailsController bookDetailsController = new BookDetailsController(this, alertDisplayer);
            bookDetailsController.registerEvent(book);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
