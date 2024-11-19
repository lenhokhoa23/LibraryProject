package org.example.libraryfxproject.View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.example.libraryfxproject.Controller.UserMenuController;
import org.example.libraryfxproject.Util.AlertDisplayer;
import org.example.libraryfxproject.Util.JavaFXAlertDisplayer;

import java.io.IOException;

public class UserView {
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

    private AlertDisplayer alertDisplayer;


    private boolean isSelecting = false;


    public UserView(Stage stage) {
        this.stage = stage;
        alertDisplayer = new JavaFXAlertDisplayer();
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
