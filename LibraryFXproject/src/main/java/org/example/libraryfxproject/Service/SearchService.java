package org.example.libraryfxproject.Service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.libraryfxproject.Dao.BookDAO;
import org.example.libraryfxproject.Dao.UserDAO;
import org.example.libraryfxproject.Model.Book;
import org.example.libraryfxproject.Model.TrieNode;
import org.example.libraryfxproject.Model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchService {
    private final BookDAO bookDAO;
    private final UserDAO userDAO;
    private static SearchService searchService;

    private SearchService() {
        userDAO = UserDAO.getInstance();
        bookDAO = BookDAO.getInstance();
    }

    public static synchronized SearchService getInstance() {
        if (searchService == null) {
            searchService = new SearchService();
        }
        return searchService;
    }

    /**
     * Tìm kiếm sách theo tiền tố (prefix) trong danh sách sách.
     * @param prefix Tiền tố cần tìm kiếm
     * @return Danh sách các tên sách tìm được
     */
    public List<String> searchBookByPrefix(String prefix) {
        prefix = prefix.toUpperCase();
        List<String> results = new ArrayList<>();
        TrieNode current = bookDAO.getTrie().getRoot();
        for (char ch : prefix.toCharArray()) {
            TrieNode node = current.getChildren().get(ch);
            if (node == null) {
                return results;
            }
            current = node;
        }
        findAllWordsOfBook(current, new StringBuilder(prefix), results);
        return results;
    }

    /**
     * Tìm tất cả các từ (tên sách) trong trie bắt đầu với tiền tố đã cho.
     * @param node    Node hiện tại trong trie
     * @param prefix  Tiền tố đã cho
     * @param results Danh sách kết quả tìm được
     */
    private void findAllWordsOfBook(TrieNode node, StringBuilder prefix, List<String> results) {
        if (node.isEndOfWord()) {
            results.add(prefix.toString());
        }
        for (Map.Entry<Character, TrieNode> entry : node.getChildren().entrySet()) {
            prefix.append(entry.getKey());
            findAllWordsOfBook(entry.getValue(), prefix, results);
            prefix.deleteCharAt(prefix.length() - 1);  // Backtrack
        }
    }

    /**
     * Tìm kiếm sách theo thuộc tính và giá trị đã cho.
     * @param attribute Tên thuộc tính của sách
     * @param value     Giá trị của thuộc tính cần tìm
     * @return Danh sách sách tìm được dưới dạng ObservableList
     */
    public ObservableList<Book> searchBookByAttribute(String attribute, String value) {
        List<Book> books = bookDAO.findBooksByAttribute(attribute, value);
        return FXCollections.observableArrayList(books);
    }

    /**
     * Tìm kiếm người dùng theo tên người dùng.
     * @param username Tên người dùng cần tìm
     * @return Danh sách người dùng tìm được dưới dạng ObservableList
     */
    public ObservableList<User> searchUserByUsername(String username) {
        List<User> users = userDAO.findUserByComponentOfUserName(username);
        return FXCollections.observableArrayList(users);
    }
}
