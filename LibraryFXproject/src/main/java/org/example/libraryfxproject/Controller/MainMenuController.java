package org.example.libraryfxproject.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseEvent;
import org.example.libraryfxproject.Service.SearchService;
import org.example.libraryfxproject.View.MainMenuView;

import java.util.List;

public class MainMenuController {
    private final MainMenuView mainMenuView;
    private final SearchService searchService;

    public MainMenuController(MainMenuView mainMenuView) {
        this.mainMenuView = mainMenuView;
        this.searchService = new SearchService();
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

    }
}
