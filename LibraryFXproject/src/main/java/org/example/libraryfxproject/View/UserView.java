package org.example.libraryfxproject.View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class UserView {
    private final Stage stage;
    public UserView(Stage stage) {
        this.stage = stage;
        initializeUserView();
    }

    public void initializeUserView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/libraryfxproject/UserView.fxml"));
        fxmlLoader.setController(this);
        try {
            Parent userParent = fxmlLoader.load();
            Scene scene = new Scene(userParent);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
