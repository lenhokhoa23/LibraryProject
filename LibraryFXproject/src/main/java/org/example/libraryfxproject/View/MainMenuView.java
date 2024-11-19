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

import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.libraryfxproject.Controller.MainMenuController;
import org.example.libraryfxproject.Dao.BookDAO;
import org.example.libraryfxproject.Model.Book;
import org.example.libraryfxproject.Model.User;
import org.example.libraryfxproject.Util.AlertDisplayer;
import org.example.libraryfxproject.Util.JavaFXAlertDisplayer;

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
    private BarChart<String, Number> genreBorrowedBarChart;

    @FXML
    private Label chartTitleLabel;

    @FXML
    private TableView<User> studentTableView;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableColumn<User, String> nameColumn;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private TableColumn<User, String> phoneColumn;

    @FXML
    private TableColumn<User, String> borrowedBookColumn;

    @FXML
    private TableColumn<User, String> membershipTypeColumn;

    @FXML
    private Pagination studentPagination;

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
    private TextField nameField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private Button addStudent;
    @FXML
    private Button cancelAddStudentButton;
    @FXML
    private Button addStudentButton;

    private AlertDisplayer alertDisplayer;

    public TableView<Book> getCatalogTableView() {
        return catalogTableView;
    }

    public Pagination getCatalogPagination() {
        return catalogPagination; // Add this method
    }


    public MainMenuView(Stage stage) {
        this.stage = stage;
        initializeMainMenuView();
        alertDisplayer = new JavaFXAlertDisplayer();
    }

    public Button getAddStudentButton() {
        return addStudentButton;
    }

    public TextField getNameField() {
        return nameField;
    }

    public TextField getUsernameField() {
        return usernameField;
    }

    public TextField getEmailField() {
        return emailField;
    }

    public TextField getPhoneField() {
        return phoneField;
    }

    public Button getAddStudent() {
        return addStudent;
    }

    public Button getCancelAddStudentButton() {
        return cancelAddStudentButton;
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

    public Label getChartTitleLabel() {
        return chartTitleLabel;
    }

    public void setChartTitleLabel(Label chartTitleLabel) {
        this.chartTitleLabel = chartTitleLabel;
    }

    public TableView<User> getStudentTableView() {
        return studentTableView;
    }

    public void setStudentTableView(TableView<User> studentTableView) {
        this.studentTableView = studentTableView;
    }

    public TableColumn<User, String> getUsernameColumn() {
        return usernameColumn;
    }

    public void setUsernameColumn(TableColumn<User, String> usernameColumn) {
        this.usernameColumn = usernameColumn;
    }

    public TableColumn<User, String> getNameColumn() {
        return nameColumn;
    }

    public void setNameColumn(TableColumn<User, String> nameColumn) {
        this.nameColumn = nameColumn;
    }

    public TableColumn<User, String> getEmailColumn() {
        return emailColumn;
    }

    public void setEmailColumn(TableColumn<User, String> emailColumn) {
        this.emailColumn = emailColumn;
    }

    public TableColumn<User, String> getPhoneColumn() {
        return phoneColumn;
    }

    public void setPhoneColumn(TableColumn<User, String> phoneColumn) {
        this.phoneColumn = phoneColumn;
    }

    public TableColumn<User, String> getBorrowedBookColumn() {
        return borrowedBookColumn;
    }

    public void setBorrowedBookColumn(TableColumn<User, String> borrowedBookColumn) {
        this.borrowedBookColumn = borrowedBookColumn;
    }

    public TableColumn<User, String> getMembershipTypeColumn() {
        return membershipTypeColumn;
    }

    public void setMembershipTypeColumn(TableColumn<User, String> membershipTypeColumn) {
        this.membershipTypeColumn = membershipTypeColumn;
    }

    public Pagination getStudentPagination() {
        return studentPagination;
    }

    public void setStudentPagination(Pagination studentPagination) {
        this.studentPagination = studentPagination;
    }

    public void initializeMainMenuView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/libraryfxproject/MainMenu.fxml"));
        fxmlLoader.setController(this);
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
            MainMenuController mainMenuController = new MainMenuController(this, alertDisplayer);
            mainMenuController.registerEvent();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void initializeAddStudentView(MainMenuController mainMenuController) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/libraryfxproject/addStudent.fxml"));
        fxmlLoader.setController(this);
        try {
            Parent addStudentParent = fxmlLoader.load();
            Scene addStudentScene = new Scene(addStudentParent);
            Stage addStudentStage = new Stage();
            addStudentStage.setScene(addStudentScene);
            addStudentStage.initModality(Modality.APPLICATION_MODAL);
            addStudentStage.initOwner(stage);
            addStudentStage.show();
            mainMenuController.registerForAddStudent(addStudentStage);
        } catch (IOException e) {
            throw new RuntimeException(e);
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



}
