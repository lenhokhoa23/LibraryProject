package org.example.libraryfxproject.Controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
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
    private final int ROWS_PER_PAGE = 15;
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
        userView.setUser(userService.findUserByUsername(userView.getUsername()));
        userView.getWelcomeMessage().setText("Welcome back, " + userView.getUser().getName() + "!");
        hideSuggestions(userView.getSuggestions());
        hideSuggestions(userView.getSuggestions1());
        hideSuggestions(userView.getSuggestions2());

        userView.getLogoutItem().setOnAction(event -> {
            Stage stage = (Stage) userView.getProfileButton().getScene().getWindow();
            stage.close();
            LoginView.openLoginView(new Stage());
        });
        
        handleUsingTextField(userView.getSearchField(), userView.getSuggestions(), 0);
        handleUsingTextField(userView.getUserBorrowISBN(), userView.getSuggestions1(), 1);
        handleUsingTextField(userView.getUserReturnISBN(), userView.getSuggestions2(), 2);

        userView.getSuggestions().setOnMousePressed(event -> userView.setSelecting(true));
        userView.getSuggestions1().setOnMousePressed(event -> userView.setSelecting1(true));
        userView.getSuggestions2().setOnMousePressed(event -> userView.setSelecting2(true));

        // Add a click listener to the root pane to hide suggestions when clicking outside
        userView.getProfileButton().getScene().getRoot().setOnMouseClicked(event -> {
            if (!userView.getSearchField().isFocused() && !userView.getSuggestions().isFocused()) {
                hideSuggestions(userView.getSuggestions());
            }
            if (!userView.getUserBorrowISBN().isFocused() && !userView.getSuggestions1().isFocused()) {
                hideSuggestions(userView.getSuggestions1());
            }
            if (!userView.getUserReturnISBN().isFocused() && !userView.getSuggestions2().isFocused()) {
                hideSuggestions(userView.getSuggestions2());
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

        userView.getBorrowBook().setOnAction(event -> {
            TabPane tabPane = userView.getTabPane();
            tabPane.getSelectionModel().select(5);
        });

        userView.getReturnBook().setOnAction(event -> {
            TabPane tabPane = userView.getTabPane();
            tabPane.getSelectionModel().select(5);
        });

        userView.getStudentProfileDetails().setOnAction(event -> {
            userView.initializeStudentDetailsView(this);
        });

        userView.getUserBorrowButton().setOnAction(this::handleBorrowService);
        userView.getUserReturnButton().setOnAction(this::handleReturnService);

        setupTableColumns();
        loadTableData();
        initializeTable();
        initializePagination();
        initializeDueTable();
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
        String title = userView.getUserBorrowISBN().getText();
        String isbn = bookService.fetchISBNByTitle(title);
        LocalDate dueDate = userView.getUserBorrowReturnDate().getValue();
        if (isbn.isEmpty() || dueDate == null) {
            showErrorMessage("Error, please fill in all fields before borrowing a book.");
            return;
        }
        if (!bookService.hasBookWithISBN(isbn)) {
            showErrorMessage("Không tìm thấy sách bạn muốn mượn!\nVui lòng thử lại");
        }
        if (!bookService.hasEnoughQuantity(isbn)) {
            showErrorMessage("Sách này hiện không còn trong kho, bạn vui lòng chờ dịp khác nhé!");
        }
        if (LocalDate.now().isAfter(dueDate) || LocalDate.now().isEqual(dueDate)) {
            showErrorMessage("Ngày được chọn không hợp lệ!\nVui lòng thử lại");
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
            showErrorMessage("An error occurred while adding the cart: " + e.getMessage());
        }
    }

    public void handleReturnService(Event event) {
        int studentId = userView.getUser().getUserID();
        String title = userView.getUserReturnISBN().getText();
        String isbn = bookService.fetchISBNByTitle(title);
        if (isbn.isEmpty()) {
            System.out.println("Vui lòng điền đầy đủ thông tin.");
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
            showErrorMessage("An error occurred while removing the cart: " + e.getMessage());
        }
    }

    private void handleUsingTextField(TextField textField, ListView<String> listView, int x) {
        textField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                performSearch(listView);
            } else {
                scheduleSearch(textField, listView);
            }
        });

        listView.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 1) {
                if (x == 0) {
                    userView.setSelecting(false);
                } else if (x == 1) {
                    userView.setSelecting1(false);
                } else if (x == 2) {
                    userView.setSelecting2(false);
                }
                String selectedItem = listView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    textField.setText(selectedItem);
                    hideSuggestions(listView);
                    performSearch(listView);
                }
            }
        });

        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                showSuggestionsIfNotEmpty(listView);
            } else {
                Platform.runLater(() -> {
                    if (!userView.isSelecting() && x == 0) {
                        hideSuggestions(listView);
                    }
                    if (!userView.isSelecting1() && x == 1) {
                        hideSuggestions(listView);
                    }
                    if (!userView.isSelecting2() && x == 2) {
                        hideSuggestions(listView);
                    }
                });
            }
        });

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.trim().isEmpty()) {
                hideSuggestions(listView);
            } else {
                scheduleSearch(textField, listView);
            }
        });
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

    private void performSearch(ListView<String> listView) {
        hideSuggestions(listView);
    }

    public void shutdown() {
        scheduler.shutdownNow();
    }

    public void initializeDueTable() {
        userView.getTimeDueColumn().setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(0)));
        userView.getUserIdDueColumn().setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(1)));
        userView.getIsbnDueColumn().setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(4)));
        userView.getDueDueColumn().setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(5)));
        updateService.populateTableViewByCartId(userView.getActivityDueTable(), userView.getUser().getUserID());

        userView.getNotificationButton().setOnAction(this::handleNotificationButtonAction);
    }

    public void handleNotificationButtonAction(Event event) {
        userView.getActivityDueTable().setVisible(true);
        userView.getNotificationButton().setOnAction(this::closeNotificationPanel);
    }

    private void closeNotificationPanel(Event event) {
        userView.getActivityDueTable().setVisible(false);
        userView.getNotificationButton().setOnAction(this::handleNotificationButtonAction);
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

    public void registerForStudentDetails(Stage stage) {
        updateUserInfo(userView.getUser());
    }

    public void updateUserInfo(User user) {
        if (user == null) {
            return;
        }
        userView.getUsernameLabel().setText(user.getUsername());
        userView.getNameLabel().setText("Name: " + user.getName());
        userView.getEmailLabel().setText("Email: " + user.getEmail());
        userView.getPhoneNumberLabel().setText("Phone number: " + user.getPhoneNumber());
        userView.getCartIDLabel().setText("ID: " + String.valueOf(user.getCart_ID()));
        userView.getBorrowedBooksLabel().setText("Borrowed books: " + String.valueOf(user.getBorrowedBooks()));
        userView.getMembershipTypeLabel().setText(user.getMembershipType());
    }

}
