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

    /**
     * Constructor tạo một thể hiện của GoogleBooksService để kết nối với Google Books API.
     * @throws Exception nếu có lỗi khi tạo kết nối đến Google Books API
     */
    public GoogleBooksService() throws Exception {
        booksService = new Books.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                null)
                .setApplicationName(LIBRARY_NAME)
                .setGoogleClientRequestInitializer(new BooksRequestInitializer("AIzaSyC4y_RrRbfruzkYMiUFdQi_kkzdpCBW8WE"))
                .build();
    }

    /**
     * Tìm một cuốn sách từ Google Books API dựa trên ISBN.
     * @param isbn Mã ISBN của cuốn sách
     * @return Đối tượng Book chứa thông tin cuốn sách nếu tìm thấy, null nếu không tìm thấy
     * @throws Exception nếu có lỗi trong quá trình kết nối hoặc lấy dữ liệu từ API
     */
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

    /**
     * Chuyển đối tượng Volume từ Google Books API thành đối tượng Book.
     * @param volume Đối tượng Volume từ Google Books API
     * @return Đối tượng Book với thông tin từ volume
     */
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

    /**
     * Lấy thông tin ISBN từ đối tượng Volume.
     * @param volumeInfo Thông tin về volume từ Google Books API
     * @return ISBN của cuốn sách, hoặc "N/A" nếu không có
     */
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

    /**
     * Lấy giá của cuốn sách từ thông tin Volume.
     * @param volume Thông tin về volume từ Google Books API
     * @return Giá của cuốn sách, hoặc "N/A" nếu không có thông tin giá
     */
    private String getPrice(Volume volume) {
        if (volume.getSaleInfo() != null && volume.getSaleInfo().getListPrice() != null) {
            return String.valueOf(volume.getSaleInfo().getListPrice().getAmount());
        }
        return "N/A";
    }

    /**
     * Lấy chủ đề của cuốn sách từ thông tin Volume.
     * @param volumeInfo Thông tin về volume từ Google Books API
     * @return Chủ đề của cuốn sách, hoặc "N/A" nếu không có thông tin
     */
    private String getSubject(Volume.VolumeInfo volumeInfo) {
        if (volumeInfo.getCategories() != null && !volumeInfo.getCategories().isEmpty()) {
            return volumeInfo.getCategories().get(0);
        }
        return "N/A";
    }

    /**
     * Lấy danh mục của cuốn sách từ thông tin Volume.
     * @param volumeInfo Thông tin về volume từ Google Books API
     * @return Danh mục của cuốn sách, hoặc "N/A" nếu không có thông tin
     */
    private String getCategory(Volume.VolumeInfo volumeInfo) {
        if (volumeInfo.getCategories() != null && !volumeInfo.getCategories().isEmpty()) {
            return String.join(", ", volumeInfo.getCategories());
        }
        return "N/A";
    }
}
