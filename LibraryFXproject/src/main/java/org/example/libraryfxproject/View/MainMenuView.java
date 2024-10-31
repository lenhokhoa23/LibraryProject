package org.example.libraryfxproject.View;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.chart.PieChart;
import javafx.scene.layout.Pane;

import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.libraryfxproject.Controller.MainMenuController;
import org.example.libraryfxproject.Dao.BookDAO;
import org.example.libraryfxproject.Model.Book;

import java.io.IOException;

public class MainMenuView {
    @FXML
    private TableView<Book> catalogTableView;

    @FXML
    private Pagination catalogPagination;

    @FXML
    private TableColumn<Book, Integer> itemIdColumn;

    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, String> subjectColumn;

    @FXML
    private TableColumn<Book, String> bookTypeColumn;

    @FXML
    private TableColumn<Book, String> quantityColumn;

    @FXML
    private final Stage stage;

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;
    @FXML
    private TextField searchCatalog;

    @FXML
    private ComboBox<String> filterComboBox;

    @FXML
    private Button refreshButton;

    @FXML
    private Button addItemButton;

    @FXML
    private Button deleteItemButton;

    @FXML
    private ToggleButton searchToggle;

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

    public TableView<Book> getCatalogTableView() {
        return catalogTableView;
    }

    public Pagination getCatalogPagination() {
        return catalogPagination; // Add this method
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

    public TextField getSearchCatalog() {
        return searchCatalog;
    }

    public void setSearchCatalog(TextField searchCatalog) {
        this.searchCatalog = searchCatalog;
    }

    public ComboBox<String> getFilterComboBox() {
        return filterComboBox;
    }

    public void setFilterComboBox(ComboBox<String> filterComboBox) {
        this.filterComboBox = filterComboBox;
    }

    public Button getRefreshButton() {
        return refreshButton;
    }

    public void setRefreshButton(Button refreshButton) {
        this.refreshButton = refreshButton;
    }

    public Button getAddItemButton() {
        return addItemButton;
    }

    public void setAddItemButton(Button addItemButton) {
        this.addItemButton = addItemButton;
    }

    public Button getDeleteItemButton() {
        return deleteItemButton;
    }

    public void setDeleteItemButton(Button deleteItemButton) {
        this.deleteItemButton = deleteItemButton;
    }

    public ToggleButton getSearchToggle() {
        return searchToggle;
    }

    public void setSearchToggle(ToggleButton searchToggle) {
        this.searchToggle = searchToggle;
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
        fxmlLoader.setController(this); // Set controller as the current object
        try {
            Parent mainViewParent = fxmlLoader.load();
            stage.setWidth(Screen.getPrimary().getBounds().getWidth());
            stage.setHeight(Screen.getPrimary().getBounds().getHeight());
            stage.setX((Screen.getPrimary().getBounds().getWidth() - stage.getWidth()) / 2);
            stage.setY((Screen.getPrimary().getBounds().getHeight() - stage.getHeight()) / 2);
            setupCatalogTableView();
         
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

    public void setupCatalogTableView() {
        itemIdColumn.setCellValueFactory(new PropertyValueFactory<>("no"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        bookTypeColumn.setCellValueFactory(new PropertyValueFactory<>("bookType"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }
    private void handleActionClick(Book book) {
        // Handle button click event
        System.out.println("Button clicked for book: " + book.getTitle());
    }


    public void showErrorMessFill() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText("Please fill the blank search fields!");
        alert.show();
    }
    


}
