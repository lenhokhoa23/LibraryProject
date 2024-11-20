package org.example.libraryfxproject.Controller;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.libraryfxproject.Model.Book;
import org.example.libraryfxproject.Model.Cart;
import org.example.libraryfxproject.Model.User;
import org.example.libraryfxproject.Service.*;
import org.example.libraryfxproject.Util.AlertDisplayer;
import org.example.libraryfxproject.Util.Exception.ExportException;
import org.example.libraryfxproject.Util.ExporterFactory;
import org.example.libraryfxproject.View.AddBookView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.libraryfxproject.Service.SearchService;
import org.example.libraryfxproject.View.LoginView;
import org.example.libraryfxproject.View.MainMenuView;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

public class MainMenuController extends BaseController {
    private final MainMenuView mainMenuView;
    private final SearchService searchService ;
    private final BookService bookService;
    private final UpdateService updateService;
    private final UserService userService;
    private final ExportService exportService;
    private ObservableList<Book> observableBooks;
    private ObservableList<User> studentList = FXCollections.observableArrayList();
    private ObservableList<Cart> borrowHistoryData;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> searchTask;
    private final int ROWS_PER_PAGE = 15; // Số lượng records trên 1 page
    private boolean isFilteredView = false;
    private final ContextMenuController contextMenuController;

    public MainMenuController(MainMenuView mainMenuView, AlertDisplayer alertDisplayer) {
        super(alertDisplayer);
        this.mainMenuView = mainMenuView;
        this.searchService = SearchService.getInstance();
        this.updateService = UpdateService.getInstance();
        this.bookService = BookService.getInstance();
        this.userService = UserService.getInstance();
        this.exportService = new ExportService(ExporterFactory.ExportType.EXCEL);
        this.observableBooks = BookService.getInstance().getAllBooks();
        loadTableData();
        initializePagination();
        contextMenuController = new ContextMenuController(mainMenuView.getCatalogTableView());
    }
  
    public void registerEvent() {
        hideSuggestions();

        mainMenuView.getLogoutItem().setOnAction(event -> {
            LoginView.openLoginView((Stage) mainMenuView.getProfileButton().getScene().getWindow());
        });

        mainMenuView.getSearchField().setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                performSearch();
            } else {
                scheduleSearch();
            }
        });

        mainMenuView.getSuggestions().setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 1) {
                mainMenuView.setSelecting(false);
                String selectedItem = mainMenuView.getSuggestions().getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    mainMenuView.getSearchField().setText(selectedItem);
                    hideSuggestions();
                    performSearch();
                }
            }
        });

        mainMenuView.getSearchField().focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                showSuggestionsIfNotEmpty();
            } else {
                Platform.runLater(() -> {
                    if (!mainMenuView.isSelecting()) {
                        hideSuggestions();
                    }
                });
            }
        });

        mainMenuView.getSearchField().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.trim().isEmpty()) {
                hideSuggestions();
            } else {
                scheduleSearch();
            }
        });
        mainMenuView.getAddItemButton().setOnAction(this::openAddBookView);

        // mainMenuView.getModifyButton().setOnAction(event -> openModifyBookView());
        mainMenuView.getModifyButton().setOnAction(event -> {
            mainMenuView.initializeModifyBookView(this);
        });

        mainMenuView.getRefreshButton().setOnAction(event -> {
            isFilteredView = false;
            loadTableData();
            initializePagination();
        });
        mainMenuView.getSearchToggle().setOnAction(event -> {
            CatalogEvent();
        });
        setupTableColumns();

        mainMenuView.getAddStudentButton().setOnAction(event -> {
            mainMenuView.initializeAddStudentView(this);
        });
        mainMenuView.getStudentSearchButton().setOnAction(event -> {
            ObservableList<User> filteredUser = searchService.searchUserByUsername(mainMenuView.getStudentSearch().getText());
            updateUserTableView(filteredUser);
        });
        mainMenuView.getRefreshStudentButton().setOnAction(event -> {
            loadTableData();
        });
        mainMenuView.getExportDataButton().setOnAction(event -> {
            try {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Choose export location");
                File selectedDirectory = directoryChooser.showDialog(null);
                if (selectedDirectory != null) {
                    List<User> userList = userService.getUserDAO().findUserByComponentOfUserName("");
                    exportService.exportStudentData(userList, selectedDirectory.getAbsolutePath());
                    alertDisplayer.showInformationAlert("Export Success", "Data has been exported successfully!");
                }
            } catch (ExportException exportException) {
                alertDisplayer.showErrorAlert("Export Error", "Failed to export data " + exportException.getMessage());
            }
        });
    }

    public void registerForAddStudent(Stage addStudentStage) {
        mainMenuView.getCancelAddStudentButton().setOnAction(event -> {
            addStudentStage.close();
        });
        mainMenuView.getAddStudent().setOnAction(event -> {
            if (showConfirmation("A new User will be added, are you sure?")) {
                userService.getUserDAO().saveUserToDatabase(mainMenuView.getNameField().getText(),
                        mainMenuView.getEmailField().getText(), mainMenuView.getPhoneField().getText(),
                        mainMenuView.getUsernameField().getText(),
                        mainMenuView.getPasswordField().getText(),
                        mainMenuView.getMembershipTypeComboBox().getValue());
                showSuccessMessage("Add user successfully!!");
                addStudentStage.close();
            }
        });
    }
