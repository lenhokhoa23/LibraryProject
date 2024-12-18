package org.example.libraryfxproject.View;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;

import javafx.scene.chart.PieChart;
import javafx.scene.layout.Pane;


import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.example.libraryfxproject.Controller.MainMenuController;
import org.example.libraryfxproject.Model.Book;
import org.example.libraryfxproject.Model.Librarian;
import org.example.libraryfxproject.Model.User;
import org.example.libraryfxproject.Util.AlertDisplayer;
import org.example.libraryfxproject.Util.JavaFXAlertDisplayer;

import java.io.IOException;

public class MainMenuView {

    /** FXML buttons declaration with getters and setters. */
    @FXML
    private TextField title;

    @FXML
    private TextField author;

    @FXML
    private DatePicker pubdate;

    @FXML
    private DatePicker releaseDate;

    @FXML
    private TextField ISBN;

    @FXML
    private TextField price;

    @FXML
    private TextField subject;

    @FXML
    private TextField category;

    @FXML
    private TextField URL;

    @FXML
    private TextField bookType;

    @FXML
    private TextField quantity;

    @FXML
    private Button addBookButton;

    private String username;

    @FXML
    private Button backButton;

    @FXML
    private TableView<Book> catalogTableView;

    @FXML
    private Pagination catalogPagination;

    private Librarian librarian;

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
    private Button searchBookButton;

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
    private ListView<String> suggestions1 = new ListView<>();

    @FXML
    private ListView<String> suggestions2 = new ListView<>();

    @FXML
    private ListView<String> suggestions3 = new ListView<>();

    @FXML
    private MenuItem logoutItem;

    @FXML
    private MenuButton profileButton;
    private boolean isSelecting;
    private boolean isSelecting1 = false;
    private boolean isSelecting2 = false;

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

    @FXML
    private ComboBox<String> membershipTypeComboBox;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField studentSearch;

    @FXML
    private Button studentSearchButton;

    @FXML
    private Button refreshStudentButton;

    @FXML
    private Button exportDataButton;

    private AlertDisplayer alertDisplayer;

    @FXML
    private Button modifyButton;

    @FXML
    private TextField isbnField;

    @FXML
    private TextField newValueField;

    @FXML
    private Button updateButton;

    @FXML
    private Button goToPageButton;

    @FXML
    private TextField pageNumberField;

    @FXML
    private TableView<ObservableList<String>> borrowHistoryTable;

    @FXML
    private TableColumn<ObservableList<String>, String> cartIdColumn;

    @FXML
    private TableColumn<ObservableList<String>, String> bookTitleColumn;

    @FXML
    private TableColumn<ObservableList<String>, String> isbnColumn;

    @FXML
    private TableColumn<ObservableList<String>, String> borrowDateColumn;

    @FXML
    private TableColumn<ObservableList<String>, String> dueDateColumn;

    @FXML
    private TableColumn<ObservableList<String>, String> userNameColumn;

    @FXML
    private Tab service;

    @FXML
    private TextField borrowStudentIdField1;

    @FXML
    private TextField borrowISBNField1;

    @FXML
    private DatePicker borrowDueDatePicker1;

    @FXML
    private Button borrowServiceButton;

    @FXML
    private TextField returnStudentIdField1;

    @FXML
    private TextField returnISBNField1;

    @FXML
    private Button returnServiceButton;

    @FXML
    private Button search1;

    @FXML
    private TextField studentID1;

    @FXML
    private DatePicker startDate1;

    @FXML
    private DatePicker endDate1;

    @FXML
    private Button refresh1;
  
    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private ComboBox<String> filterByColumn;

    @FXML
    private TextField searchField1;

    @FXML
    private Button searchButton1;

    @FXML
    private Button quickAddBookButton;

    @FXML
    private ComboBox<String> attributeStudentComboBox;

    @FXML
    private ComboBox<String> attributeBookComboBox;

    @FXML
    private TextField studentIdField1;

    @FXML
    private TextField newValueField1;

    @FXML
    private Button updateButton1;

    @FXML
    private Button backButton1;

    @FXML
    private Button modifyStudentButton;

    public MainMenuView(Stage stage, String username) {
        this.stage = stage;
        this.username = username;
        initializeMainMenuView();
    }

    public Button getQuickAddBookButton() {
        return quickAddBookButton;
    }

    public TableColumn<Book, Integer> getItemIdColumn() {
        return itemIdColumn;
    }

    public void setItemIdColumn(TableColumn<Book, Integer> itemIdColumn) {
        this.itemIdColumn = itemIdColumn;
    }

    public TableColumn<Book, String> getTitleColumn() {
        return titleColumn;
    }

