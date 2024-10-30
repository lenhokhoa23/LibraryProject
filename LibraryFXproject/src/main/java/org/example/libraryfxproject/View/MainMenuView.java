package org.example.libraryfxproject.View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.example.libraryfxproject.Controller.MainMenuController;

import java.io.IOException;

public class MainMenuView {
    private final Stage stage;
    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private ListView<String> suggestions = new ListView<>();
    @FXML
    private MenuItem logoutItem;
    @FXML
    private MenuButton profileButton;
    private boolean isSelecting = false;



    public MainMenuView(Stage stage) {
        this.stage = stage;
        initializeMainMenuView();
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

    public Button getSearchButton() {
        return searchButton;
    }

    public void setSearchButton(Button searchButton) {
        this.searchButton = searchButton;
    }

    public void initializeMainMenuView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/libraryfxproject/MainMenu.fxml"));
        fxmlLoader.setController(this); // Đặt controller là đối tượng hiện tại
        try {
            Parent mainViewParent = fxmlLoader.load();
            Scene scene = new Scene(mainViewParent);
            stage.setX(-5);
            stage.setY(-5);
            stage.setWidth(Screen.getPrimary().getBounds().getWidth() + 10);
            stage.setHeight(Screen.getPrimary().getBounds().getHeight() - 30);
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);
            MainMenuController mainMenuController = new MainMenuController(this);
            mainMenuController.registerEvent();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showErrorMessFill() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText("Please fill the blank search fields!");
        alert.show();
    }


}
