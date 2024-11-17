import org.example.libraryfxproject.Dao.BookDAO;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static org.junit.jupiter.api.Assertions.*;
public class BookTest {
    private static Connection connection;
    @BeforeAll
    public static void setupDatabase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        try (Statement stmt = connection.createStatement()) {
            String createTableSQL = "CREATE TABLE books ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "title VARCHAR(255), "
                    + "author VARCHAR(255), "
                    + "pubdate DATE, "
                    + "releaseDate DATE, "
                    + "ISBN VARCHAR(50), "
                    + "price VARCHAR(50), "
                    + "subject VARCHAR(255), "
                    + "category VARCHAR(255), "
                    + "URL VARCHAR(255), "
                    + "bookType VARCHAR(255), "
                    + "quantity VARCHAR(50))";
            stmt.execute(createTableSQL);
        }
    }

    @BeforeEach
    public void clearDatabase() throws SQLException {
        // Xóa dữ liệu trước mỗi test để đảm bảo không bị ảnh hưởng bởi test trước
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM books");
        }
    }

    @Test
    public void testInsertBookToDatabase() {
        // Chuẩn bị dữ liệu đầu vào
        String title = "Effective Java";
        String author = "Joshua Bloch";
        String pubdateStr = "01-Jan-01";
        String releaseDateStr = "01-Feb-01";
        String ISBN = "978-0134685991";
        String price = "45.99";
        String subject = "Programming";
        String category = "Computing";
        String URL = "http://example.com";
        String bookType = "Hardcover";
        String quantity = "5";

        // Gọi phương thức insertBookToDatabase
        BookDAO bookDAO = new BookDAO(); // Giả sử phương thức này nằm trong class Library
        bookDAO.insertBookToDatabase(title, author, pubdateStr, releaseDateStr, ISBN, price, subject, category, URL, bookType, quantity);

        // Kiểm tra xem sách đã được thêm vào chưa
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM books WHERE ISBN = ?")) {
            stmt.setString(1, ISBN);
            ResultSet rs = stmt.executeQuery();

            assertTrue(rs.next(), "Book should be added to database");
            assertEquals(title, rs.getString("title"));
            assertEquals(author, rs.getString("author"));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yy");
            LocalDate expectedPubDate = LocalDate.parse(pubdateStr, formatter);
            LocalDate expectedReleaseDate = LocalDate.parse(releaseDateStr, formatter);
            assertEquals(Date.valueOf(expectedPubDate), rs.getDate("pubdate"));
            assertEquals(Date.valueOf(expectedReleaseDate), rs.getDate("releaseDate"));
            assertEquals(price, rs.getString("price"));
            assertEquals(subject, rs.getString("subject"));
            assertEquals(category, rs.getString("category"));
            assertEquals(URL, rs.getString("URL"));
            assertEquals(bookType, rs.getString("bookType"));
            assertEquals(quantity, rs.getString("quantity"));
        } catch (SQLException e) {
            fail("Error querying the database: " + e.getMessage());
        }
    }

    @AfterAll
    public static void tearDownDatabase() throws SQLException {
        // Đóng kết nối cơ sở dữ liệu sau khi tất cả các test hoàn thành
        connection.close();
    }
}
