package org.example.libraryfxproject.View;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.chart.PieChart;
import javafx.scene.layout.Pane;

import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.libraryfxproject.Controller.MainMenuController;
import org.example.libraryfxproject.Dao.BookDAO;
import org.example.libraryfxproject.Model.Book;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
    private BarChart<String, Number> genreBorrowedBarChart;

    @FXML
    private Label chartTitleLabel;

    @FXML
    private TableView<ObservableList<String>> recentActivitiesTable;

    @FXML
    private TableColumn<ObservableList<String>, String> activityTimeColumn;

    @FXML
    private TableColumn<ObservableList<String>, String> activityUserIDColumn;

    @FXML
    private TableColumn<ObservableList<String>, String> activityUsernameColumn;

    @FXML
    private TableColumn<ObservableList<String>, String> activityBookTitleColumn;

    @FXML
    private TableColumn<ObservableList<String>, String> activityISBNColumn;

    @FXML
    private TableColumn<ObservableList<String>, String> activityDueColumn;

    @FXML
    private Button viewAllButton;

    @FXML
    private Button modifyButton;

    @FXML
    private TextField isbnField;

    @FXML
    private TextField attributeField;

    @FXML
    private TextField newValueField;

    @FXML
    private Button updateButton;

    @FXML
    private Button backButton;

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

    public BarChart<String, Number> getGenreBorrowedBarChart() {
        return genreBorrowedBarChart;
    }

    public void setGenreBorrowedBarChart(BarChart<String, Number> genreBorrowedBarChart) {
        this.genreBorrowedBarChart = genreBorrowedBarChart;
    }

    public TableView<ObservableList<String>> getRecentActivitiesTable() {
        return recentActivitiesTable;
    }

    public void setRecentActivitiesTable(TableView<ObservableList<String>> recentActivitiesTable) {
        this.recentActivitiesTable = recentActivitiesTable;
    }

    public TableColumn<ObservableList<String>, String> getActivityTimeColumn() {
        return activityTimeColumn;
    }

    public void setActivityTimeColumn(TableColumn<ObservableList<String>, String> activityTimeColumn) {
        this.activityTimeColumn = activityTimeColumn;
    }

    public TableColumn<ObservableList<String>, String> getActivityUserIDColumn() {
        return activityUserIDColumn;
    }

    public void setActivityUserIDColumn(TableColumn<ObservableList<String>, String> activityUserIDColumn) {
        this.activityUserIDColumn = activityUserIDColumn;
    }

    public TableColumn<ObservableList<String>, String> getActivityUsernameColumn() {
        return activityUsernameColumn;
    }

    public void setActivityUsernameColumn(TableColumn<ObservableList<String>, String> activityUsernameColumn) {
        this.activityUsernameColumn = activityUsernameColumn;
    }

    public TableColumn<ObservableList<String>, String> getActivityBookTitleColumn() {
        return activityBookTitleColumn;
    }

    public void setActivityBookTitleColumn(TableColumn<ObservableList<String>, String> activityBookTitleColumn) {
        this.activityBookTitleColumn = activityBookTitleColumn;
    }

    public TableColumn<ObservableList<String>, String> getActivityISBNColumn() {
        return activityISBNColumn;
    }

    public void setActivityISBNColumn(TableColumn<ObservableList<String>, String> activityISBNColumn) {
        this.activityISBNColumn = activityISBNColumn;
    }

    public TableColumn<ObservableList<String>, String> getActivityDueColumn() {
        return activityDueColumn;
    }

    public void setActivityDueColumn(TableColumn<ObservableList<String>, String> activityDueColumn) {
        this.activityDueColumn = activityDueColumn;
    }

    public Button getViewAllButton() {
        return viewAllButton;
    }

    public void setViewAllButton(Button viewAllButton) {
        this.viewAllButton = viewAllButton;
    }

    public TextField getIsbnField() {
        return isbnField;
    }

    public void setIsbnField(TextField isbnField) {
        this.isbnField = isbnField;
    }

    public TextField getAttributeField() {
        return attributeField;
    }

    public void setAttributeField(TextField attributeField) {
        this.attributeField = attributeField;
    }

    public TextField getNewValueField() {
        return newValueField;
    }

    public void setNewValueField(TextField newValueField) {
        this.newValueField = newValueField;
    }

    public Button getUpdateButton() {
        return updateButton;
    }

    public void setUpdateButton(Button updateButton) {
        this.updateButton = updateButton;
    }

    public Button getBackButton() {
        return backButton;
    }

    public void setBackButton(Button backButton) {
        this.backButton = backButton;
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

    public Button getModifyButton() {
        return modifyButton;
    }

    public void setModifyButton(Button modifyButton) {
        this.modifyButton = modifyButton;
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

//    public void initializeModifyBookView() {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/libraryfxproject/modifyBook.fxml"));
//            loader.setController(this);
//            Parent root = loader.load();
//            Stage popupStage = new Stage();
//            popupStage.setTitle("Modify Book");
//            popupStage.setScene(new Scene(root));
//
//            popupStage.initModality(Modality.WINDOW_MODAL);
//            popupStage.initOwner(this.stage);
//
//            updateButton.setOnAction(event -> {
//                try {
//                    String ISBN = isbnField.getText();
//                    String attribute = attributeField.getText();
//                    String newValue = newValueField.getText();
//                    if (ISBN.isEmpty() || attribute.isEmpty() || newValue.isEmpty()) {
//                        System.out.println("Please fill in all fields!");
//                    } else {
//                        // bookService.modifyBook(ISBN, attribute, newValue);
//                        System.out.println("Book updated successfully!");
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    System.out.println("An error occurred. Please check the input values.");
//                }
//            });
//            backButton.setOnAction(event -> popupStage.close());
//            popupStage.showAndWait();
//            MainMenuController mainMenuController = new MainMenuController(this);
//            mainMenuController.registerEvent();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public Parent initializeModifyBookView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/libraryfxproject/modifyBook.fxml"));
            loader.setController(this);
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void showSuccessMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Message");
        alert.setHeaderText(null);
        alert.setContentText("Book modified successfully!");
        alert.show();
    }
}
