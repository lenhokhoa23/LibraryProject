package org.example.libraryfxproject.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.libraryfxproject.Dao.BookDAO;
import org.example.libraryfxproject.Model.Book;
import org.example.libraryfxproject.Service.BookService;
import org.example.libraryfxproject.Service.SearchService;
import org.example.libraryfxproject.View.AddBookView;
import org.example.libraryfxproject.View.MainMenuView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
public class MainMenuController {
    private final MainMenuView mainMenuView;
    private final SearchService searchService;
    private final BookDAO bookDAO;
    private final BookService bookService;
    HashMap<String, Book> booksMap;
    private ObservableList<Book> observableBooks;
    private final int rowsPerPage = 100; // Số lượng records trên 1 page
    private boolean isFilteredView = false;
    private final ContextMenuController contextMenuController;
    public MainMenuController(MainMenuView mainMenuView) {
        this.mainMenuView = mainMenuView;
        this.searchService = new SearchService();
        this.bookDAO = new BookDAO();
        this.bookService = new BookService();
        booksMap = bookService.loadData();
        loadCatalogData();
        initializePagination();
        contextMenuController = new ContextMenuController(mainMenuView.getCatalogTableView());
    }

    public void registerEvent() {
        mainMenuView.getSearchField().setOnKeyReleased(event -> {
            String query = mainMenuView.getSearchField().getText().toLowerCase();
            System.out.println("Query: " + query);
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
        mainMenuView.getAddItemButton().setOnAction(this::openAddBookView);
        mainMenuView.getRefreshButton().setOnAction(event -> {
            isFilteredView = false;
            loadCatalogData();
            initializePagination();
        });
        mainMenuView.getSearchToggle().setOnAction(event -> {
            CatalogEvent();
        });
    }

    public void loadCatalogData() {
        System.out.println("Loaded books: " + booksMap.size());
        ObservableList<Book> observableBooks = FXCollections.observableArrayList(booksMap.values());
        updateTableView(observableBooks);
    }

    private void initializePagination() {
        List<Book> bookList = new ArrayList<>(booksMap.values());
        observableBooks = FXCollections.observableArrayList(bookList);

        int pageCount = (int) Math.ceil((double) observableBooks.size() / rowsPerPage);
        mainMenuView.getCatalogPagination().setPageCount(pageCount);
        System.out.println("Total books: " + observableBooks.size());
        System.out.println("Total pages: " + pageCount);
        mainMenuView.getCatalogPagination().setPageFactory(pageIndex -> createPage(pageIndex));
    }

    private Node createPage(int pageIndex) {
        int start = pageIndex * rowsPerPage;
        int end = Math.min(start + rowsPerPage, observableBooks.size());
        ObservableList<Book> currentPageBooks = FXCollections.observableArrayList();
        if (start < observableBooks.size()) {
            currentPageBooks.addAll(observableBooks.subList(start, end));
        }
        TableView<Book> catalogTableView = mainMenuView.getCatalogTableView();
        catalogTableView.getItems().clear();
        catalogTableView.setItems(currentPageBooks);
        catalogTableView.scrollTo(0);
        VBox pageContainer = new VBox();
        pageContainer.getChildren().add(catalogTableView);
        return pageContainer;
    }

    public void updateTableView(ObservableList<Book> books) {
        mainMenuView.getCatalogTableView().getItems().clear();
        mainMenuView.getCatalogTableView().setItems(books);
    }

    public void CatalogEvent() {
        if (isFilteredView) {
            loadCatalogData();
            isFilteredView = false;
        } else {
            String searchType = mainMenuView.getFilterComboBox().getValue();
            String searchText = mainMenuView.getSearchCatalog().getText().toUpperCase();
            ObservableList<Book> filteredBooks = FXCollections.observableArrayList();
            if (searchType != null && !searchText.isEmpty()) {
                filteredBooks = searchService.searchBookByAttribute(searchType.toLowerCase(), searchText);
                updateTableView(filteredBooks);
                isFilteredView = true;
            }
        }
    }

    public void openAddBookView(ActionEvent event) {
        new AddBookView(mainMenuView.getStage());
    }

}