    public void setTitleColumn(TableColumn<Book, String> titleColumn) {
        this.titleColumn = titleColumn;
    }

    public TableColumn<Book, String> getAuthorColumn() {
        return authorColumn;
    }

    public void setAuthorColumn(TableColumn<Book, String> authorColumn) {
        this.authorColumn = authorColumn;
    }

    public TableColumn<Book, String> getSubjectColumn() {
        return subjectColumn;
    }

    public void setSubjectColumn(TableColumn<Book, String> subjectColumn) {
        this.subjectColumn = subjectColumn;
    }

    public TableColumn<Book, String> getBookTypeColumn() {
        return bookTypeColumn;
    }

    public void setBookTypeColumn(TableColumn<Book, String> bookTypeColumn) {
        this.bookTypeColumn = bookTypeColumn;
    }

    public TableColumn<Book, String> getQuantityColumn() {
        return quantityColumn;
    }

    public void setQuantityColumn(TableColumn<Book, String> quantityColumn) {
        this.quantityColumn = quantityColumn;
    }
    
    public TableView<Book> getCatalogTableView() {
        return catalogTableView;
    }

    public Pagination getCatalogPagination() {
        return this.catalogPagination; // Add this method
    }

    public void setCatalogPagination(Pagination catalogPagination) {
        this.catalogPagination = catalogPagination;
    }

    public TextField getPageNumberField() {
        return pageNumberField;
    }

    public void setPageNumberField(TextField pageNumberField) {
        this.pageNumberField = pageNumberField;
    }

    public Button getGoToPageButton() {
        return goToPageButton;
    }

    public void setGoToPageButton(Button goToPageButton) {
        this.goToPageButton = goToPageButton;
    }
    public ProgressIndicator getProgressIndicator() {
        return progressIndicator;
    }

    public Button getExportDataButton() {
        return exportDataButton;
    }

    public Button getRefreshStudentButton() {
        return refreshStudentButton;
    }

    public TextField getStudentSearch() {
        return studentSearch;
    }

