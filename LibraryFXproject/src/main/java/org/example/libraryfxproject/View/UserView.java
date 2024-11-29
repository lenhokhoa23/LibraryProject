package org.example.libraryfxproject.View;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.example.libraryfxproject.Controller.UserMenuController;
import org.example.libraryfxproject.Model.Book;
import org.example.libraryfxproject.Model.User;
import org.example.libraryfxproject.Util.AlertDisplayer;
import org.example.libraryfxproject.Util.JavaFXAlertDisplayer;

import java.io.IOException;

public class UserView {
    private String username;
    private User user;
    private final Stage stage;

    @FXML
    private MenuItem logoutItem;
    @FXML
    private MenuButton profileButton;
    @FXML
    private ListView<String> suggestions;
    @FXML
    private TextField searchField;
    @FXML
    private Hyperlink aboutUs;
    @FXML
    private Hyperlink contact;
    @FXML
    private Hyperlink privacyPolicy;
    @FXML
    private Hyperlink termsOfService;
    @FXML
    private Button club1;
    @FXML
    private Button club2;
    @FXML
    private Button club3;
    @FXML
    private Button club4;
    @FXML
    private Button club5;

    @FXML
    private TextField userBorrowISBN;

    @FXML
    private DatePicker userBorrowReturnDate;

    @FXML
    private TextField userReturnISBN;

    @FXML
    private Button userBorrowButton;

    @FXML
    private Button userReturnButton;

    @FXML
    private TableView<ObservableList<String>> userBorrowedBooksTable;

    @FXML
    private TableColumn<ObservableList<String>, String> borrowedUserDateColumn;

    @FXML
    private TableColumn<ObservableList<String>, String> borrowedIDColumn;

    @FXML
    private TableColumn<ObservableList<String>, String> borrowedUsernameColumn;

    @FXML
    private TableColumn<ObservableList<String>, String> borrowedISBNColumn;

    @FXML
    private TableColumn<ObservableList<String>, String> borrowedTitleColumn;

    @FXML
    private TableColumn<ObservableList<String>, String> borrowedDueDateColumn;

    @FXML
    private Button refreshBorrowedBooksTable;

    @FXML
    private Button notificationButton;

    @FXML
    private TableView<ObservableList<String>> activityDueTable;

    @FXML
    private TableColumn<ObservableList<String>, String> timeDueColumn;

    @FXML
    private TableColumn<ObservableList<String>, String> userIdDueColumn;

    @FXML
    private TableColumn<ObservableList<String>, String> isbnDueColumn;

    @FXML
    private TableColumn<ObservableList<String>, String> dueDueColumn;

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
    private TableView<Book> catalogTableView;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchCatalog;

    @FXML
    private ComboBox<String> filterComboBox;

    @FXML
    private Button refreshButton;

    @FXML
    private ToggleButton searchToggle;


    @FXML
    private StackPane notificationPanel;

    private AlertDisplayer alertDisplayer;

    private boolean isSelecting = false;

    public UserView(Stage stage, String username) {
        this.stage = stage;
        this.username = username;
        initializeUserView();
    }

    public Button getClub1() {
        return club1;
    }

    public Button getClub2() {
        return club2;
    }

    public Button getClub3() {
        return club3;
    }

    public Button getClub4() {
        return club4;
    }

    public Button getClub5() {
        return club5;
    }

    public Hyperlink getAboutUs() {
        return aboutUs;
    }

    public Hyperlink getContact() {
        return contact;
    }

    public Hyperlink getPrivacyPolicy() {
        return privacyPolicy;
    }

    public Hyperlink getTermsOfService() {
        return termsOfService;
    }

    public MenuItem getLogoutItem() {
        return logoutItem;
    }

    public Stage getStage() {
        return stage;
    }

    public MenuButton getProfileButton() {
        return profileButton;
    }

    public TextField getSearchField() {
        return searchField;
    }

    public ListView<String> getSuggestions() {
        return suggestions;
    }

    public boolean isSelecting() {
        return isSelecting;
    }

    public void setSelecting(boolean selecting) {
        isSelecting = selecting;
    }

    public Pagination getCatalogPagination() {
        return catalogPagination;
    }

