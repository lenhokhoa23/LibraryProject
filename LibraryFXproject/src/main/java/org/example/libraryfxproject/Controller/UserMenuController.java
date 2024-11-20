package org.example.libraryfxproject.Controller;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.libraryfxproject.Model.Book;
import org.example.libraryfxproject.Service.SearchService;
import org.example.libraryfxproject.Util.AlertDisplayer;
import org.example.libraryfxproject.View.LoginView;
import org.example.libraryfxproject.View.UserView;


import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class UserMenuController extends BaseController {
    private final UserView userView;
    private final SearchService searchService;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> searchTask;

    public UserMenuController(UserView userView, AlertDisplayer alertDisplayer) {
        super(alertDisplayer);
        this.userView = userView;
        searchService = SearchService.getInstance();
    }

    public void registerEvent() {
        hideSuggestions();

        userView.getLogoutItem().setOnAction(event -> {
            LoginView.openLoginView((Stage) userView.getProfileButton().getScene().getWindow());
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
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (URISyntaxException e) {
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
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });

        userView.getClub2().setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://www.facebook.com/groups/1953714107984908"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });

        userView.getClub3().setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://www.facebook.com/groups/1928903884018373"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });

        userView.getClub4().setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://www.facebook.com/groups/theabcbookclub"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });

        userView.getClub5().setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://www.facebook.com/groups/152211783175655"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
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

    public void shutdown() {
        scheduler.shutdownNow();
    }
}
