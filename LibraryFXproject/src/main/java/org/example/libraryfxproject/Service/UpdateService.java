package org.example.libraryfxproject.Service;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
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

    public void updatePieChart(PieChart genreCirculationChart) {
        bookDAO.loadGenreCirculationData(genreCirculationChart);

        int totalQuantity = genreCirculationChart.getData().stream()
                .mapToInt(data -> (int) data.getPieValue())
                .sum();

        for (PieChart.Data data : genreCirculationChart.getData()) {
            String originalName = data.getName();

            // Tạo Tooltip và đặt thời gian hiển thị ngay lập tức
            Tooltip tooltip = new Tooltip();
            Tooltip.install(data.getNode(), tooltip);
            tooltip.setShowDelay(Duration.ZERO);

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

    public void updateBarChart(BarChart<String, Number> genreBorrowedBarChart) {
        ObservableList<XYChart.Series<String, Number>> seriesList = bookDAO.loadGenreBorrowedData();
        genreBorrowedBarChart.getData().addAll(seriesList);

        for (XYChart.Series<String, Number> series : seriesList) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                // Tạo tooltip với cả tên thể loại và số lượng mượn
                String tooltipText = data.getXValue() + ": " + data.getYValue();
                Tooltip tooltip = new Tooltip(tooltipText);
                Tooltip.install(data.getNode(), tooltip);
                tooltip.setShowDelay(Duration.ZERO);

                data.getNode().setOnMouseEntered(event -> {
                    data.getNode().setStyle("-fx-bar-fill: #FF5733;");

                    Bounds bounds = data.getNode().localToScreen(data.getNode().getBoundsInLocal());
                    double tooltipX = bounds.getMinX() + 10;
                    if (tooltipX + tooltip.getWidth() > genreBorrowedBarChart.getScene().getWindow().getX() + genreBorrowedBarChart.getScene().getWindow().getWidth()) {
                        tooltipX = bounds.getMinX() - tooltip.getWidth() - 10;
                    }
                    tooltip.show(data.getNode(), tooltipX, bounds.getMinY());
                });

                data.getNode().setOnMouseExited(event -> {
                    data.getNode().setStyle("");
                    tooltip.hide();
                });
            }
        }
    }


    public void populateTableView(TableView<ObservableList<String>> tableView, int limit) {
        tableView.setItems(cartDAO.getActivities(limit));
    }

}
