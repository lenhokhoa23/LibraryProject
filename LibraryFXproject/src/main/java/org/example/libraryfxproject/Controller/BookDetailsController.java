package org.example.libraryfxproject.Controller;

import org.example.libraryfxproject.Model.Book;
import org.example.libraryfxproject.Util.AlertDisplayer;
import org.example.libraryfxproject.Util.DateTimeUtils;
import org.example.libraryfxproject.View.BookDetailsView;

public class BookDetailsController extends BaseController {
    BookDetailsView bookDetailsView;

    public BookDetailsController(BookDetailsView bookDetailsView, AlertDisplayer alertDisplayer) {
        super(alertDisplayer);
        this.bookDetailsView = bookDetailsView;
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
    }
}
