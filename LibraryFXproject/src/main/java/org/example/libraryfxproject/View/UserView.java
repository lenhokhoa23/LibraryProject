package org.example.libraryfxproject.View;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.example.libraryfxproject.Controller.UserMenuController;
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
}
