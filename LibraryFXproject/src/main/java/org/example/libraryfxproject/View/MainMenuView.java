package org.example.libraryfxproject.View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import javafx.stage.Screen;
import javafx.stage.Stage;
import org.example.libraryfxproject.Controller.MainMenuController;

import java.io.IOException;

public class MainMenuView {
    private final Stage stage;
    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private ListView<String> suggestions = new ListView<>();
    @FXML
    private MenuItem logoutItem;
    @FXML
    private MenuButton profileButton;
    private boolean isSelecting = false;


    @FXML
    private Label totalBooksLabel;

    @FXML
    private Label activeStudentsLabel;

    @FXML
    private Label booksBorrowedLabel;

    @FXML
    private Label overdueBooksLabel;

    @FXML
    private PieChart genreCirculationChart;

    @FXML
    private Pane chartPane;

    @FXML
    private Label chartTitleLabel;

    public ListView<String> getSuggestions() {
        return suggestions;
    }


    public MainMenuView(Stage stage) {
        this.stage = stage;
        initializeMainMenuView();
    }

    public boolean isSelecting() {
        return isSelecting;
    }

    public void setSelecting(boolean selecting) {
        isSelecting = selecting;
    }

    public MenuButton getProfileButton() {
        return profileButton;
    }

    public MenuItem getLogoutItem() {
        return logoutItem;
    }

    public ListView<String> getSuggestions() {
        return suggestions;
    }

    public Stage getStage() {
        return stage;
    }

    public TextField getSearchField() {
        return searchField;
    }

    public void setSearchField(TextField searchField) {
        this.searchField = searchField;
    }

    public Button getSearchButton() {
        return searchButton;
    }

    public void setSearchButton(Button searchButton) {
        this.searchButton = searchButton;
    }

    public Label getTotalBooksLabel() {
        return totalBooksLabel;
    }

    public void setTotalBooksLabel(Label totalBooksLabel) {
        this.totalBooksLabel = totalBooksLabel;
    }

    public Label getActiveStudentsLabel() {
        return activeStudentsLabel;
    }

    public void setActiveStudentsLabel(Label activeStudentsLabel) {
        this.activeStudentsLabel = activeStudentsLabel;
    }

    public Label getBooksBorrowedLabel() {
        return booksBorrowedLabel;
    }

    public void setBooksBorrowedLabel(Label booksBorrowedLabel) {
        this.booksBorrowedLabel = booksBorrowedLabel;
    }

    public Label getOverdueBooksLabel() {
        return overdueBooksLabel;
    }

    public void setOverdueBooksLabel(Label overdueBooksLabel) {
        this.overdueBooksLabel = overdueBooksLabel;
    }

    public PieChart getGenreCirculationChart() {
        return genreCirculationChart;
    }

    public void setGenreCirculationChart(PieChart genreCirculationChart) {
        this.genreCirculationChart = genreCirculationChart;
    }

    public Pane getChartPane() {
        return chartPane;
    }

    public void setChartPane(Pane chartPane) {
        this.chartPane = chartPane;
    }

    public Label getChartTitleLabel() {
        return chartTitleLabel;
    }

    public void setChartTitleLabel(Label chartTitleLabel) {
        this.chartTitleLabel = chartTitleLabel;
    }

    public void initializeMainMenuView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/libraryfxproject/MainMenu.fxml"));
        fxmlLoader.setController(this); // Đặt controller là đối tượng hiện tại
        try {
            Parent mainViewParent = fxmlLoader.load();
            Scene scene = new Scene(mainViewParent);
            stage.setX(-5);
            stage.setY(-5);
            stage.setWidth(Screen.getPrimary().getBounds().getWidth() + 10);
            stage.setHeight(Screen.getPrimary().getBounds().getHeight() - 30);
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);
            MainMenuController mainMenuController = new MainMenuController(this);
            mainMenuController.registerEvent();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showErrorMessFill() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText("Please fill the blank search fields!");
        alert.show();
    }
}
