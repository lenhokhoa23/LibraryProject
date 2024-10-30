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

    public ListView<String> getSuggestions() {
        return suggestions;
    }
    
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

    public void initializeMainMenuView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/libraryfxproject/MainMenu.fxml"));
        fxmlLoader.setController(this); // Set controller as the current object
        try {
            Parent mainViewParent = fxmlLoader.load();
            stage.setWidth(Screen.getPrimary().getBounds().getWidth());
            stage.setHeight(Screen.getPrimary().getBounds().getHeight());
            stage.setX((Screen.getPrimary().getBounds().getWidth() - stage.getWidth()) / 2);
            stage.setY((Screen.getPrimary().getBounds().getHeight() - stage.getHeight()) / 2);
            Scene scene = new Scene(mainViewParent);
            stage.setScene(scene);
            stage.show();
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
    @FXML
    public void initialize() {
        setupCatalogTableView();
    }

}
