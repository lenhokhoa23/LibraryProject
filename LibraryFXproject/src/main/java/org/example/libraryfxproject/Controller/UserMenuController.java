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
import org.example.libraryfxproject.View.BookDetailsView;
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
    private final CartService cartService;
    private final UpdateService updateService;
    private final BookService bookService;
    private final LoginService loginService;
    private final int ROWS_PER_PAGE = 15;
    private ObservableList<Book> bookList = FXCollections.observableArrayList();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> searchTask;
    private boolean isFilteredView = false;

    public UserMenuController(UserView userView, AlertDisplayer alertDisplayer) {
        super(alertDisplayer);
        this.userView = userView;
        searchService = SearchService.getInstance();
        cartService = CartService.getInstance();
        updateService = UpdateService.getInstance();
        bookService = BookService.getInstance();
        loginService = LoginService.getInstance();
    }

    /**
     * Phương thức này đăng ký  các sự kiện và hành động cho các thành phần giao diện người dùng trong UserView.
     * Các sự kiện bao gồm việc xử lý hành động khi người dùng tìm kiếm sách, mượn sách, trả sách, đăng xuất,
     * cũng như các sự kiện liên quan đến các nút và menu trong giao diện người dùng.
     */
     public void registerEvent() {
        userView.setUser(loginService.findUserByUsername(userView.getUsername()));
        userView.getWelcomeMessage().setText("Welcome back, " + userView.getUser().getName() + "!");
        hideSuggestions(userView.getSuggestions());
        hideSuggestions(userView.getSuggestions1());
        hideSuggestions(userView.getSuggestions2());

        // Sự kiện đăng xuất
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

        // Các sự kiện tiện ích bổ sung: điều khoản, hội nhóm,...
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

        // Nút refresh cho catalog view
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

        // Nút toggle search bật tắt
        userView.getSearchToggle().setOnAction(event -> {
            CatalogEvent();
        });

        // Nút chuyển sang tab khác
        userView.getBorrowBook().setOnAction(event -> {
            TabPane tabPane = userView.getTabPane();
            tabPane.getSelectionModel().select(4);
        });

        // Vẫn vậy
        userView.getReturnBook().setOnAction(event -> {
            TabPane tabPane = userView.getTabPane();
            tabPane.getSelectionModel().select(4);
        });

        // Hiện chi tiết profile của user
        userView.getStudentProfileDetails().setOnAction(event -> {
            userView.initializeStudentDetailsView(this);
        });

        //Xử lý sự kiện mượn sách
        userView.getUserBorrowButton().setOnAction(this::handleBorrowService);
        userView.getUserReturnButton().setOnAction(this::handleReturnService);

        // Thiết lập bảng catalog
        setupTableColumns();

        // Load dữ liệu cho bảng
        loadTableData();

        // Thiết lập bang sách đã mượn
        initializeTable();

        //Khởi tạo thanh chuyển page
        initializePagination();


        initializeDueTable();

        // Hiển thị chi tiết thông tin sách
        userView.getSearchBookButton().setOnAction(e -> {
            Book book = bookService.getBookByTitle(userView.getSearchField().getText());
            new BookDetailsView(book, userView.getUsername());
        });


    }

    /**
     * Khởi tạo bảng để hiển thị các sách đã mượn của người dùng.
     * Thiết lập các giá trị cho từng cột trong bảng sách mượn
     * và điền dữ liệu vào bảng từ dịch vụ giỏ hàng dựa trên ID người dùng.
     * Thêm một trình lắng nghe để làm mới bảng khi người dùng nhấn nút làm mới.
     */
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

    /**
     * Phương thức này dùng để xử lý việc mượn sách của người dùng.
     * Nó kiểm tra các điều kiện như ISBN, ngày trả sách hợp lệ, và tồn kho của sách trước khi thực hiện mượn sách.
     *
     * @param event sự kiện kích hoạt phương thức (thường là từ một nút bấm)
     */
    public void handleBorrowService(Event event) {
        int studentId = userView.getUser().getUserID();
        String title = userView.getUserBorrowISBN().getText();
        String isbn = bookService.fetchISBNByTitle(title);
        LocalDate dueDate = userView.getUserBorrowReturnDate().getValue();
        if (isbn.isEmpty() || dueDate == null) {
            showErrorMessage("Error, please fill in all fields before borrowing a book.");
            return;
        }
        // Kiểm tra sách tồn kho và ngày trả sách hợp lệ
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
        // Thêm vào giỏ hàng
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
        initializeDueTable();
    }

    /**
     * Xử lý hành động trả sách đã mượn.
     * Kiểm tra xem ISBN mà người dùng nhập có trong giỏ hàng của họ hay không và,
     * nếu có, xóa sách khỏi giỏ hàng và cập nhật bảng ngày đến hạn.
     *
     * @param event Sự kiện kích hoạt hành động trả sách.
     */
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
        initializeDueTable();
    }

    /**
     * Xử lý sự kiện khi người dùng nhập văn bản vào ô tìm kiếm.
     * Nếu người dùng nhấn phím Enter, thực hiện tìm kiếm ngay lập tức.
     * Nếu người dùng nhập một ký tự, thực hiện tìm kiếm có độ trễ.
     * Khi người dùng chọn một mục trong danh sách gợi ý, điền vào ô tìm kiếm và thực hiện tìm kiếm.
     *
     * @param textField Ô nhập văn bản của người dùng.
     * @param listView Danh sách gợi ý để hiển thị kết quả tìm kiếm.
     * @param x Chỉ số dùng để phân biệt các ô nhập liệu.
     */
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

    /**
     * Lên lịch tìm kiếm sau một khoảng thời gian ngắn khi người dùng nhập văn bản vào ô tìm kiếm.
     * Nếu có một tìm kiếm đang chạy, nó sẽ bị hủy trước khi bắt đầu tìm kiếm mới.
     *
     * @param textField Ô nhập văn bản mà người dùng sử dụng để tìm kiếm.
     * @param listView Danh sách gợi ý kết quả tìm kiếm sẽ được hiển thị.
     */
    private void scheduleSearch(TextField textField, ListView<String> listView) {
        if (searchTask != null && !searchTask.isDone()) {
            searchTask.cancel(false);
        }
        searchTask = scheduler.schedule(() -> updateSuggestions(textField, listView), 50, TimeUnit.MILLISECONDS);
    }

    /**
     * Cập nhật các gợi ý tìm kiếm dựa trên văn bản người dùng nhập vào ô tìm kiếm.
     * Nếu văn bản tìm kiếm không rỗng, nó sẽ tìm kiếm sách có tiền tố phù hợp và hiển thị kết quả.
     * Nếu không có kết quả tìm kiếm, danh sách gợi ý sẽ bị ẩn.
     *
     * @param textField Ô nhập văn bản của người dùng.
     * @param listView Danh sách gợi ý kết quả tìm kiếm.
     */
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

    /**
     * Khởi tạo bảng hiển thị thông tin về các sách đã đến hạn trả của người dùng.
     * Thiết lập các giá trị cho từng cột trong bảng và cập nhật bảng với dữ liệu từ dịch vụ.
     */
    public void initializeDueTable() {
        userView.getTimeDueColumn().setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(0)));
        userView.getUserIdDueColumn().setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(1)));
        userView.getIsbnDueColumn().setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(4)));
        userView.getDueDueColumn().setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(5)));
        updateService.populateTableViewByCartId(userView.getActivityDueTable(), userView.getUser().getUserID());

        userView.getNotificationButton().setOnAction(this::handleNotificationButtonAction);
    }

    /**
     * Xử lý hành động khi người dùng nhấn nút thông báo.
     * Hiển thị bảng thông báo về các sách đã đến hạn trả và thay đổi hành động của nút thông báo.
     *
     * @param event Sự kiện kích hoạt hành động (nhấn nút).
     */
    public void handleNotificationButtonAction(Event event) {
        userView.getActivityDueTable().setVisible(true);
        userView.getNotificationButton().setOnAction(this::closeNotificationPanel);
    }

    /**
     * Xử lý hành động khi người dùng nhấn nút tắt thông báo.
     * Ẩn hiển thị bảng .
     *
     * @param event Sự kiện kích hoạt hành động (nhấn nút).
     */
    private void closeNotificationPanel(Event event) {
        userView.getActivityDueTable().setVisible(false);
        userView.getNotificationButton().setOnAction(this::handleNotificationButtonAction);
    }

    /**
     * Khởi tạo phân trang cho bảng sách trong giao diện người dùng.
     * Tính toán số trang và thiết lập chức năng phân trang cho bảng sách.
     */
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

    /**
     * Cập nhật bảng hiển thị sách trong giao diện người dùng với danh sách sách mới.
     * Phương thức này sẽ xóa các mục hiện tại trong bảng và thêm các sách từ danh sách được cung cấp.
     *
     * @param books Danh sách sách mới cần hiển thị trong bảng.
     */
    public void updateBookTableView(ObservableList<Book> books) {
        userView.getCatalogTableView().getItems().clear();
        userView.getCatalogTableView().setItems(books);
    }

    /**
     * Cập nhật bảng sách hiển thị trên trang hiện tại dựa trên chỉ số trang.
     * Dữ liệu sẽ được phân trang và chỉ hiển thị các sách trong phạm vi trang hiện tại.
     *
     * @param pageIndex Chỉ số của trang hiện tại.
     */
    private void updateBookTable(int pageIndex) {
        int start = pageIndex * ROWS_PER_PAGE;
        int end = Math.min(start + ROWS_PER_PAGE, bookList.size());
        userView.getCatalogTableView().setItems(FXCollections.observableArrayList(bookList.subList(start, end)));
    }

    /**
     * Cấu hình các cột trong bảng sách của giao diện người dùng.
     * Phương thức này thiết lập các thuộc tính cho mỗi cột trong bảng, như ID mục, tiêu đề, tác giả,
     * chủ đề, loại sách và số lượng. Sau đó, nó thêm các cột này vào bảng hiển thị.
     */
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

    /**
     * Tải dữ liệu sách và cập nhật bảng sách hiển thị trong giao diện người dùng.
     */
    public void loadTableData() {
        bookList = FXCollections.observableArrayList(bookService.getBookDAO().getDataMap().values());
        updateBookTableView(bookList);
    }

    /**
     * Xử lý sự kiện tìm kiếm và lọc danh sách sách trong catalog.
     * Nếu có bộ lọc, tìm kiếm sách theo bộ lọc và cập nhật bảng sách.
     */
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

    /**
     * Cập nhật thông tin người dùng trong giao diện người dùng.
     * Hiển thị thông tin chi tiết của người dùng như tên, email, số điện thoại, v.v.
     *
     * @param user Đối tượng người dùng cần cập nhật thông tin.
     */
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
