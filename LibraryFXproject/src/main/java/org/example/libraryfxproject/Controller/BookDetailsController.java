package org.example.libraryfxproject.Controller;

import javafx.stage.FileChooser;
import org.example.libraryfxproject.Model.Book;
import org.example.libraryfxproject.Service.QRCodeService;
import org.example.libraryfxproject.Util.AlertDisplayer;
import org.example.libraryfxproject.Util.DateTimeUtils;
import org.example.libraryfxproject.View.BookDetailsView;

import java.io.File;

public class BookDetailsController extends BaseController {
    BookDetailsView bookDetailsView;
    QRCodeService qrCodeService;

    public BookDetailsController(BookDetailsView bookDetailsView, AlertDisplayer alertDisplayer) {
        super(alertDisplayer);
        this.bookDetailsView = bookDetailsView;
        qrCodeService = QRCodeService.getInstance();
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
        try {
            bookDetailsView.getQrCodeImageView().setImage(qrCodeService.generateQRCode(book, 200, 200));
        } catch (Exception e) {
            alertDisplayer.showErrorAlert("QR Code Error", "Failed to generate QR code: " + e.getMessage());
        }
        bookDetailsView.getSaveQrCodeButton().setOnAction(e -> {
            handleSaveQrCode(book);
        });
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
