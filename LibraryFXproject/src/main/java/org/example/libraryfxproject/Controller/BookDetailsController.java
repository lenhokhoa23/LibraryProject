package org.example.libraryfxproject.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;
import org.example.libraryfxproject.Model.Book;
import org.example.libraryfxproject.Model.Comment;
import org.example.libraryfxproject.Service.BookService;
import org.example.libraryfxproject.Service.QRCodeService;
import org.example.libraryfxproject.Util.AlertDisplayer;
import org.example.libraryfxproject.Util.DateTimeUtils;
import org.example.libraryfxproject.View.BookDetailsView;
import org.example.libraryfxproject.View.CommentListCell;

import java.io.File;
import java.util.List;

public class BookDetailsController extends BaseController {
    BookDetailsView bookDetailsView;
    QRCodeService qrCodeService;
    private ObservableList<Comment> comments;
    BookService bookService;

    public BookDetailsController(BookDetailsView bookDetailsView, AlertDisplayer alertDisplayer, String username) {
        super(alertDisplayer);
        this.bookDetailsView = bookDetailsView;
        qrCodeService = QRCodeService.getInstance();
        comments = FXCollections.observableArrayList();
        bookService = BookService.getInstance();
    }

    public void registerEvent(Book book) {
        bookDetailsView.getTitleLabel().textProperty().set(book.getTitle());
        bookDetailsView.getAuthorLabel().setText(book.getAuthor());
        bookDetailsView.getBookTypeLabel().setText(book.getBookType());
        bookDetailsView.getCategoryLabel().setText(book.getCategory());
        bookDetailsView.getIsbnLabel().setText(book.getISBN());
        bookDetailsView.getPriceLabel().setText(book.getPrice());
        bookDetailsView.getPubDateLabel().setText(DateTimeUtils
                .formatDate(DateTimeUtils.parseDate(book.getPubdate())));
        bookDetailsView.getQuantityLabel().setText(book.getQuantity());
        bookDetailsView.getSubjectLabel().setText(book.getSubject());
        bookDetailsView.getReleaseDateLabel().setText(DateTimeUtils
                .formatDate(DateTimeUtils.parseDate(book.getReleaseDate())));
        bookDetailsView.getUrlHyperlink().setText(book.getURL());
        bookDetailsView.getUrlHyperlink().setOnAction(e -> {
            try {
                java.awt.Desktop.getDesktop().browse(new java.net.URI(bookDetailsView.getUrlHyperlink().getText()));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        bookDetailsView.getBookImage().setImage(new Image(getClass().getResource("/org/example/libraryfxproject/images/image_book2.png").toString(), true));
        try {
            bookDetailsView.getQrCodeImageView().setImage(qrCodeService.generateQRCode(book, 200, 200));
        } catch (Exception e) {
            alertDisplayer.showErrorAlert("QR Code Error", "Failed to generate QR code: " + e.getMessage());
        }
        bookDetailsView.getSaveQrCodeButton().setOnAction(e -> {
            handleSaveQrCode(book);
        });
        bookDetailsView.getNewCommentArea().setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                handleSubmitComment(bookDetailsView.getUsername());
            }
        });
        bookDetailsView.getSubmitButton().setOnAction(e -> {
            handleSubmitComment(bookDetailsView.getUsername());
        });
        loadListComment();
        bookDetailsView.getCommentsListView().setItems(comments);
        bookDetailsView.getCommentsListView().setCellFactory(commentListView -> new CommentListCell());
    }
    private void handleSubmitComment(String username) {
        String content = bookDetailsView.getNewCommentArea().getText();
        String author = username;
        Comment comment;

        if (!content.isEmpty()) {
            comment = new Comment(author, content);
            comments.add(comment);
            bookDetailsView.getNewCommentArea().clear();
            // Them comment vao dtb
            Book book = bookService.getBookByTitle(bookDetailsView.getTitleLabel().getText());
            try {
                bookService.addComment(book, comment);
            } catch (RuntimeException e) {
                alertDisplayer.showErrorAlert("Error", "An error occured when adding comment.");
                e.printStackTrace();
            }
        }
    }

    private void loadListComment() {
        List<Comment> commentList = bookService.getAllComment(bookService
                .getBookByTitle(bookDetailsView.getTitleLabel().getText()));
        for (Comment comment : commentList) {
            comments.add(comment);
        }
    }
    private void handleSaveQrCode(Book book) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save QR Code");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png")
        );
        fileChooser.setInitialFileName(book.getTitle().replaceAll("[^a-zA-Z0-9]", "_") + "_QR.png");

        File file = fileChooser.showSaveDialog(bookDetailsView.getQrCodeImageView().getScene().getWindow());

        if (file != null) {
            try {
                qrCodeService.saveQRCodeToFile(book, file.getAbsolutePath());
                alertDisplayer.showInformationAlert("Success", "QR code saved successfully!");
            } catch (Exception e) {
                alertDisplayer.showErrorAlert("Error", "Failed to save QR code: " + e.getMessage());
            }
        }
    }
}
