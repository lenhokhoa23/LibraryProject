package org.example.libraryfxproject.Controller;

import javafx.animation.PauseTransition;
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
import javafx.util.Duration;
import org.example.libraryfxproject.Export.ExporterFactory;
import org.example.libraryfxproject.Model.Book;
import org.example.libraryfxproject.Model.Cart;
import org.example.libraryfxproject.Model.User;
import org.example.libraryfxproject.Service.*;
import org.example.libraryfxproject.Util.AlertDisplayer;
import org.example.libraryfxproject.Util.Exception.ExportException;
import javafx.scene.input.KeyCode;
import org.example.libraryfxproject.Service.SearchService;
import org.example.libraryfxproject.View.BookDetailsView;
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
import java.util.concurrent.atomic.AtomicBoolean;

public class MainMenuController extends BaseController {
    private final MainMenuView mainMenuView;
    private final SearchService searchService ;
    private final BookService bookService;
    private final UpdateService updateService;
    private final UserService userService;
    private final CartService cartService;
    private final LoginService loginService;
    private ObservableList<Book> bookList;
    private final ExportService exportService;
    private ObservableList<User> studentList;
    private final ScheduledExecutorService scheduler;
    private ScheduledFuture<?> searchTask;
    private final int ROWS_PER_PAGE = 15;
    private boolean isFilteredView = false;
    private GoogleBooksService googleBooksService;

    public MainMenuController(MainMenuView mainMenuView, AlertDisplayer alertDisplayer) {
        super(alertDisplayer);
        this.mainMenuView = mainMenuView;
        this.searchService = SearchService.getInstance();
        this.updateService = UpdateService.getInstance();
        this.bookService = BookService.getInstance();
        this.userService = UserService.getInstance();
        this.cartService = CartService.getInstance();
        this.loginService = LoginService.getInstance();
        bookList = FXCollections.observableArrayList();
        scheduler = Executors.newSingleThreadScheduledExecutor();
        this.exportService = new ExportService(ExporterFactory.ExportType.EXCEL);
        try {
            this.googleBooksService = new GoogleBooksService();
        } catch (Exception e) {
            showErrorMessage("An error occured when working with Google API Books: " + e.getMessage());
        }
        loadTableData();
        initializePagination();
    }
  
    public void registerEvent() {
        mainMenuView.setLibrarian(loginService.findLibrarianByUsername(mainMenuView.getUsername()));

        mainMenuView.getSearchBookButton().setOnAction(e -> {
            performSearch(mainMenuView.getSearchField().getText());
        });

        mainMenuView.getModifyButton().setOnAction(event -> {
            mainMenuView.initializeModifyBookView(this);
        });

        mainMenuView.getRefreshButton().setOnAction(event -> {
            isFilteredView = false;
            mainMenuView.getSearchToggle().setSelected(false);
            mainMenuView.getSearchToggle().getStyleClass().remove("view-toggle:selected");
            if (!mainMenuView.getSearchToggle().getStyleClass().contains("view-toggle")) {
                mainMenuView.getSearchToggle().getStyleClass().add("view-toggle"); // Đảm bảo thêm lại lớp mặc định
            }
            mainMenuView.getSearchCatalog().setText("");
            loadTableData();
            initializePagination();
        });

        mainMenuView.getLogoutItem().setOnAction(event -> {
            Stage stage = (Stage) mainMenuView.getProfileButton().getScene().getWindow();
            stage.close();
            LoginView.openLoginView(new Stage());
        });

        hideSuggestions(mainMenuView.getSuggestions());
        hideSuggestions(mainMenuView.getSuggestions1());
        hideSuggestions(mainMenuView.getSuggestions2());
        handleUsingTextField(mainMenuView.getSearchField(), mainMenuView.getSuggestions(), 0);
        handleUsingTextField(mainMenuView.getBorrowISBNField1(), mainMenuView.getSuggestions1(), 1);
        handleUsingTextField(mainMenuView.getReturnISBNField1(), mainMenuView.getSuggestions2(), 2);

        mainMenuView.getSearchToggle().setOnAction(event -> {
            CatalogEvent();
        });

        mainMenuView.getGoToPageButton().setOnAction(event -> {
            goToPage();
        });

        setupTableColumns();

        mainMenuView.getAddStudentButton().setOnAction(event -> {
            mainMenuView.initializeAddStudentView(this);
            loadTableData();
            initializePagination();
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
            initializePagination();
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
        mainMenuView.getStudentPagination().setMaxHeight(Double.MAX_VALUE);

        mainMenuView.getSuggestions().setOnMousePressed(event -> mainMenuView.setSelecting(true));
        mainMenuView.getSuggestions1().setOnMousePressed(event -> mainMenuView.setSelecting1(true));
        mainMenuView.getSuggestions2().setOnMousePressed(event -> mainMenuView.setSelecting2(true));

        mainMenuView.getProfileButton().getScene().getRoot().setOnMouseClicked(event -> {
            if (!mainMenuView.getSearchField().isFocused() && !mainMenuView.getSuggestions().isFocused()) {
                hideSuggestions(mainMenuView.getSuggestions());
            }
            if (!mainMenuView.getBorrowISBNField1().isFocused() && !mainMenuView.getSuggestions1().isFocused()) {
                hideSuggestions(mainMenuView.getSuggestions1());
            }
            if (!mainMenuView.getReturnISBNField1().isFocused() && !mainMenuView.getSuggestions2().isFocused()) {
                hideSuggestions(mainMenuView.getSuggestions2());
            }
        });
    }

    private void handleUsingTextField(TextField textField, ListView<String> listView, int x) {
        textField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                hideSuggestions(listView);
            } else {
                scheduleSearch(textField, listView);
            }
        });

