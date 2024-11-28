package org.example.libraryfxproject.Controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.libraryfxproject.Model.Book;
import org.example.libraryfxproject.Model.User;
import org.example.libraryfxproject.Service.*;
import org.example.libraryfxproject.Util.AlertDisplayer;
import org.example.libraryfxproject.View.LoginView;
import org.example.libraryfxproject.View.UserView;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class UserMenuController extends BaseController {
    private final UserView userView;
    private final SearchService searchService;
    private final UserService userService;
    private final CartService cartService;
    private final UpdateService updateService;
    private final BookService bookService;
    private final int ROWS_PER_PAGE = 15; // Số lượng records trên 1 page
    private ObservableList<Book> bookList = FXCollections.observableArrayList();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> searchTask;
    private boolean isFilteredView = false;

    public UserMenuController(UserView userView, AlertDisplayer alertDisplayer) {
        super(alertDisplayer);
        this.userView = userView;
        searchService = SearchService.getInstance();
        userService = UserService.getInstance();
        cartService = CartService.getInstance();
        updateService = UpdateService.getInstance();
        bookService = BookService.getInstance();
    }

    public void registerEvent() {
        hideSuggestions();
        userView.setUser(userService.findUserByUsername(userView.getUsername()));
        userView.getLogoutItem().setOnAction(event -> {
            Stage stage = (Stage) userView.getProfileButton().getScene().getWindow();
            stage.close();
            LoginView.openLoginView(new Stage());
        });

        userView.getSearchField().setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                performSearch();
            } else {
                scheduleSearch();
            }
        });

        userView.getSuggestions().setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 1) {
                userView.setSelecting(false);
                String selectedItem = userView.getSuggestions().getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    userView.getSearchField().setText(selectedItem);
                    hideSuggestions();
                    performSearch();
                }
            }
        });

        userView.getSearchField().focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                showSuggestionsIfNotEmpty();
            } else {
                Platform.runLater(() -> {
                    if (!userView.isSelecting()) {
                        hideSuggestions();
                    }
                });
            }
        });

        userView.getSearchField().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.trim().isEmpty()) {
                hideSuggestions();
            } else {
                scheduleSearch();
            }
        });

        userView.getSuggestions().setOnMousePressed(event -> userView.setSelecting(true));

        // Add a click listener to the root pane to hide suggestions when clicking outside
        userView.getProfileButton().getScene().getRoot().setOnMouseClicked(event -> {
            if (!userView.getSearchField().isFocused() && !userView.getSuggestions().isFocused()) {
                hideSuggestions();
            }
        });

        userView.getContact().setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://www.facebook.com/mgcfrog912"));
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });

        userView.getPrivacyPolicy().setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://docs.google.com/document/d/" +
                        "15gVj_RsRB_5sqHsp6ShJ6s2jfX5TEIXL1puhSqyoiqo/edit?usp=sharing"));
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });

        userView.getAboutUs().setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://docs.google.com/document/" +
                        "d/18zGVMysIS5GG8MEduQ2WVXLQaR6Yjocatv2cyxcHaWY/edit?usp=sharing"));
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });

        userView.getTermsOfService().setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://docs.google.com/document/" +
                        "d/1e2l3OQCMfy7gAhbwsTzPusMx8V2__OlxFhmehIJm0Zw/edit?tab=t.0"));
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });

        userView.getClub1().setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://www.facebook.com/groups/1138721139638235"));
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });

        userView.getClub2().setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://www.facebook.com/groups/1953714107984908"));
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });

        userView.getClub3().setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://www.facebook.com/groups/1928903884018373"));
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });

        userView.getClub4().setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://www.facebook.com/groups/theabcbookclub"));
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });

        userView.getClub5().setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://www.facebook.com/groups/152211783175655"));
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });

        userView.getRefreshButton().setOnAction(event -> {
            isFilteredView = false;
            userView.getSearchToggle().setSelected(false);

            userView.getSearchToggle().getStyleClass().remove("view-toggle:selected"); // Xóa trạng thái đã chọn
            if (!userView.getSearchToggle().getStyleClass().contains("view-toggle")) {
                userView.getSearchToggle().getStyleClass().add("view-toggle"); // Đảm bảo thêm lại lớp mặc định
            }
            userView.getSearchCatalog().setText("");
            loadTableData();
            initializePagination();
        });
        userView.getSearchToggle().setOnAction(event -> {
            CatalogEvent();
        });

        userView.getUserBorrowButton().setOnAction(this::handleBorrowService);
        userView.getUserReturnButton().setOnAction(this::handleReturnService);
        setupTableColumns();
        loadTableData();
        initializeTable();
        initializePagination();
    }

    public void initializeTable() {
        userView.getBorrowedUserDateColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
        userView.getBorrowedIDColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
        userView.getBorrowedUsernameColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));
        userView.getBorrowedTitleColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(3)));
        userView.getBorrowedISBNColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(4)));
        userView.getBorrowedDueDateColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(5)));
        updateService.populateTableViewByCartId(userView.getUserBorrowedBooksTable(), userView.getUser().getUserID());
        userView.getRefreshBorrowedBooksTable().setOnAction(event -> {
            updateService.populateTableViewByCartId(userView.getUserBorrowedBooksTable(), userView.getUser().getUserID());
        });
    }

    public void handleBorrowService(Event event) {
        int studentId = userView.getUser().getUserID();
        String isbn = userView.getUserBorrowISBN().getText();
        LocalDate dueDate = userView.getUserBorrowReturnDate().getValue();

        if (isbn.isEmpty() || dueDate == null) {
            System.out.println("Error, please fill in all fields before borrowing a book.");
            return;
        }
        if (!bookService.hasBookWithISBN(isbn)) {
            showErrorMessage("Không tìm thấy sách bạn muốn mượn!\nVui lòng thử lại");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            cartService.addCart(
                    studentId,
                    LocalDate.now().format(formatter),
                    dueDate.format(formatter),
                    isbn
            );
            System.out.println("Cart added successfully!");
            showSuccessMessage("Mượn sách thành công");
        } catch (Exception e) {
            System.out.println("An error occurred while adding the cart: " + e.getMessage());
        }
    }

    public void handleReturnService(Event event) {
        int studentId = userView.getUser().getUserID();
        String isbn = userView.getUserReturnISBN().getText();
        if (isbn.isEmpty()) {
            System.out.println("Error, please fill in all fields before returning a book.");
            return;
        }
        if (!cartService.hasBookInCart(isbn, studentId)) {
            showErrorMessage("Kho hàng của bạn không có sách này!");
            return;
        }
        try {
            cartService.deleteCart(isbn, studentId);
            System.out.println("Cart removed successfully!");
            showSuccessMessage("Trả sách thành công");
        } catch (Exception e) {
            System.out.println("An error occurred while removing the cart: " + e.getMessage());
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
            String query = userView.getSearchField().getText().toLowerCase();
            if (!query.trim().isEmpty()) {
                List<String> filteredBooks = searchService.searchBookByPrefix(query);
                ObservableList<String> suggestionList = FXCollections.observableArrayList(filteredBooks);
                userView.getSuggestions().setItems(suggestionList);
                showSuggestionsIfNotEmpty();
            } else {
                hideSuggestions();
            }
        });
    }

    private void showSuggestionsIfNotEmpty() {
        if (!userView.getSuggestions().getItems().isEmpty()) {
            userView.getSuggestions().setVisible(true);
        }
    }

    private void hideSuggestions() {
        userView.getSuggestions().setVisible(false);
    }

    private void performSearch() {
        // Implement your search logic here

        hideSuggestions();
    }

    private void initializePagination() {
        int pageCount = (int) Math.ceil((double) bookList.size() / ROWS_PER_PAGE);
        userView.getCatalogPagination().setPageCount(pageCount);
        userView.getCatalogPagination().setPageFactory(new Callback<Integer, Node>() {
            @Override
            public TableView<Book> call(Integer pageIndex) {
                updateBookTable(pageIndex);
                return userView.getCatalogTableView();
            }
        });
        VBox.setVgrow(userView.getCatalogPagination(), Priority.ALWAYS);
        userView.getCatalogPagination().setMaxHeight(Double.MAX_VALUE);
    }

    public void updateBookTableView(ObservableList<Book> books) {
        userView.getCatalogTableView().getItems().clear();
        userView.getCatalogTableView().setItems(books);
    }

    private void updateBookTable(int pageIndex) {
        int start = pageIndex * ROWS_PER_PAGE;
        int end = Math.min(start + ROWS_PER_PAGE, bookList.size());
        userView.getCatalogTableView().setItems(FXCollections.observableArrayList(bookList.subList(start, end)));
    }

    private void setupTableColumns () {
        userView.getItemIdColumn().setCellValueFactory(new PropertyValueFactory<>("no"));
        userView.getTitleColumn().setCellValueFactory(new PropertyValueFactory<>("title"));
        userView.getAuthorColumn().setCellValueFactory(new PropertyValueFactory<>("author"));
        userView.getSubjectColumn().setCellValueFactory(new PropertyValueFactory<>("subject"));
        userView.getBookTypeColumn().setCellValueFactory(new PropertyValueFactory<>("bookType"));
        userView.getQuantityColumn().setCellValueFactory(new PropertyValueFactory<>("quantity"));
        userView.getCatalogTableView().getColumns().clear();
        userView.getCatalogTableView().getColumns().addAll(
                userView.getItemIdColumn(),
                userView.getTitleColumn(),
                userView.getAuthorColumn(),
                userView.getSubjectColumn(),
                userView.getBookTypeColumn(),
                userView.getQuantityColumn());

    }

    public void loadTableData() {
        bookList = FXCollections.observableArrayList(bookService.getBookDAO().getDataMap().values());
        updateBookTableView(bookList);
    }

    public void CatalogEvent() {
        if (isFilteredView) {
            loadTableData();
            isFilteredView = false;
        } else {
            String searchType = userView.getFilterComboBox().getValue();
            String searchText = userView.getSearchCatalog().getText().toUpperCase();
            ObservableList<Book> filteredBooks = FXCollections.observableArrayList();
            if (searchType != null && !searchText.isEmpty()) {
                filteredBooks = searchService.searchBookByAttribute(searchType.toLowerCase(), searchText);
                updateBookTableView(filteredBooks);
                isFilteredView = true;
            }
        }
    }

    public void shutdown() {
        scheduler.shutdownNow();
    }
}
