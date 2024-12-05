package org.example.libraryfxproject.Service;

import org.example.libraryfxproject.Dao.BookDAO;
import org.example.libraryfxproject.Model.Book;
import org.example.libraryfxproject.Model.Comment;
import org.example.libraryfxproject.Util.ValidationUtils;

import java.util.List;

public class BookService {
    private final BookDAO bookDAO = BookDAO.getInstance();
    private static BookService bookService;

    public static synchronized BookService getInstance() {
        if (bookService == null) {
            bookService = new BookService();
        }
        return bookService;
    }

    public BookDAO getBookDAO() {
        return bookDAO;
    }

    /**
     * Khởi tạo BookService và nạp dữ liệu từ BookDAO.
     */
    private BookService() {
        LoadService.loadData(bookDAO); // Nạp dữ liệu cho BookDAO
    }

    /**
     * Kiểm tra tính hợp lệ của các thông tin khi thêm sách.
     * Trả về mã lỗi nếu thông tin không hợp lệ, hoặc 0 nếu tất cả hợp lệ.
     * @param book        sách cần kiểm tra
     * @return Mã lỗi nếu có (1-9), hoặc 0 nếu tất cả hợp lệ.
     */
    public int validateAddBookInput(Book book) {

        // Kiểm tra ISBN
        if (!ValidationUtils.isValidISBN(book.getISBN())) {
            return 1; // Invalid ISBN
        }

        // Kiểm tra tiêu đề sách
        if (!ValidationUtils.isValidTitle(book.getTitle())) {
            return 2; // Invalid title
        }

        // Kiểm tra giá
        try {
            Double.parseDouble(book.getPrice());
        } catch (NumberFormatException e) {
            return 3; // Invalid price
        }

        // Kiểm tra thể loại
        if (!ValidationUtils.isValidSubject(book.getSubject())) {
            return 4; // Invalid subject
        }

        // Kiểm tra danh mục
        if (!ValidationUtils.isValidCategory(book.getCategory())) {
            return 5; // Invalid category
        }

        // Kiểm tra URL
        if (!ValidationUtils.isValidURL(book.getURL())) {
            return 6; // Invalid URL
        }

        // Kiểm tra loại sách
        if (!ValidationUtils.isValidTitle(book.getBookType())) {
            return 7; // Invalid bookType
        }

        // Kiểm tra số lượng
        try {
            Integer.parseInt(book.getQuantity());
        } catch (NumberFormatException e) {
            return 8; // Invalid quantity
        }

        // Kiểm tra tác giả
        if (!ValidationUtils.isValidAuthor(book.getAuthor())) {
            return 9; // Invalid author
        }

        return 0; // Tất cả hợp lệ
    }

    /**
     * Thêm sách vào cơ sở dữ liệu.
     * @param book           Sách cần thêm
     */
    public void insertBookToDatabase(Book book) {
        bookDAO.insertBookToDatabase(book);
        UpdateService.getInstance().updateBookDAO();
    }

    /**
     * Xóa sách khỏi cơ sở dữ liệu.
     * @param title Tiêu đề sách cần xóa
     */
    public void deleteBookFromDatabase(String title) {
        bookDAO.deleteBookFromDatabase(title);
        UpdateService.getInstance().updateBookDAO();
    }

    /**
     * Sửa thông tin của một cuốn sách.
     * @param ISBN      ISBN của sách cần sửa
     * @param attribute Thuộc tính cần sửa (ví dụ: "title", "price", v.v.)
     * @param newValue  Giá trị mới
     */
    public void modifyBook(String ISBN, String attribute, String newValue) {
        BookDAO.modifyBookAttribute(ISBN, attribute, newValue);
    }

    /**
     * Kiểm tra xem sách có đủ số lượng trong kho không.
     * @param ISBN ISBN của cuốn sách cần kiểm tra
     * @return true nếu số lượng sách còn, false nếu hết sách
     */
    public boolean hasEnoughQuantity(String ISBN) {
        int quantity = bookDAO.fetchQuantityFromBooks(ISBN, 3);
        return quantity > 0;
    }

    /**
     * Lấy thông tin sách từ tiêu đề sách.
     * @param title Tiêu đề sách cần tìm
     * @return {@link Book} tương ứng với tiêu đề sách
     */
    public Book getBookByTitle(String title) {
        return bookDAO.getBookByBookname(title);
    }

    /**
     * Kiểm tra xem có cuốn sách nào có ISBN trùng khớp không.
     * @param ISBN ISBN của cuốn sách cần kiểm tra
     * @return true nếu có sách với ISBN trùng khớp, false nếu không
     */
    public boolean hasBookWithISBN(String ISBN) {
        Book book = bookDAO.findBookByDistinctAttribute(ISBN, 3);
        return book != null;
    }

    /**
     * Lấy ISBN của sách từ tiêu đề sách.
     * @param title Tiêu đề sách
     * @return ISBN của sách tương ứng
     */
    public String fetchISBNByTitle(String title) {
        return bookDAO.fetchISBNFromBooks(title, 2);
    }

    /**
     * Thêm bình luận mới cho sách.
     * @param book    Cuốn sách cần thêm bình luận
     * @param comment Bình luận cần thêm
     * @throws RuntimeException nếu có lỗi trong quá trình thêm bình luận
     */
    public void addComment(Book book, Comment comment) throws RuntimeException {
        bookDAO.addNewComment(book, comment);
    }

    /**
     * Lấy tất cả bình luận của một cuốn sách.
     * @param book Cuốn sách cần lấy bình luận
     * @return danh sách các bình luận của sách
     */
    public List<Comment> getAllComment(Book book) {
        String ISBN = book.getISBN();
        return bookDAO.getBookComment(ISBN);
    }
}