//    String username, String name, String email, String phoneNumber, int id,
//    int borrowedBooks, String membershipType

    public void registerForModifyBook(Stage stage) {
        mainMenuView.getUpdateButton().setOnAction(event -> {
            try {
                String ISBN = mainMenuView.getIsbnField().getText();
                String attribute = mainMenuView.getAttributeField().getText();
                String newValue = mainMenuView.getNewValueField().getText();
                if (ISBN.isEmpty() || attribute.isEmpty() || newValue.isEmpty()) {
                    System.out.println("Please fill in all fields!");
                } else {
                    bookService.modifyBook(ISBN, attribute, newValue);
                    System.out.println("Book updated successfully!");
                    stage.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("An error occurred. Please check the input values.");
            }
        });
        mainMenuView.getBackButton().setOnAction(event -> stage.close());mainMenuView.getUpdateButton().setOnAction(event -> {
            try {
                String ISBN = mainMenuView.getIsbnField().getText();
                String attribute = mainMenuView.getAttributeField().getText();
                String newValue = mainMenuView.getNewValueField().getText();
                if (ISBN.isEmpty() || attribute.isEmpty() || newValue.isEmpty()) {
                    System.out.println("Please fill in all fields!");
                } else {
                    bookService.modifyBook(ISBN, attribute, newValue);
                    System.out.println("Book updated successfully!");
                    stage.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("An error occurred. Please check the input values.");
            }
        });
        mainMenuView.getBackButton().setOnAction(event -> stage.close());
    }

    public void loadTableData() {
        updateTableView(getPageData(0)); // Load the first page initially
        studentList = FXCollections.observableArrayList(userService.getUserDAO().getDataMap().values());
        updateUserTableView(studentList);
    }

    private void initializePagination() {
        int pageCount = (int) Math.ceil((double) observableBooks.size() / ROWS_PER_PAGE);
        mainMenuView.getCatalogPagination().setPageCount(pageCount);
        System.out.println("Total books: " + observableBooks.size());
        System.out.println("Total pages: " + pageCount);
        mainMenuView.getCatalogPagination().setPageFactory(this::createPage);

        int totalPages = (int) Math.ceil((double) studentList.size() / ROWS_PER_PAGE);
        mainMenuView.getStudentPagination().setPageCount(totalPages);

        mainMenuView.getStudentPagination().setPageFactory(new Callback<Integer, Node>() {
            @Override
            public TableView<User> call(Integer pageIndex) {
                updateTable(pageIndex);
                return mainMenuView.getStudentTableView();
            }
        });
        VBox.setVgrow(mainMenuView.getStudentPagination(), Priority.ALWAYS);
        mainMenuView.getStudentPagination().setMaxHeight(Double.MAX_VALUE);
        mainMenuView.getSuggestions().setOnMousePressed(event -> mainMenuView.setSelecting(true));

        // Add a click listener to the root pane to hide suggestions when clicking outside
        mainMenuView.getProfileButton().getScene().getRoot().setOnMouseClicked(event -> {
            if (!mainMenuView.getSearchField().isFocused() && !mainMenuView.getSuggestions().isFocused()) {
                hideSuggestions();
            }
        });
      
        mainMenuView.getTotalBooksLabel().setText(updateService.updatedLabel(1).getText());
        mainMenuView.getActiveStudentsLabel().setText(updateService.updatedLabel(2).getText());
        mainMenuView.getBooksBorrowedLabel().setText(updateService.updatedLabel(3).getText());
        mainMenuView.getOverdueBooksLabel().setText(updateService.updatedLabel(4).getText());

        updateService.updatePieChart(mainMenuView.getGenreCirculationChart());
        mainMenuView.getChartTitleLabel().setLayoutX(10);
        mainMenuView.getChartTitleLabel().setLayoutY(10);

        mainMenuView.getGenreCirculationChart().layoutXProperty().bind(
                mainMenuView.getChartPane().widthProperty().subtract(mainMenuView.getGenreCirculationChart().widthProperty()).divide(2)
        );
        mainMenuView.getGenreCirculationChart().layoutYProperty().bind(
                mainMenuView.getChartPane().heightProperty().subtract(mainMenuView.getGenreCirculationChart().heightProperty()).divide(2)
        );

        updateService.updateBarChart(mainMenuView.getGenreBorrowedBarChart());
        VBox.setVgrow(mainMenuView.getGenreBorrowedBarChart(), Priority.ALWAYS);

        mainMenuView.getActivityTimeColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
        mainMenuView.getActivityUserIDColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
        mainMenuView.getActivityUsernameColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));
        mainMenuView.getActivityBookTitleColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(3)));
        mainMenuView.getActivityISBNColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(4)));
        mainMenuView.getActivityDueColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(5)));
        updateService.populateTableView(mainMenuView.getRecentActivitiesTable(), 17);
        mainMenuView.getViewAllButton().setOnAction(event -> {
            updateService.populateTableView(mainMenuView.getRecentActivitiesTable(), 0);
        });
        mainMenuView.getCatalogPagination().setMinHeight(450); // Adjust as needed
        mainMenuView.getCatalogPagination().setPrefHeight(Region.USE_COMPUTED_SIZE);

        // Set page factory
        mainMenuView.getCatalogPagination().setPageFactory(this::createPage);

        // Make sure pagination control uses available space
        VBox.setVgrow(mainMenuView.getCatalogPagination(), Priority.ALWAYS);
        mainMenuView.getCartIdColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
        mainMenuView.getBookTitleColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(3)));
        mainMenuView.getIsbnColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(4)));
        mainMenuView.getBorrowDateColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
        mainMenuView.getDueDateColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(5)));
        mainMenuView.getUserNameColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));
        updateService.populateTableView(mainMenuView.getBorrowHistoryTable(), 50);
    }

    private void scheduleSearch() {
        if (searchTask != null && !searchTask.isDone()) {
            searchTask.cancel(false);
        }

        // Đặt lại searchTask với tác vụ mới
        searchTask = scheduler.schedule(this::updateSuggestions, 50, TimeUnit.MILLISECONDS);
    }

    private void updateSuggestions() {
        Platform.runLater(() -> {
            String query = mainMenuView.getSearchField().getText().toLowerCase();
            if (!query.trim().isEmpty()) {
                List<String> filteredBooks = searchService.searchBookByPrefix(query);
                ObservableList<String> suggestionList = FXCollections.observableArrayList(filteredBooks);
                mainMenuView.getSuggestions().setItems(suggestionList);
                showSuggestionsIfNotEmpty();
            } else {
                hideSuggestions();
            }
        });
    }

    private void showSuggestionsIfNotEmpty() {
        if (!mainMenuView.getSuggestions().getItems().isEmpty()) {
            mainMenuView.getSuggestions().setVisible(true);
        }
    }

    private void hideSuggestions() {
        mainMenuView.getSuggestions().setVisible(false);
    }

    private void performSearch() {
        // Implement your search logic here

        hideSuggestions();
    }

    public void shutdown() {
        scheduler.shutdownNow();
    }

    private Node createPage(int pageIndex) {
        // Get data for current page
        ObservableList<Book> currentPageBooks = getPageData(pageIndex);

        // Get the table view
        TableView<Book> catalogTableView = mainMenuView.getCatalogTableView();

        // Clear and set new items
        catalogTableView.getItems().clear();
        catalogTableView.setItems(currentPageBooks);

        // Reset scroll position
        catalogTableView.scrollTo(0);

        // Create container with proper layout constraints
        VBox pageContainer = new VBox();
        pageContainer.setFillWidth(true);
        VBox.setVgrow(catalogTableView, Priority.ALWAYS);

        // Set minimum height for table view
        catalogTableView.setMinHeight(400); // Adjust this value based on your needs
        catalogTableView.setPrefHeight(Region.USE_COMPUTED_SIZE);

        // Add table to container
        pageContainer.getChildren().add(catalogTableView);

        return pageContainer;
    }

    private ObservableList<Book> getPageData(int pageIndex) {
        int start = pageIndex * ROWS_PER_PAGE;
        int end = Math.min(start + ROWS_PER_PAGE, observableBooks.size());
        return FXCollections.observableArrayList(observableBooks.subList(start, end));
    }

    public void updateTableView(ObservableList<Book> books) {
        mainMenuView.getCatalogTableView().getItems().clear();
        mainMenuView.getCatalogTableView().setItems(books);
    }

    public void updateUserTableView(ObservableList<User> users) {
        mainMenuView.getStudentTableView().getItems().clear();
        mainMenuView.getStudentTableView().setItems(users);
    }

    private void updateTable(int pageIndex) {
        int start = pageIndex * ROWS_PER_PAGE;
        int end = Math.min(start + ROWS_PER_PAGE, studentList.size());
        mainMenuView.getStudentTableView().setItems(FXCollections.observableArrayList(studentList.subList(start, end)));
    }

    public void CatalogEvent() {
        if (isFilteredView) {
            loadTableData();
            isFilteredView = false;
        } else {
            String searchType = mainMenuView.getFilterComboBox().getValue();
            String searchText = mainMenuView.getSearchCatalog().getText().toUpperCase();
            ObservableList<Book> filteredBooks = FXCollections.observableArrayList();
            if (searchType != null && !searchText.isEmpty()) {
                filteredBooks = searchService.searchBookByAttribute(searchType.toLowerCase(), searchText);
                updateTableView(filteredBooks);
                isFilteredView = true;
            }
        }
    }

    public void openAddBookView(ActionEvent event) {
        new AddBookView(mainMenuView.getStage());
    }

    private void setupTableColumns() {
        mainMenuView.getUsernameColumn().setCellValueFactory(new PropertyValueFactory<>("username"));
        mainMenuView.getNameColumn().setCellValueFactory(new PropertyValueFactory<>("name"));
        mainMenuView.getEmailColumn().setCellValueFactory(new PropertyValueFactory<>("email"));
        mainMenuView.getPhoneColumn().setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        mainMenuView.getBorrowedBookColumn().setCellValueFactory(new PropertyValueFactory<>("borrowedBooks"));
        mainMenuView.getMembershipTypeColumn().setCellValueFactory(new PropertyValueFactory<>("membershipType"));
        mainMenuView.getStudentTableView().getColumns().clear();
        mainMenuView.getStudentTableView().getColumns().addAll(mainMenuView.getUsernameColumn(),
                mainMenuView.getNameColumn(),
                mainMenuView.getEmailColumn(),
                mainMenuView.getPhoneColumn(),
                mainMenuView.getBorrowedBookColumn(),
                mainMenuView.getMembershipTypeColumn());
    }

}
