package org.example.libraryfxproject.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseEvent;
import org.example.libraryfxproject.Service.LoadService;
import org.example.libraryfxproject.Service.SearchService;
import org.example.libraryfxproject.Service.UpdateService;
import org.example.libraryfxproject.View.MainMenuView;

import java.util.List;

public class MainMenuController {
    private final MainMenuView mainMenuView;
    private final SearchService searchService;
    private final UpdateService updateService;

    public MainMenuController(MainMenuView mainMenuView) {
        this.mainMenuView = mainMenuView;
        this.searchService = new SearchService();
        this.updateService = new UpdateService();
    }

    public void registerEvent() {
        mainMenuView.getSearchField().setOnKeyReleased(event -> {
            String query = mainMenuView.getSearchField().getText().toLowerCase();
            System.out.println("Query: " + query);

            // Lấy danh sách gợi ý từ Trie
            List<String> filteredBooks = searchService.searchBookByPrefix(query);

            ObservableList<String> suggestionsList = FXCollections.observableArrayList(filteredBooks);
            mainMenuView.getSuggestions().setItems(suggestionsList);
            System.out.println("Suggestions: " + filteredBooks);
        });
        mainMenuView.getSuggestions().setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 1) {
                String selectedItem = mainMenuView.getSuggestions().getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    mainMenuView.getSearchField().setText(selectedItem);
                }
            }
        });
        mainMenuView.getSearchButton().setOnAction(event -> {
            if (mainMenuView.getSearchField().getText().isEmpty()) {
                mainMenuView.showErrorMessFill();
            } else {

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
}
