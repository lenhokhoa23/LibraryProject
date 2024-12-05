package org.example.libraryfxproject.Dao;

import org.example.libraryfxproject.Model.Librarian;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Lớp này cung cấp các phương thức để thao tác với librarian data trong database.
 * Nó bao gồm các phương thức để tải dữ liệu, tìm kiếm thủ thư theo ID hoặc username, và hỗ trợ các chức năng quản lý thủ thư.
 */
public class LibrarianDAO extends GeneralDAO<String, Librarian> {

    private static LibrarianDAO librarianDAO;

    private LibrarianDAO() {
    }

    public static synchronized LibrarianDAO getInstance() {
        if (librarianDAO == null) {
            librarianDAO = new LibrarianDAO();
        }
        return librarianDAO;
    }

    /**
     * Tải dữ liệu thủ thư từ cơ sở dữ liệu vào bộ nhớ.
     * Dữ liệu thủ thư sẽ được lưu trữ trong `dataMap` với key là username.
     */
    @Override
    public void loadData() {
        String sql = "SELECT * FROM Librarian";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                // Lấy thông tin từ ResultSet và tạo đối tượng Librarian
                Librarian librarian = new Librarian(resultSet.getString("username"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phoneNumber"),
                        resultSet.getInt("Librarian_ID"),
                        resultSet.getString("workShift"));

                // Thêm đối tượng Librarian vào dataMap
                dataMap.put(librarian.getUsername(), librarian);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tìm kiếm một thủ thư theo ID hoặc username.
     * @param type Loại tìm kiếm (có thể là ID hoặc username).
     * @param searchType Kiểu tìm kiếm (1: tìm theo ID, 2: tìm theo username).
     * @return Thủ thư nếu tìm thấy, null nếu không tìm thấy.
     */
    public Librarian findLibrarian(String type, int searchType) {
        Librarian librarian = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = null;

        // Chọn câu lệnh truy vấn dựa vào loại tìm kiếm
        if (searchType == 1) {
            query = "SELECT * FROM librarian WHERE Librarian_ID = ?";
        } else if (searchType == 2) {
            query = "SELECT * FROM librarian WHERE username = ?";
        }

        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, type);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                librarian = new Librarian(resultSet.getString("username"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phoneNumber"),
                        resultSet.getInt("Librarian_ID"),
                        resultSet.getString("workShift"));
            } else {
                System.out.println("Không tìm thấy thủ thư với " + type);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return librarian;
    }
}