        listView.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 1) {
                if (x == 0) {
                    mainMenuView.setSelecting(false);
                } else if (x == 1) {
                    mainMenuView.setSelecting1(false);
                } else if (x == 2) {
                    mainMenuView.setSelecting2(false);
                }
                String selectedItem = listView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    textField.setText(selectedItem);
                    Platform.runLater(() -> {
                        textField.requestFocus();
                        textField.positionCaret(textField.getText().length());

                        // Delay ẩn suggestions :D
                        PauseTransition pause = new PauseTransition(Duration.millis(100));
                        pause.setOnFinished(e -> hideSuggestions(listView));
                        pause.play();
                    });
                }
            }
        });

        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                showSuggestionsIfNotEmpty(listView);
            } else {
                Platform.runLater(() -> {
                    if (!mainMenuView.isSelecting() && x == 0) {
                        hideSuggestions(listView);
                    }
                    if (!mainMenuView.isSelecting1() && x == 1) {
                        hideSuggestions(listView);
                    }
                    if (!mainMenuView.isSelecting2() && x == 2) {
                        hideSuggestions(listView);
                    }
                });
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
        String title = mainMenuView.getBorrowISBNField1().getText();
        String isbn = bookService.fetchISBNByTitle(title);
        LocalDate dueDate = mainMenuView.getBorrowDueDatePicker1().getValue();
        if (studentId.isEmpty() || isbn.isEmpty() || dueDate == null) {
            showErrorMessage("Vui lòng nhập đầy đủ thông tin!");
            return;
        }
        if (!userService.hasIDInUser(Integer.parseInt(studentId))) {
            showErrorMessage("Không tìm thấy ID người dùng!\nVui lòng thử lại");
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
        initializeLabel();
    }

    public void handleReturnService(Event event) {
        String studentId = mainMenuView.getReturnStudentIdField1().getText();
        String title = mainMenuView.getReturnISBNField1().getText();
        String isbn = bookService.fetchISBNByTitle(title);
        if (studentId.isEmpty() || isbn.isEmpty()) {
            showErrorMessage("Vui lòng điền đầy đủ thông tin.");
            return;
        }
        if (!userService.hasIDInUser(Integer.parseInt(studentId))) {
            showErrorMessage("Không tìm thấy ID người dùng!\nVui lòng thử lại");
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

    private void scheduleSearch(TextField textField, ListView<String> listView) {
        if (searchTask != null && !searchTask.isDone()) {
            searchTask.cancel(false);
        }
        searchTask = scheduler.schedule(() -> updateSuggestions(textField, listView), 50, TimeUnit.MILLISECONDS);
    }

    private void updateSuggestions(TextField textField, ListView<String> listView) {
        Platform.runLater(() -> {
            String query = textField.getText().toLowerCase();
            if (!query.trim().isEmpty()) {
                List<String> filteredBooks = searchService.searchBookByPrefix(query);
                ObservableList<String> suggestionList = FXCollections.observableArrayList(filteredBooks);
                listView.setItems(suggestionList);
                showSuggestionsIfNotEmpty(listView);
            } else {
                hideSuggestions(listView);
            }
        });
    }

    private void showSuggestionsIfNotEmpty(ListView<String> listView) {
        if (!listView.getItems().isEmpty()) {
            listView.setVisible(true);
        }
    }

    private void hideSuggestions(ListView<String> listView) {
        listView.setVisible(false);
    }

    private void performSearch(String selectedItem) {
        Book book = bookService.getBookByTitle(selectedItem);
        new BookDetailsView(book, mainMenuView.getUsername());
        hideSuggestions(mainMenuView.getSuggestions());
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

        mainMenuView.getQuickAddBookButton().setOnAction(event -> {
            Book book = null;
            try {
                book = googleBooksService.getBookByISBN(mainMenuView.getISBN().getText());
            } catch (Exception e) {
                showErrorMessage("An error occur when getting book by ISBN: " + e.getMessage());
            }
            if (book == null) {
                showErrorMessage("No book found for this ISBN");
            } else {
                mainMenuView.getTitle().setText(book.getTitle());
                mainMenuView.getAuthor().setText(book.getAuthor());
                mainMenuView.getPrice().setText(book.getPrice());
                mainMenuView.getSubject().setText(book.getSubject());
                mainMenuView.getCategory().setText(book.getCategory());
                mainMenuView.getURL().setText(book.getURL());
                mainMenuView.getBookType().setText(book.getBookType());
            }
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

