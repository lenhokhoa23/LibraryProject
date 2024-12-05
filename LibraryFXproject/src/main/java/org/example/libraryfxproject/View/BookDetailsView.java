package org.example.libraryfxproject.View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.libraryfxproject.Controller.BookDetailsController;
import org.example.libraryfxproject.Model.Book;
import org.example.libraryfxproject.Model.Comment;
import org.example.libraryfxproject.Util.AlertDisplayer;
import org.example.libraryfxproject.Util.JavaFXAlertDisplayer;

import java.io.IOException;

public class BookDetailsView {

    private Stage stage;
    private AlertDisplayer alertDisplayer;
    private String username;

    /** FXML buttons declaration with getters and setters. */
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
    @FXML
    ImageView bookImage;
    @FXML
    TextArea newCommentArea;
    @FXML
    Button submitButton;
    @FXML
    ListView<Comment> commentsListView;


    public String getUsername() {
        return username;
    }

    public ListView<Comment> getCommentsListView() {
        return commentsListView;
    }

    public Button getSubmitButton() {
        return submitButton;
    }

    public TextArea getNewCommentArea() {
        return newCommentArea;
    }

    public ImageView getBookImage() {
        return bookImage;
    }

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

    /**
     * Khởi tạo một đối tượng `BookDetailsView` với cuốn sách và tên người dùng.
     * @param book đối tượng `Book` chứa thông tin chi tiết của cuốn sách.
     * @param username tên người dùng hiện tại.
     */
    public BookDetailsView(Book book, String username) {
        commentsListView = new ListView<>();
        this.username = username;
        initializeBookDetailsView(book);
    }

    /**
     * Khởi tạo giao diện chi tiết cuốn sách, tải tệp FXML và thiết lập các sự kiện liên quan.
     * @param book đối tượng `Book` chứa thông tin chi tiết của cuốn sách cần hiển thị.
     */
    public void initializeBookDetailsView(Book book) {
        stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/libraryfxproject/views/BookDetailView.fxml"));
        fxmlLoader.setController(this);
        alertDisplayer = JavaFXAlertDisplayer.getInstance();
        try {
            Parent bookDetailsParent = fxmlLoader.load();
            Scene scene = new Scene(bookDetailsParent);
            stage.setScene(scene);
            stage.show();

            // Khởi tạo và đăng ký sự kiện cho BookDetailsController.
            BookDetailsController bookDetailsController = new BookDetailsController(this, alertDisplayer, username);
            bookDetailsController.registerEvent(book);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
