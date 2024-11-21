package org.example.libraryfxproject.Controller;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.libraryfxproject.Model.Book;
import org.example.libraryfxproject.Model.User;
import org.example.libraryfxproject.Service.*;
import org.example.libraryfxproject.Util.AlertDisplayer;

import javafx.scene.input.KeyCode;

import org.example.libraryfxproject.Service.UpdateService;
import javafx.stage.Stage;
import org.example.libraryfxproject.Service.SearchService;
import org.example.libraryfxproject.View.LoginView;
import org.example.libraryfxproject.View.MainMenuView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private ObservableList<Book> bookList = FXCollections.observableArrayList();
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
        mainMenuView.getModifyButton().setOnAction(event -> openModifyBookView());

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

    }

    public void registerForAddStudent(Stage stage) {
        mainMenuView.getCancelAddStudentButton().setOnAction(event -> {
            stage.close();
        });
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
        System.out.println("Total books: " + bookList.size());
        System.out.println("Total pages: " + pageCount);
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

    public void openModifyBookView() {
        Parent root = mainMenuView.initializeModifyBookView();
        if (root != null) {
            Stage popupStage = new Stage();
            popupStage.setTitle("Modify Book");
            popupStage.setScene(new Scene(root));
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.initOwner(this.mainMenuView.getStage());

            System.out.println(mainMenuView);
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
                        popupStage.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("An error occurred. Please check the input values.");
                }
            });
            mainMenuView.getBackButton().setOnAction(event -> popupStage.close());
            popupStage.showAndWait();
        }
    }

    private void setupTableColumns() {
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

    private void goToPage() {
        try {
            int pageNumber = Integer.parseInt(mainMenuView.getPageNumberField().getText()) - 1;
            if (pageNumber >= 0 && pageNumber < mainMenuView.getCatalogPagination().getPageCount()) {
                mainMenuView.getCatalogPagination().setCurrentPageIndex(pageNumber);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void registerForAddBook(Stage addBookStage) {
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
                    case 1 -> alertDisplayer.showErrorAlert("Invalid ISBN. It must be exactly 13 digits.", "Lỗi");
                    case 2 -> alertDisplayer.showErrorAlert("Invalid title. Special characters are not allowed.", "Lỗi");
                    case 3 -> alertDisplayer.showErrorAlert("Invalid price or quantity. Both must be positive numbers.", "Lỗi");
                    case 4 -> alertDisplayer.showErrorAlert("Invalid subject. Numbers are not allowed.", "Lỗi");
                    case 5 -> alertDisplayer.showErrorAlert("Invalid category. Numbers are not allowed.", "Lỗi");
                    case 6 -> alertDisplayer.showErrorAlert("Invalid URL. It must follow the format http:// or https://.", "Lỗi");
                    case 7 -> alertDisplayer.showErrorAlert("Invalid book type. Special characters are not allowed.", "Lỗi");
                    case 8 -> alertDisplayer.showErrorAlert("Invalid quantity. It must be a positive integer.", "Lỗi");
                    case 9 -> alertDisplayer.showErrorAlert("Invalid author. Special characters are not allowed.", "Lỗi");
                    default -> {
                        // Nếu tất cả đều hợp lệ, thêm sách vào database
                        bookService.insertBookToDatabase(title, author, pubdate, releaseDate, ISBN, price, subject, category, URL, bookType, quantity);
                        alertDisplayer.showConfirmationAlert("Thêm sách thành công", "Thông báo");
                        addBookStage.close();
                    }
                }
            } catch (Exception e) {
                alertDisplayer.showErrorAlert("Lỗi khi thêm sách", "Lỗi");
            }
        });
        // Handle Back button click
        mainMenuView.getBackButton().setOnAction(event -> {
            addBookStage.close();
        });
    }
}
