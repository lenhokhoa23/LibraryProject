package org.example.libraryfxproject.Controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.libraryfxproject.Model.Book;
import org.example.libraryfxproject.Service.BookService;
import org.example.libraryfxproject.Service.SearchService;
import org.example.libraryfxproject.View.AddBookView;

import javafx.scene.input.KeyCode;

import org.example.libraryfxproject.Service.UpdateService;
import org.example.libraryfxproject.View.LoginView;
import org.example.libraryfxproject.View.MainMenuView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.List;

public class MainMenuController {
    private final MainMenuView mainMenuView;
    private final SearchService searchService;
    private final BookService bookService;
    private final UpdateService updateService;
    private ObservableList<Book> observableBooks;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> searchTask;
    private final int rowsPerPage = 100; // Số lượng records trên 1 page
    private boolean isFilteredView = false;
    private final ContextMenuController contextMenuController;

    public MainMenuController(MainMenuView mainMenuView) {
        this.mainMenuView = mainMenuView;
        this.searchService = new SearchService();
        this.updateService = new UpdateService();
        this.bookService = new BookService();
        observableBooks = bookService.getAllBooks();
        loadCatalogData();
        initializePagination();
        contextMenuController = new ContextMenuController(mainMenuView.getCatalogTableView());
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
        mainMenuView.getAddItemButton().setOnAction(this::openAddBookView);
        mainMenuView.getModifyButton().setOnAction(event -> openModifyBookView());

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
        System.out.println("Loaded books: " + observableBooks.size());
        updateTableView(getPageData(0)); // Load the first page initially
    }

    private void initializePagination() {
        int pageCount = (int) Math.ceil((double) observableBooks.size() / rowsPerPage);
        mainMenuView.getCatalogPagination().setPageCount(pageCount);
        System.out.println("Total books: " + observableBooks.size());
        System.out.println("Total pages: " + pageCount);
        mainMenuView.getCatalogPagination().setPageFactory(this::createPage);

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

        updateService.updatePieChart(mainMenuView.getGenreCirculationChart());
        mainMenuView.getChartTitleLabel().setLayoutX(10);
        mainMenuView.getChartTitleLabel().setLayoutY(10);

        mainMenuView.getGenreCirculationChart().layoutXProperty().bind(
                mainMenuView.getChartPane().widthProperty().subtract(mainMenuView.getGenreCirculationChart().widthProperty()).divide(2)
        );
        mainMenuView.getGenreCirculationChart().layoutYProperty().bind(
                mainMenuView.getChartPane().heightProperty().subtract(mainMenuView.getGenreCirculationChart().heightProperty()).divide(2)
        );

        updateService.updateBarChart(mainMenuView.getGenreBorrowedBarChart());
        VBox.setVgrow(mainMenuView.getGenreBorrowedBarChart(), Priority.ALWAYS);

        mainMenuView.getActivityTimeColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
        mainMenuView.getActivityUserIDColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
        mainMenuView.getActivityUsernameColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));
        mainMenuView.getActivityBookTitleColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(3)));
        mainMenuView.getActivityISBNColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(4)));
        mainMenuView.getActivityDueColumn().setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(5)));
        updateService.populateTableView(mainMenuView.getRecentActivitiesTable(), 17);
        mainMenuView.getViewAllButton().setOnAction(event -> {
            updateService.populateTableView(mainMenuView.getRecentActivitiesTable(), 0);
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

    private Node createPage(int pageIndex) {
        ObservableList<Book> currentPageBooks = getPageData(pageIndex);
        TableView<Book> catalogTableView = mainMenuView.getCatalogTableView();
        catalogTableView.getItems().clear();
        catalogTableView.setItems(currentPageBooks);
        catalogTableView.scrollTo(0);
        VBox pageContainer = new VBox();
        pageContainer.getChildren().add(catalogTableView);
        return pageContainer;
    }

    private ObservableList<Book> getPageData(int pageIndex) {
        int start = pageIndex * rowsPerPage;
        int end = Math.min(start + rowsPerPage, observableBooks.size());
        return FXCollections.observableArrayList(observableBooks.subList(start, end));
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

    public void openModifyBookView() {
        Parent root = mainMenuView.initializeModifyBookView();
        if (root != null) {
            Stage popupStage = new Stage();
            popupStage.setTitle("Modify Book");
            popupStage.setScene(new Scene(root));
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.initOwner(this.mainMenuView.getStage());

            System.out.println(mainMenuView);
            mainMenuView.getUpdateButton().setOnAction(event -> {
                try {
                    String ISBN = mainMenuView.getIsbnField().getText();
                    String attribute = mainMenuView.getAttributeField().getText();
                    String newValue = mainMenuView.getNewValueField().getText();
                    if (ISBN.isEmpty() || attribute.isEmpty() || newValue.isEmpty()) {
                        System.out.println("Please fill in all fields!");
                    } else {
                        bookService.modifyBook(ISBN, attribute, newValue);
                        System.out.println("Book updated successfully!");
                        popupStage.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("An error occurred. Please check the input values.");
                }
            });
            mainMenuView.getBackButton().setOnAction(event -> popupStage.close());
            popupStage.showAndWait();
        }
    }


}
