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
import org.example.libraryfxproject.Model.Cart;

import java.util.List;

public class UpdateService {

    private BookDAO bookDAO;
    private CartDAO cartDAO;
    private UserDAO userDAO;
    private AccountDAO accountDAO = AccountDAO.getInstance();
    private static UpdateService updateService;

    private UpdateService() {
        bookDAO = BookDAO.getInstance();
        cartDAO = CartDAO.getInstance();
        userDAO = UserDAO.getInstance();
    }

    public static synchronized UpdateService getInstance() {
        if (updateService == null) {
            updateService = new UpdateService();
        }
        return updateService;
    }

    /**
     * Cập nhật nhãn với giá trị mới dựa trên loại (1: Số lượng sách, 2: Người dùng ngẫu nhiên, 3: Số sách mượn, 4: Sách quá hạn).
     * @param type Loại dữ liệu cần cập nhật
     * @return Label với giá trị được cập nhật
     */
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

    /**
     * Cập nhật dữ liệu cho biểu đồ tròn thể hiện số lượng sách theo thể loại.
     * @param genreCirculationChart Biểu đồ tròn để cập nhật
     */
    public void updatePieChart(PieChart genreCirculationChart) {
        bookDAO.loadGenreCirculationData(genreCirculationChart);

        int totalQuantity = genreCirculationChart.getData().stream()
                .mapToInt(data -> (int) data.getPieValue())
                .sum();

        for (PieChart.Data data : genreCirculationChart.getData()) {
            String originalName = data.getName();

            Tooltip tooltip = new Tooltip();
            Tooltip.install(data.getNode(), tooltip);
            tooltip.setShowDelay(Duration.ZERO);

            data.getNode().setOnMouseEntered(event -> {
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

    /**
     * Cập nhật dữ liệu cho biểu đồ cột (BarChart) thể hiện số lượng sách mượn theo thể loại.
     * @param genreBorrowedBarChart Biểu đồ cột để cập nhật
     */
    public void updateBarChart(BarChart<String, Number> genreBorrowedBarChart) {
        ObservableList<XYChart.Series<String, Number>> seriesList = bookDAO.loadGenreBorrowedData();
        genreBorrowedBarChart.getData().addAll(seriesList);

        for (XYChart.Series<String, Number> series : seriesList) {
            for (XYChart.Data<String, Number> data : series.getData()) {
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

    /**
     * Cập nhật dữ liệu cho bảng (TableView) với một số lượng giới hạn các hoạt động.
     * @param tableView Bảng cần cập nhật
     * @param limit     Giới hạn số lượng bản ghi
     */
    public void populateTableView(TableView<ObservableList<String>> tableView, int limit) {
        tableView.setItems(cartDAO.getActivities(limit));
    }

    /**
     * Cập nhật dữ liệu cho bảng (TableView) với các hoạt động của người dùng trong khoảng thời gian cụ thể.
     * @param tableView Bảng cần cập nhật
     * @param ID        ID người dùng
     * @param startDate Ngày bắt đầu
     * @param endDate   Ngày kết thúc
     */
    public void populateTableView(TableView<ObservableList<String>> tableView, int ID, String startDate, String endDate) {
        tableView.setItems(cartDAO.getActivities(ID, startDate, endDate));
    }

    /**
     * Cập nhật dữ liệu cho bảng (TableView) với các hoạt động của giỏ hàng cụ thể.
     * @param tableView Bảng cần cập nhật
     * @param cartId    ID giỏ hàng
     */
    public void populateTableViewByCartId(TableView<ObservableList<String>> tableView, int cartId) {
        tableView.setItems(cartDAO.getActivitiesByCartId(cartId));
    }

    /**
     * Cập nhật dữ liệu cho AccountDAO.
     */
    public void updateAccountDAO() {
        LoadService.loadData(accountDAO);
    }

    /**
     * Cập nhật dữ liệu cho BookDAO.
     */
    public void updateBookDAO() {
        LoadService.loadData(bookDAO);
    }

    /**
     * Cập nhật dữ liệu cho CartDAO.
     */
    public void updateCartDAO() {
        LoadService.loadData(cartDAO);
    }

    /**
     * Cập nhật dữ liệu cho UserDAO.
     */
    public void updateUserDAO() {
        LoadService.loadData(userDAO);
    }
}