    public void setCatalogPagination(Pagination catalogPagination) {
        this.catalogPagination = catalogPagination;
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

    public void setCatalogTableView(TableView<Book> catalogTableView) {
        this.catalogTableView = catalogTableView;
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


    public ToggleButton getSearchToggle() {
        return searchToggle;
    }

    public void setSearchToggle(ToggleButton searchToggle) {
        this.searchToggle = searchToggle;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TextField getUserBorrowISBN() {
        return userBorrowISBN;
    }

    public void setUserBorrowISBN(TextField userBorrowISBN) {
        this.userBorrowISBN = userBorrowISBN;
    }

    public DatePicker getUserBorrowReturnDate() {
        return userBorrowReturnDate;
    }

    public void setUserBorrowReturnDate(DatePicker userBorrowReturnDate) {
        this.userBorrowReturnDate = userBorrowReturnDate;
    }

    public TextField getUserReturnISBN() {
        return userReturnISBN;
    }

    public void setUserReturnISBN(TextField userReturnISBN) {
        this.userReturnISBN = userReturnISBN;
    }

    public Button getUserBorrowButton() {
        return userBorrowButton;
    }

    public void setUserBorrowButton(Button userBorrowButton) {
        this.userBorrowButton = userBorrowButton;
    }

    public Button getUserReturnButton() {
        return userReturnButton;
    }

    public void setUserReturnButton(Button userReturnButton) {
        this.userReturnButton = userReturnButton;
    }

    public TableView<ObservableList<String>> getUserBorrowedBooksTable() {
        return userBorrowedBooksTable;
    }

    public void setUserBorrowedBooksTable(TableView<ObservableList<String>> userBorrowedBooksTable) {
        this.userBorrowedBooksTable = userBorrowedBooksTable;
    }

    public TableColumn<ObservableList<String>, String> getBorrowedUserDateColumn() {
        return borrowedUserDateColumn;
    }

    public void setBorrowedUserDateColumn(TableColumn<ObservableList<String>, String> borrowedUserDateColumn) {
        this.borrowedUserDateColumn = borrowedUserDateColumn;
    }

    public TableColumn<ObservableList<String>, String> getBorrowedIDColumn() {
        return borrowedIDColumn;
    }

    public void setBorrowedIDColumn(TableColumn<ObservableList<String>, String> borrowedIDColumn) {
        this.borrowedIDColumn = borrowedIDColumn;
    }

    public TableColumn<ObservableList<String>, String> getBorrowedUsernameColumn() {
        return borrowedUsernameColumn;
    }

    public void setBorrowedUsernameColumn(TableColumn<ObservableList<String>, String> borrowedUsernameColumn) {
        this.borrowedUsernameColumn = borrowedUsernameColumn;
    }

    public TableColumn<ObservableList<String>, String> getBorrowedISBNColumn() {
        return borrowedISBNColumn;
    }

    public void setBorrowedISBNColumn(TableColumn<ObservableList<String>, String> borrowedISBNColumn) {
        this.borrowedISBNColumn = borrowedISBNColumn;
    }

    public TableColumn<ObservableList<String>, String> getBorrowedTitleColumn() {
        return borrowedTitleColumn;
    }

    public void setBorrowedTitleColumn(TableColumn<ObservableList<String>, String> borrowedTitleColumn) {
        this.borrowedTitleColumn = borrowedTitleColumn;
    }

    public TableColumn<ObservableList<String>, String> getBorrowedDueDateColumn() {
        return borrowedDueDateColumn;
    }

    public void setBorrowedDueDateColumn(TableColumn<ObservableList<String>, String> borrowedDueDateColumn) {
        this.borrowedDueDateColumn = borrowedDueDateColumn;
    }

    public Button getRefreshBorrowedBooksTable() {
        return refreshBorrowedBooksTable;
    }

    public void setRefreshBorrowedBooksTable(Button refreshBorrowedBooksTable) {
        this.refreshBorrowedBooksTable = refreshBorrowedBooksTable;
    }

    public TableView<ObservableList<String>> getActivityDueTable() {
        return activityDueTable;
    }

    public void setActivityDueTable(TableView<ObservableList<String>> activityDueTable) {
        this.activityDueTable = activityDueTable;
    }

    public TableColumn<ObservableList<String>, String> getTimeDueColumn() {
        return timeDueColumn;
    }

    public void setTimeDueColumn(TableColumn<ObservableList<String>, String> timeDueColumn) {
        this.timeDueColumn = timeDueColumn;
    }

    public TableColumn<ObservableList<String>, String> getUserIdDueColumn() {
        return userIdDueColumn;
    }

    public void setUserIdDueColumn(TableColumn<ObservableList<String>, String> userIdDueColumn) {
        this.userIdDueColumn = userIdDueColumn;
    }

    public TableColumn<ObservableList<String>, String> getIsbnDueColumn() {
        return isbnDueColumn;
    }

    public void setIsbnDueColumn(TableColumn<ObservableList<String>, String> isbnDueColumn) {
        this.isbnDueColumn = isbnDueColumn;
    }

    public TableColumn<ObservableList<String>, String> getDueDueColumn() {
        return dueDueColumn;
    }

    public void setDueDueColumn(TableColumn<ObservableList<String>, String> dueDueColumn) {
        this.dueDueColumn = dueDueColumn;
    }

    public StackPane getNotificationPanel() {
        return notificationPanel;
    }

    public void setNotificationPanel(StackPane notificationPanel) {
        this.notificationPanel = notificationPanel;
    }

    public Button getNotificationButton() {
        return notificationButton;
    }

    public void setNotificationButton(Button notificationButton) {
        this.notificationButton = notificationButton;
    }

    public void initializeUserView() {
        alertDisplayer = new JavaFXAlertDisplayer();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/libraryfxproject/UserView.fxml"));
        fxmlLoader.setController(this);
        try {
            Parent userParent = fxmlLoader.load();
            Scene scene = new Scene(userParent);
            stage.setX(-5);
            stage.setY(-5);
            stage.setWidth(Screen.getPrimary().getBounds().getWidth() + 10);
            stage.setHeight(Screen.getPrimary().getBounds().getHeight() - 30);
            stage.setScene(scene);
            stage.show();
            UserMenuController userMenuController = new UserMenuController(this, alertDisplayer);
            userMenuController.registerEvent();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
