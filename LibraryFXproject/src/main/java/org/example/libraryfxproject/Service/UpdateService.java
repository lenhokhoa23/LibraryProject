package org.example.libraryfxproject.Service;

import javafx.beans.binding.Bindings;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;
import org.example.libraryfxproject.Dao.AccountDAO;
import org.example.libraryfxproject.Dao.BookDAO;
import org.example.libraryfxproject.Dao.CartDAO;
import org.example.libraryfxproject.Dao.UserDAO;
import org.example.libraryfxproject.Model.Account;

public class UpdateService {
    private final BookDAO bookDAO = new BookDAO();
    private final CartDAO cartDAO = new CartDAO();
    private final UserDAO userDAO = new UserDAO();

    public Label updatedLabel(int type) {
        Label label = new Label();
        if (type == 1) {
            label.setText(String.format("%,d", BookDAO.totalQuantity));
        } else if (type == 2) {
            int totalUsers = userDAO.getTotalUserCount();
            int randomUserCount = (int) (Math.random() * totalUsers);
            label.setText(String.format("%d", randomUserCount));
        } else if (type == 3) {
            label.setText(String.format("%,d", cartDAO.getBooksBorrowedCount()));
        } else if (type == 4) {
            label.setText(String.format("%,d", cartDAO.getOverdueBooksCount()));
        }
        return label;
    }

    public void updateChart(PieChart genreCirculationChart) {
        bookDAO.loadGenreCirculationData(genreCirculationChart);

        // Tính tổng số lượng sách từ tất cả các thể loại
        int totalQuantity = genreCirculationChart.getData().stream()
                .mapToInt(data -> (int) data.getPieValue())
                .sum();

        for (PieChart.Data data : genreCirculationChart.getData()) {
            String originalName = data.getName(); // Lưu tên ban đầu

            // Tạo Tooltip và đặt thời gian hiển thị ngay lập tức
            Tooltip tooltip = new Tooltip();
            Tooltip.install(data.getNode(), tooltip);
            tooltip.setShowDelay(Duration.ZERO); // Hiển thị ngay lập tức khi di chuột vào

            data.getNode().setOnMouseEntered(event -> {
                // Đặt nội dung của tooltip với tên và phần trăm
                String percentageText = String.format("%s %.1f%%", originalName, (data.getPieValue() / totalQuantity) * 100);
                tooltip.setText(percentageText);

                data.getNode().setScaleX(1.1);
                data.getNode().setScaleY(1.1);
            });

            data.getNode().setOnMouseExited(event -> {
                data.getNode().setScaleX(1.0);
                data.getNode().setScaleY(1.0);
            });
        }
    }


}
