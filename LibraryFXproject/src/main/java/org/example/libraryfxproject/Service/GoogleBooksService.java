package org.example.libraryfxproject.Service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.books.Books;
import com.google.api.services.books.BooksRequestInitializer;
import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;
import org.example.libraryfxproject.Model.Book;
import org.example.libraryfxproject.Util.ValidationUtils;

import java.util.ArrayList;
import java.util.List;

public class GoogleBooksService {
    private static final String LIBRARY_NAME = "VNU-LIC";
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 1000;

    private final Books booksService;

    public GoogleBooksService() throws Exception {
        booksService = new Books.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                null)
                .setApplicationName(LIBRARY_NAME)
                .build();
    }

    public Book getBookByISBN(String isbn) throws Exception {
        if (isbn == null || !ValidationUtils.isValidISBN(isbn)) {
            throw new IllegalArgumentException("ISBN không hợp lệ");
        }

        int retries = 0;
        while (retries < MAX_RETRIES) {
            try {
                String query = "isbn:" + isbn;
                Books.Volumes.List volumesList = booksService.volumes().list(query);
                Volumes volumes = volumesList.execute();
                if (volumes == null || volumes.getItems() == null || volumes.getItems().isEmpty()) {
                    return null;
                }
                Volume volume = volumes.getItems().get(0);
                return convertVolumeToBook(volume);

            } catch (Exception e) {
                retries++;
                if (retries == MAX_RETRIES) {
                    throw new RuntimeException("Không thể kết nối đến Google Books API sau " + MAX_RETRIES + " lần thử. Vui lòng thử lại sau.", e);
                }
                Thread.sleep(RETRY_DELAY_MS);
                System.out.println("Đang thử kết nối lại lần " + retries + "...");
            }
        }
        return null;
    }

    private Book convertVolumeToBook(Volume volume) {
        Volume.VolumeInfo volumeInfo = volume.getVolumeInfo();
        Book book = new Book();
        book.setTitle(volumeInfo.getTitle());
        book.setAuthor(volumeInfo.getAuthors() != null ? String.join(", ", volumeInfo.getAuthors()) : "Unknown");
        book.setPubdate(volumeInfo.getPublishedDate());
        book.setISBN(getISBN(volumeInfo));
        book.setPrice(getPrice(volume));
        book.setSubject(getSubject(volumeInfo));
        book.setCategory(getCategory(volumeInfo));
        book.setURL(volumeInfo.getInfoLink());
        book.setBookType(volumeInfo.getPrintType() != null ? volumeInfo.getPrintType() : "N/A");
        book.setReleaseDate("");
        return book;
    }

    private String getISBN(Volume.VolumeInfo volumeInfo) {
        if (volumeInfo.getIndustryIdentifiers() != null) {
            for (Volume.VolumeInfo.IndustryIdentifiers identifier : volumeInfo.getIndustryIdentifiers()) {
                if ("ISBN_13".equals(identifier.getType())) {
                    return identifier.getIdentifier();
                }
            }
        }
        return "N/A";
    }

    private String getPrice(Volume volume) {
        if (volume.getSaleInfo() != null && volume.getSaleInfo().getListPrice() != null) {
            return String.valueOf(volume.getSaleInfo().getListPrice().getAmount());
        }
        return "N/A";
    }

    private String getSubject(Volume.VolumeInfo volumeInfo) {
        if (volumeInfo.getCategories() != null && !volumeInfo.getCategories().isEmpty()) {
            return volumeInfo.getCategories().get(0);
        }
        return "N/A";
    }

    private String getCategory(Volume.VolumeInfo volumeInfo) {
        if (volumeInfo.getCategories() != null && !volumeInfo.getCategories().isEmpty()) {
            return String.join(", ", volumeInfo.getCategories());
        }
        return "N/A";
    }
}
