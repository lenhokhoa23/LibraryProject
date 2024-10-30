package org.example.libraryfxproject.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

import org.example.libraryfxproject.Service.LoadService;
import org.example.libraryfxproject.Service.UpdateService;
import javafx.stage.Stage;
import org.example.libraryfxproject.Service.SearchService;
import org.example.libraryfxproject.View.LoginView;

import org.example.libraryfxproject.View.MainMenuView;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MainMenuController {
    private final MainMenuView mainMenuView;
    private final SearchService searchService;
    private final UpdateService updateService;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> searchTask;

    public MainMenuController(MainMenuView mainMenuView) {
        this.mainMenuView = mainMenuView;
        this.searchService = new SearchService();
        this.updateService = new UpdateService();
    }

    public void registerEvent() {
        hideSuggestions();

        mainMenuView.getLogoutItem().setOnAction(event -> {
            LoginView.openLoginView((Stage) mainMenuView.getProfileButton().getScene().getWindow());
        });

        mainMenuView.getSearchField().setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                performSearch();
            } else {
                scheduleSearch();
            }
        });

        mainMenuView.getSuggestions().setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 1) {
                mainMenuView.setSelecting(false);
                String selectedItem = mainMenuView.getSuggestions().getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    mainMenuView.getSearchField().setText(selectedItem);
                    hideSuggestions();
                    performSearch();
                }
            }
        });

        mainMenuView.getSearchField().focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                showSuggestionsIfNotEmpty();
            } else {
                Platform.runLater(() -> {
                    if (!mainMenuView.isSelecting()) {
                        hideSuggestions();
                    }
                });
            }
        });

        mainMenuView.getSearchField().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.trim().isEmpty()) {
                hideSuggestions();
            } else {
                scheduleSearch();
            }
        });

        mainMenuView.getSuggestions().setOnMousePressed(event -> mainMenuView.setSelecting(true));

        // Add a click listener to the root pane to hide suggestions when clicking outside
        mainMenuView.getProfileButton().getScene().getRoot().setOnMouseClicked(event -> {
            if (!mainMenuView.getSearchField().isFocused() && !mainMenuView.getSuggestions().isFocused()) {
                hideSuggestions();
            }
        });
      
        mainMenuView.getTotalBooksLabel().setText(updateService.updatedLabel(1).getText());
        mainMenuView.getActiveStudentsLabel().setText(updateService.updatedLabel(2).getText());
        mainMenuView.getBooksBorrowedLabel().setText(updateService.updatedLabel(3).getText());
        mainMenuView.getOverdueBooksLabel().setText(updateService.updatedLabel(4).getText());

        updateService.updateChart(mainMenuView.getGenreCirculationChart());
        mainMenuView.getChartTitleLabel().setLayoutX(10);
        mainMenuView.getChartTitleLabel().setLayoutY(10);

        mainMenuView.getGenreCirculationChart().layoutXProperty().bind(
                mainMenuView.getChartPane().widthProperty().subtract(mainMenuView.getGenreCirculationChart().widthProperty()).divide(2)
        );
        mainMenuView.getGenreCirculationChart().layoutYProperty().bind(
                mainMenuView.getChartPane().heightProperty().subtract(mainMenuView.getGenreCirculationChart().heightProperty()).divide(2)
        );
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
            String query = mainMenuView.getSearchField().getText().toLowerCase();
            if (!query.trim().isEmpty()) {
                List<String> filteredBooks = searchService.searchBookByPrefix(query);
                ObservableList<String> suggestionList = FXCollections.observableArrayList(filteredBooks);
                mainMenuView.getSuggestions().setItems(suggestionList);
                showSuggestionsIfNotEmpty();
            } else {
                hideSuggestions();
            }
        });
    }

    private void showSuggestionsIfNotEmpty() {
        if (!mainMenuView.getSuggestions().getItems().isEmpty()) {
            mainMenuView.getSuggestions().setVisible(true);
        }
    }

    private void hideSuggestions() {
        mainMenuView.getSuggestions().setVisible(false);
    }

    private void performSearch() {
        // Implement your search logic here

        hideSuggestions();
    }

    public void shutdown() {
        scheduler.shutdownNow();
    }
}