    public Button getStudentSearchButton() {
        return studentSearchButton;
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

    public ComboBox<String> getMembershipTypeComboBox() {
        return membershipTypeComboBox;
    }

    public TextField getPasswordField() {
        return passwordField;
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

    public Button getSearchBookButton() {
        return searchBookButton;
    }

    public void setSearchBookButton(Button searchButton) {
        this.searchBookButton = searchButton;
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

    public TextField getTitle() {
        return title;
    }

    public TextField getAuthor() {
        return author;
    }

    public DatePicker getPubdate() {
        return pubdate;
    }

    public DatePicker getReleaseDate() {
        return releaseDate;
    }

    public TextField getISBN() {
        return ISBN;
    }

    public TextField getPrice() {
        return price;
    }

    public TextField getSubject() {
        return subject;
    }

    public TextField getCategory() {
        return category;
    }

    public TextField getURL() {
        return URL;
    }

    public TextField getBookType() {
        return bookType;
    }

    public TextField getQuantity() {
        return quantity;
    }

    public Button getAddBookButton() {
        return addBookButton;
    }


    public TableColumn<ObservableList<String>, String> getCartIdColumn() {
        return cartIdColumn;
    }

    public void setCartIdColumn(TableColumn<ObservableList<String>, String> cartIdColumn) {
        this.cartIdColumn = cartIdColumn;
    }

    public TableColumn<ObservableList<String>, String> getBookTitleColumn() {
        return bookTitleColumn;
    }

    public void setBookTitleColumn(TableColumn<ObservableList<String>, String> bookTitleColumn) {
        this.bookTitleColumn = bookTitleColumn;
    }

    public TableColumn<ObservableList<String>, String> getIsbnColumn() {
        return isbnColumn;
    }

    public void setIsbnColumn(TableColumn<ObservableList<String>, String> isbnColumn) {
        this.isbnColumn = isbnColumn;
    }

    public TableColumn<ObservableList<String>, String> getBorrowDateColumn() {
        return borrowDateColumn;
    }

    public void setBorrowDateColumn(TableColumn<ObservableList<String>, String> borrowDateColumn) {
        this.borrowDateColumn = borrowDateColumn;
    }

    public TableColumn<ObservableList<String>, String> getDueDateColumn() {
        return dueDateColumn;
    }

    public void setDueDateColumn(TableColumn<ObservableList<String>, String> dueDateColumn) {
        this.dueDateColumn = dueDateColumn;
    }

    public TableColumn<ObservableList<String>, String> getUserNameColumn() {
        return userNameColumn;
    }

    public void setUserNameColumn(TableColumn<ObservableList<String>, String> userNameColumn) {
        this.userNameColumn = userNameColumn;
    }

    public TableView<ObservableList<String>> getBorrowHistoryTable() {
        return borrowHistoryTable;
    }

    public void setBorrowHistoryTable(TableView<ObservableList<String>> borrowHistoryTable) {
        this.borrowHistoryTable = borrowHistoryTable;
    }

    public TextField getBorrowStudentIdField1() {
        return borrowStudentIdField1;
    }


    public void setBorrowStudentIdField1(TextField borrowStudentIdField1) {
        this.borrowStudentIdField1 = borrowStudentIdField1;
    }

    public TextField getBorrowISBNField1() {
        return borrowISBNField1;
    }

    public void setBorrowISBNField1(TextField borrowISBNField1) {
        this.borrowISBNField1 = borrowISBNField1;
    }

    public DatePicker getBorrowDueDatePicker1() {
        return borrowDueDatePicker1;
    }

    public void setBorrowDueDatePicker1(DatePicker borrowDueDatePicker1) {
        this.borrowDueDatePicker1 = borrowDueDatePicker1;
    }

    public Button getBorrowServiceButton() {
        return borrowServiceButton;
    }

    public void setBorrowServiceButton(Button borrowServiceButton) {
        this.borrowServiceButton = borrowServiceButton;
    }

    public TextField getReturnStudentIdField1() {
        return returnStudentIdField1;
    }

    public void setReturnStudentIdField1(TextField returnStudentIdField1) {
        this.returnStudentIdField1 = returnStudentIdField1;
    }

    public TextField getReturnISBNField1() {
        return returnISBNField1;
    }

    public void setReturnISBNField1(TextField returnISBNField1) {
        this.returnISBNField1 = returnISBNField1;
    }

    public Button getReturnServiceButton() {
        return returnServiceButton;
    }

    public void setReturnServiceButton(Button returnServiceButton) {
        this.returnServiceButton = returnServiceButton;
    }

    public Tab getService() {
        return service;
    }

    public void setService(Tab service) {
        this.service = service;
    }

    public Button getSearch1() {
        return search1;
    }

    public void setSearch1(Button search1) {
        this.search1 = search1;
    }

    public TextField getStudentID1() {
        return studentID1;
    }

    public void setStudentID1(TextField studentID1) {
        this.studentID1 = studentID1;
    }

    public DatePicker getStartDate1() {
        return startDate1;
    }

    public void setStartDate1(DatePicker startDate1) {
        this.startDate1 = startDate1;
    }

    public DatePicker getEndDate1() {
        return endDate1;
    }

    public void setEndDate1(DatePicker endDate1) {
        this.endDate1 = endDate1;
    }

    public Button getRefresh1() {
        return refresh1;
    }

    public void setRefresh1(Button refresh1) {
        this.refresh1 = refresh1;
    }

    public ListView<String> getSuggestions1() {
        return suggestions1;
    }

    public void setSuggestions1(ListView<String> suggestions1) {
        this.suggestions1 = suggestions1;
    }

    public boolean isSelecting1() {
        return isSelecting1;
    }

    public void setSelecting1(boolean selecting1) {
        isSelecting1 = selecting1;
    }

    public ListView<String> getSuggestions2() {
        return suggestions2;
    }

    public void setSuggestions2(ListView<String> suggestions2) {
        this.suggestions2 = suggestions2;
    }

    public ListView<String> getSuggestions3() {
        return suggestions3;
    }

    public void setSuggestions3(ListView<String> suggestions3) {
        this.suggestions3 = suggestions3;
    }

    public boolean isSelecting2() {
        return isSelecting2;
    }

    public void setSelecting2(boolean selecting2) {
        isSelecting2 = selecting2;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Librarian getLibrarian() {
        return librarian;
    }

    public void setLibrarian(Librarian librarian) {
        this.librarian = librarian;
    }

    public TextField getStudentIdField1() {
        return studentIdField1;
    }

    public void setStudentIdField1(TextField studentIdField1) {
        this.studentIdField1 = studentIdField1;
    }

    public TextField getNewValueField1() {
        return newValueField1;
    }

    public void setNewValueField1(TextField newValueField1) {
        this.newValueField1 = newValueField1;
    }

    public Button getUpdateButton1() {
        return updateButton1;
    }

    public void setUpdateButton1(Button updateButton1) {
        this.updateButton1 = updateButton1;
    }

    public Button getBackButton1() {
        return backButton1;
    }

    public void setBackButton1(Button backButton1) {
        this.backButton1 = backButton1;
    }

    public Button getModifyStudentButton() {
        return modifyStudentButton;
    }

    public void setModifyStudentButton(Button modifyStudentButton) {
        this.modifyStudentButton = modifyStudentButton;
    }

    public ComboBox<String> getAttributeStudentComboBox() {
        return attributeStudentComboBox;
    }

    public void setAttributeStudentComboBox(ComboBox<String> attributeComboBox) {
        this.attributeStudentComboBox = attributeComboBox;
    }

    public ComboBox<String> getAttributeBookComboBox() {
        return attributeBookComboBox;
    }

    public void setAttributeBookComboBox(ComboBox<String> attributeBookComboBox) {
        this.attributeBookComboBox = attributeBookComboBox;
    }

    public Button getModifyButton() {
        return modifyButton;
    }

    /**
     * Khởi tạo và hiển thị giao diện Main menu, thiết lập cửa sổ chính của ứng dụng
     * (cho librarian).
     * Đăng ký `MainMenuController` để xử lý các tương tác của menu chính.
     * Một số thao tác căn chỉnh màn hình :).
     * @throws IOException nếu có lỗi khi tải tệp FXML .
     */
    public void initializeMainMenuView() {
        alertDisplayer = JavaFXAlertDisplayer.getInstance();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/libraryfxproject/views/MainMenu.fxml"));
        fxmlLoader.setController(this);
        try {
            Parent mainViewParent = fxmlLoader.load();
            stage.setWidth(Screen.getPrimary().getBounds().getWidth());
            stage.setHeight(Screen.getPrimary().getBounds().getHeight());
            stage.setX((Screen.getPrimary().getBounds().getWidth() - stage.getWidth()) / 2);
            stage.setY((Screen.getPrimary().getBounds().getHeight() - stage.getHeight()) / 2);
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


    /**
     * Khởi tạo và hiển thị giao diện thêm sinh viên.
     * Mở một cửa sổ mới cho phép thêm thông tin sinh viên vào hệ thống.
     * @param mainMenuController đối tượng controller của menu chính để đăng ký sự kiện cho cửa sổ thêm sinh viên.
     */
    public void initializeAddStudentView(MainMenuController mainMenuController) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/libraryfxproject/views/addStudent.fxml"));
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

    /**
     * Khởi tạo và hiển thị giao diện sửa thông tin sinh viên.
     * Mở một cửa sổ mới cho phép sửa thông tin của sinh viên.
     * @param mainMenuController đối tượng controller của menu chính để đăng ký sự kiện cho cửa sổ sửa sinh viên.
     */
    public void initializeModifyStudentView(MainMenuController mainMenuController) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/libraryfxproject/views/modifyStudent.fxml"));
        fxmlLoader.setController(this);
        try {
            Parent modifyStudentParent = fxmlLoader.load();
            Scene modifyStudentScene = new Scene(modifyStudentParent);
            Stage modifyStudentStage = new Stage();
            modifyStudentStage.setScene(modifyStudentScene);
            modifyStudentStage.initModality(Modality.APPLICATION_MODAL);
            modifyStudentStage.initOwner(stage);
            modifyStudentStage.show();
            mainMenuController.registerForModifyStudent(modifyStudentStage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Khởi tạo và hiển thị giao diện sửa thông tin sách.
     * Mở một cửa sổ mới cho phép sửa thông tin của sách.
     * @param mainMenuController đối tượng controller của menu chính để đăng ký sự kiện cho cửa sổ sửa sách.
     */
    public void initializeModifyBookView(MainMenuController mainMenuController) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/libraryfxproject/views/modifyBook.fxml"));
        fxmlLoader.setController(this);
        try {
            Parent modifyBookParent = fxmlLoader.load();
            Scene modifyBookScene = new Scene(modifyBookParent);
            Stage modifyBookStage = new Stage();
            modifyBookStage.setTitle("Modify Book");
            modifyBookStage.setScene(modifyBookScene);
            modifyBookStage.initModality(Modality.APPLICATION_MODAL);
            modifyBookStage.initOwner(stage);
            modifyBookStage.show();
            mainMenuController.registerForModifyBook(modifyBookStage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Khởi tạo và hiển thị giao diện thêm sách mới.
     * Mở một cửa sổ mới cho phép thêm sách vào hệ thống.
     * @param mainMenuController đối tượng controller của menu chính để đăng ký sự kiện cho cửa sổ thêm sách.
     */
    public void initializeAddBookView(MainMenuController mainMenuController) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/libraryfxproject/views/addBookView.fxml"));
        loader.setController(this);
        try {
            Parent addBookParent = loader.load();
            Scene addBookScene = new Scene(addBookParent);
            Stage addBookStage = new Stage();
            addBookStage.setScene(addBookScene);
            addBookStage.initModality(Modality.APPLICATION_MODAL);
            addBookStage.initOwner(stage);
            addBookStage.show();
            mainMenuController.registerForAddBook(addBookStage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
