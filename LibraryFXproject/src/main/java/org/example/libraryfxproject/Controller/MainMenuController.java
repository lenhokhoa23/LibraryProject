package org.example.libraryfxproject.Controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.libraryfxproject.Export.ExporterFactory;
import org.example.libraryfxproject.Model.Book;
import org.example.libraryfxproject.Model.Cart;
import org.example.libraryfxproject.Model.User;
import org.example.libraryfxproject.Service.*;
import org.example.libraryfxproject.Util.AlertDisplayer;
import org.example.libraryfxproject.Util.Exception.ExportException;
import javafx.scene.input.KeyCode;
import org.example.libraryfxproject.Service.SearchService;
import org.example.libraryfxproject.View.LoginView;
import org.example.libraryfxproject.View.MainMenuView;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.List;

public class MainMenuController extends BaseController {
    private final MainMenuView mainMenuView;
    private final SearchService searchService ;
    private final BookService bookService;
    private final UpdateService updateService;
    private final UserService userService;
    private final CartService cartService;
    private ObservableList<Book> bookList = FXCollections.observableArrayList();
    private final ExportService exportService;
    private ObservableList<User> studentList = FXCollections.observableArrayList();
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
        this.cartService = CartService.getInstance();
        this.exportService = new ExportService(ExporterFactory.ExportType.EXCEL);
        loadTableData();
        initializePagination();
        contextMenuController = new ContextMenuController(mainMenuView.getCatalogTableView(), alertDisplayer);
    }
  
    public void registerEvent() {
        hideSuggestions();

        mainMenuView.getLogoutItem().setOnAction(event -> {
            Stage stage = (Stage) mainMenuView.getProfileButton().getScene().getWindow();
            stage.close();
            LoginView.openLoginView(new Stage());
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


        mainMenuView.getModifyButton().setOnAction(event -> {
            mainMenuView.initializeModifyBookView(this);
        });

        mainMenuView.getRefreshButton().setOnAction(event -> {
            isFilteredView = false;
            mainMenuView.getSearchToggle().setSelected(false);

            mainMenuView.getSearchToggle().getStyleClass().remove("view-toggle:selected"); // Xóa trạng thái đã chọn
            if (!mainMenuView.getSearchToggle().getStyleClass().contains("view-toggle")) {
                mainMenuView.getSearchToggle().getStyleClass().add("view-toggle"); // Đảm bảo thêm lại lớp mặc định
            }
            mainMenuView.getSearchCatalog().setText("");
            loadTableData();
            initializePagination();
        });
        mainMenuView.getSearchToggle().setOnAction(event -> {
            CatalogEvent();
        });

        mainMenuView.getGoToPageButton().setOnAction(event -> {
            goToPage();
        });

        setupTableColumns();

        mainMenuView.getAddStudentButton().setOnAction(event -> {
            mainMenuView.initializeAddStudentView(this);
        });

        mainMenuView.getAddItemButton().setOnAction(event -> {
            mainMenuView.initializeAddBookView(this);
        });

        mainMenuView.getStudentSearchButton().setOnAction(event -> {
            ObservableList<User> filteredUser = searchService.searchUserByUsername(mainMenuView.getStudentSearch().getText().trim());
            updateUserTableView(filteredUser);
        });
        mainMenuView.getRefreshStudentButton().setOnAction(event -> {
            loadTableData();
        });

        initializeLabel();
        initializePieChart();
        initializeBarChart();
        initializeTable();
        mainMenuView.getBorrowServiceButton().setOnAction(this::handleBorrowService);
        mainMenuView.getReturnServiceButton().setOnAction(this::handleReturnService);

        mainMenuView.getExportDataButton().setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Choose export location");
            File selectedDirectory = directoryChooser.showDialog(null);
            if (selectedDirectory != null) {
                Task<Void> exportTask = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        List<User> userList = userService.getUserDAO().findUserByComponentOfUserName("");
                        exportService.exportStudentData(userList, selectedDirectory.getAbsolutePath(),
                                new ExportService.ExportCallback() {
                                    @Override
                                    public void onSuccess(String filePath) {
                                        Platform.runLater(() -> {
                                            alertDisplayer.showInformationAlert("Export Success", "Data has been exported successfully to:" + filePath);
                                            mainMenuView.getProgressIndicator().setVisible(false);
                                        });
                                    }

                                    @Override
                                    public void onError(String errorMessage) {
                                        Platform.runLater(() -> {
                                            alertDisplayer.showErrorAlert("Export Error", "Failed to export data: " + errorMessage);
                                            mainMenuView.getProgressIndicator().setVisible(false);
                                        });

                                    }
                                });
                        return null;
                    }
                };
                exportTask.setOnFailed(e -> {
                    alertDisplayer.showErrorAlert("Export Error", "An unexpected error occur during export data.");
                    mainMenuView.getProgressIndicator().setVisible(false);
                });

                mainMenuView.getProgressIndicator().setVisible(true);
                new Thread(exportTask).start();
            }
        });
        setupContextMenuForStudent();
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

    public void registerForModifyBook(Stage stage) {
        mainMenuView.getUpdateButton().setOnAction(event -> {
            try {
                String ISBN = mainMenuView.getIsbnField().getText();
                String attribute = mainMenuView.getAttributeField().getText();
                String newValue = mainMenuView.getNewValueField().getText();
                if (ISBN.isEmpty() || attribute.isEmpty() || newValue.isEmpty()) {
                    showErrorMessage("Please fill in all fields!");
                } else {
                    bookService.modifyBook(ISBN, attribute, newValue);
                    showSuccessMessage("Book updated successfully!");
                    stage.close();
                }
            } catch (Exception e) {
                showErrorMessage("An error occurred. Please check the input values.");
            }
        });
        mainMenuView.getBackButton().setOnAction(event -> stage.close());mainMenuView.getUpdateButton().setOnAction(event -> {
            try {
                String ISBN = mainMenuView.getIsbnField().getText();
                String attribute = mainMenuView.getAttributeField().getText();
                String newValue = mainMenuView.getNewValueField().getText();
                if (ISBN.isEmpty() || attribute.isEmpty() || newValue.isEmpty()) {
                    showErrorMessage("Please fill in all fields!");
                } else {
                    bookService.modifyBook(ISBN, attribute, newValue);
                    showSuccessMessage("Book updated successfully!");
                    stage.close();
                }
            } catch (Exception e) {
                showErrorMessage("An error occurred. Please check the input values.");
            }
        });
        mainMenuView.getBackButton().setOnAction(event -> stage.close());
    }

    public void loadTableData() {
        bookList = FXCollections.observableArrayList(bookService.getBookDAO().getDataMap().values());
        updateBookTableView(bookList);
        studentList = FXCollections.observableArrayList(userService.getUserDAO().getDataMap().values());
        updateUserTableView(studentList);
    }

    private void initializePagination() {
        int pageCount = (int) Math.ceil((double) bookList.size() / ROWS_PER_PAGE);
        mainMenuView.getCatalogPagination().setPageCount(pageCount);
        mainMenuView.getCatalogPagination().setPageFactory(new Callback<Integer, Node>() {
            @Override
            public TableView<Book> call(Integer pageIndex) {
                updateBookTable(pageIndex);
                return mainMenuView.getCatalogTableView();
            }
        });
        VBox.setVgrow(mainMenuView.getCatalogPagination(), Priority.ALWAYS);
        mainMenuView.getCatalogPagination().setMaxHeight(Double.MAX_VALUE);

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

        mainMenuView.getCatalogPagination().setMinHeight(450); // Adjust as needed
        mainMenuView.getCatalogPagination().setPrefHeight(Region.USE_COMPUTED_SIZE);


        // Make sure pagination control uses available space
        VBox.setVgrow(mainMenuView.getCatalogPagination(), Priority.ALWAYS);
    }

    public void initializeLabel() {
        mainMenuView.getTotalBooksLabel().setText(updateService.updatedLabel(1).getText());
        mainMenuView.getActiveStudentsLabel().setText(updateService.updatedLabel(2).getText());
        mainMenuView.getBooksBorrowedLabel().setText(updateService.updatedLabel(3).getText());
        mainMenuView.getOverdueBooksLabel().setText(updateService.updatedLabel(4).getText());
    }

    public void initializePieChart() {
        updateService.updatePieChart(mainMenuView.getGenreCirculationChart());
        mainMenuView.getChartTitleLabel().setLayoutX(10);
        mainMenuView.getChartTitleLabel().setLayoutY(10);
        mainMenuView.getGenreCirculationChart().layoutXProperty().bind(
                mainMenuView.getChartPane().widthProperty().subtract(mainMenuView.getGenreCirculationChart().widthProperty()).divide(2)
        );
        mainMenuView.getGenreCirculationChart().layoutYProperty().bind(
                mainMenuView.getChartPane().heightProperty().subtract(mainMenuView.getGenreCirculationChart().heightProperty()).divide(2)
        );
    }

    public void initializeBarChart() {
        updateService.updateBarChart(mainMenuView.getGenreBorrowedBarChart());
        VBox.setVgrow(mainMenuView.getGenreBorrowedBarChart(), Priority.ALWAYS);
    }

    public void initializeTable() {
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

        mainMenuView.getBorrowDateColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
        mainMenuView.getCartIdColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
        mainMenuView.getUserNameColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));
        mainMenuView.getBookTitleColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(3)));
        mainMenuView.getIsbnColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(4)));
        mainMenuView.getDueDateColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(5)));
        updateService.populateTableView(mainMenuView.getBorrowHistoryTable(), 0);

        mainMenuView.getRefresh1().setOnAction(actionEvent -> {
            initializeTable();
        });
        mainMenuView.getSearch1().setOnAction(event -> {
            String studentId = mainMenuView.getStudentID1().getText();
            LocalDate borrowDate = mainMenuView.getStartDate1().getValue();
            LocalDate dueDate = mainMenuView.getEndDate1().getValue();

            int ID = 0;
            if (!studentId.isEmpty()) {
                ID = Integer.parseInt(studentId);
            }

            String brDate = null;
            String dDate = null;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            if (borrowDate != null) {
                brDate = borrowDate.format(formatter);
            }
            if (dueDate != null) {
                dDate = dueDate.format(formatter);
            }

            mainMenuView.getBorrowDateColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
            mainMenuView.getCartIdColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
            mainMenuView.getUserNameColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));
            mainMenuView.getBookTitleColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(3)));
            mainMenuView.getIsbnColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(4)));
            mainMenuView.getDueDateColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(5)));
            updateService.populateTableView(mainMenuView.getBorrowHistoryTable(), ID, brDate, dDate);
        });
    }

    public void handleBorrowService(Event event) {
        String studentId = mainMenuView.getBorrowStudentIdField1().getText();
        String isbn = mainMenuView.getBorrowISBNField1().getText();
        LocalDate dueDate = mainMenuView.getBorrowDueDatePicker1().getValue();

        if (studentId.isEmpty() || isbn.isEmpty() || dueDate == null) {
            showErrorMessage("Error, please fill in all fields before borrowing a book.");
            return;
        }
        if (!bookService.hasBookWithISBN(isbn)) {
            showErrorMessage("Không tìm thấy sách bạn muốn mượn!\nVui lòng thử lại");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            cartService.addCart(
                        Integer.parseInt(studentId),
                        LocalDate.now().format(formatter),
                        dueDate.format(formatter),
                        isbn
            );
            System.out.println("Cart added successfully!");
            showSuccessMessage("Borrow book successfully!!!!!!");
        } catch (NumberFormatException e) {
            showErrorMessage("Student ID must be a number.");
        } catch (Exception e) {
            showErrorMessage("An error occurred while adding the cart: " + e.getMessage());
        }
    }

    public void handleReturnService(Event event) {
        String studentId = mainMenuView.getReturnStudentIdField1().getText();
        String isbn = mainMenuView.getReturnISBNField1().getText();
        if (studentId.isEmpty() || isbn.isEmpty()) {
            showErrorMessage("Error, please fill in all fields before returning a book.");
            return;
        }
        if (!cartService.hasBookInCart(isbn, Integer.parseInt(studentId))) {
            showErrorMessage("Kho hàng của bạn không có sách này!");
            return;
        }
        try {
            cartService.deleteCart(isbn, Integer.parseInt(studentId));
            System.out.println("Cart removed successfully!");
            showSuccessMessage("Trả sách thành công");
        } catch (NumberFormatException e) {
            showErrorMessage("Error: Student ID must be a number.");
        } catch (Exception e) {
            showErrorMessage("An error occurred while removing the cart: " + e.getMessage());
        }
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

    public void updateBookTableView(ObservableList<Book> books) {
        mainMenuView.getCatalogTableView().getItems().clear();
        mainMenuView.getCatalogTableView().setItems(books);
    }

    public void updateUserTableView(ObservableList<User> users) {
        mainMenuView.getStudentTableView().getItems().clear();
        mainMenuView.getStudentTableView().setItems(users);
    }

    private void updateBookTable(int pageIndex) {
        int start = pageIndex * ROWS_PER_PAGE;
        int end = Math.min(start + ROWS_PER_PAGE, bookList.size());
        mainMenuView.getCatalogTableView().setItems(FXCollections.observableArrayList(bookList.subList(start, end)));
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
                updateBookTableView(filteredBooks);
                isFilteredView = true;
            }
        }
    }

    private void setupTableColumns () {
        mainMenuView.getItemIdColumn().setCellValueFactory(new PropertyValueFactory<>("no"));
        mainMenuView.getTitleColumn().setCellValueFactory(new PropertyValueFactory<>("title"));
        mainMenuView.getAuthorColumn().setCellValueFactory(new PropertyValueFactory<>("author"));
        mainMenuView.getSubjectColumn().setCellValueFactory(new PropertyValueFactory<>("subject"));
        mainMenuView.getBookTypeColumn().setCellValueFactory(new PropertyValueFactory<>("bookType"));
        mainMenuView.getQuantityColumn().setCellValueFactory(new PropertyValueFactory<>("quantity"));
        mainMenuView.getCatalogTableView().getColumns().clear();
        mainMenuView.getCatalogTableView().getColumns().addAll(
                mainMenuView.getItemIdColumn(),
                mainMenuView.getTitleColumn(),
                mainMenuView.getAuthorColumn(),
                mainMenuView.getSubjectColumn(),
                mainMenuView.getBookTypeColumn(),
                mainMenuView.getQuantityColumn());

        mainMenuView.getUsernameColumn().setCellValueFactory(new PropertyValueFactory<>("username"));
        mainMenuView.getNameColumn().setCellValueFactory(new PropertyValueFactory<>("name"));
        mainMenuView.getEmailColumn().setCellValueFactory(new PropertyValueFactory<>("email"));
        mainMenuView.getPhoneColumn().setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        mainMenuView.getBorrowedBookColumn().setCellValueFactory(new PropertyValueFactory<>("borrowedBooks"));
        mainMenuView.getMembershipTypeColumn().setCellValueFactory(new PropertyValueFactory<>("membershipType"));
        mainMenuView.getStudentTableView().getColumns().clear();
        mainMenuView.getStudentTableView().getColumns().addAll(
                mainMenuView.getUsernameColumn(),
                mainMenuView.getNameColumn(),
                mainMenuView.getEmailColumn(),
                mainMenuView.getPhoneColumn(),
                mainMenuView.getBorrowedBookColumn(),
                mainMenuView.getMembershipTypeColumn());
    }

    private void goToPage () {
        try {
            int pageNumber = Integer.parseInt(mainMenuView.getPageNumberField().getText()) - 1;
            if (pageNumber >= 0 && pageNumber < mainMenuView.getCatalogPagination().getPageCount()) {
                mainMenuView.getCatalogPagination().setCurrentPageIndex(pageNumber);
            } else {
                throw new RuntimeException();
            }
        } catch (NumberFormatException e) {
            alertDisplayer.showErrorAlert("Number Formatting Error","Page must be an positive integer.");
        } catch (RuntimeException e) {
            alertDisplayer.showErrorAlert("Exceed Number", "Page not found!");
        }
    }

    public void registerForAddBook (Stage addBookStage){
        // Handle Add Book button click
        mainMenuView.getAddBookButton().setOnAction(event -> {
            try {
                // Extract input values
                String title = mainMenuView.getTitle().getText();
                String author = mainMenuView.getAuthor().getText();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yy");

                // Xử lý ngày tháng
                LocalDate pubdateLocal = mainMenuView.getPubdate().getValue();
                String pubdate = pubdateLocal != null ? pubdateLocal.format(formatter) : null;

                LocalDate releaseDateLocal = mainMenuView.getReleaseDate().getValue();
                String releaseDate = releaseDateLocal != null ? releaseDateLocal.format(formatter) : null;

                String ISBN = mainMenuView.getISBN().getText();
                String price = mainMenuView.getPrice().getText();
                String subject = mainMenuView.getSubject().getText();
                String category = mainMenuView.getCategory().getText();
                String URL = mainMenuView.getURL().getText();
                String bookType = mainMenuView.getBookType().getText();
                String quantity = mainMenuView.getQuantity().getText();

                // Kiểm tra dữ liệu đầu vào bằng validateAddBookInput
                int validate = bookService.validateAddBookInput(title, author, pubdate, releaseDate,
                        ISBN, price, subject, category, URL, bookType, quantity);

                switch (validate) {
                    case 1 -> alertDisplayer.showErrorAlert("Error", "Invalid ISBN. It must be exactly 13 digits.");
                    case 2 -> alertDisplayer.showErrorAlert("Error", "Invalid title. Special characters are not allowed.");
                    case 3 -> alertDisplayer.showErrorAlert("Error", "Invalid price or quantity. Both must be positive numbers.");
                    case 4 -> alertDisplayer.showErrorAlert("Error", "Invalid subject. Numbers are not allowed.");
                    case 5 -> alertDisplayer.showErrorAlert("Error", "Invalid category. Numbers are not allowed.");
                    case 6 -> alertDisplayer.showErrorAlert("Error", "Invalid URL. It must follow the format http:// or https://.");
                    case 7 -> alertDisplayer.showErrorAlert("Error", "Invalid book type. Special characters are not allowed.");
                    case 8 -> alertDisplayer.showErrorAlert("Error", "Invalid quantity. It must be a positive integer.");
                    case 9 -> alertDisplayer.showErrorAlert("Error", "Invalid author. Special characters are not allowed.");
                    default -> {
                        // Nếu tất cả đều hợp lệ, thêm sách vào database
                        bookService.insertBookToDatabase(title, author, pubdate, releaseDate, ISBN, price, subject, category, URL, bookType, quantity);
                        alertDisplayer.showInformationAlert("Successful message!", "Add book successfully!");
                        addBookStage.close();
                    }
                }
            } catch (Exception e) {
                alertDisplayer.showErrorAlert( "Error", "An error occured when adding book!");
            }
        });
        // Handle Back button click
        mainMenuView.getBackButton().setOnAction(event -> {
            addBookStage.close();
        });
    }


    private void setupContextMenuForStudent() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete");
        contextMenu.getItems().addAll(deleteItem);
        deleteItem.setOnAction(event -> handleDeleteStudentAction());
        mainMenuView.getStudentTableView().setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                User selectedUser = mainMenuView.getStudentTableView().getSelectionModel().getSelectedItem();
                if (selectedUser != null) {
                    contextMenu.show(mainMenuView.getStudentTableView(), event.getScreenX(), event.getScreenY());
                } else {
                    contextMenu.hide();
                }
            }
        });
    }

    private void handleDeleteStudentAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("This action cannot be undone.");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                showConfirmation("Are you sure you want to delete this record?");
                alert.showAndWait().ifPresent(response2 -> {
                    if (response2 == ButtonType.OK) {
                        User selectedUser = mainMenuView.getStudentTableView().getSelectionModel().getSelectedItem();
                        if (selectedUser != null) {
                            userService.deleteUser(selectedUser.getUsername());
                            mainMenuView.getStudentTableView().getItems().remove(selectedUser);
                        }
                    }
                });
            }
        });
    }

}
