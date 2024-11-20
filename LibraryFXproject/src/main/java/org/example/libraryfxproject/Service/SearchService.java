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
    private final BookDAO bookDAO = new BookDAO();
    private final UserDAO userDAO = new UserDAO();
    private static SearchService searchService;
    private SearchService() {}
    // synchronized giup ho tro da luong khong tao ra nhieu thuc the
    public static synchronized SearchService getInstance() {
        if (searchService == null) {
            searchService = new SearchService();
        }
        return searchService;
    }


    public List<String> searchBookByPrefix(String prefix) {
        prefix = prefix.toUpperCase();
        List<String> results = new ArrayList<>();
        TrieNode current = bookDAO.getTrie().getRoot();
        for (char ch : prefix.toCharArray()) {
            System.out.println("Checking character: " + ch);
            System.out.println("Current node children: " + current.getChildren().keySet());
            TrieNode node = current.getChildren().get(ch);
            if (node == null) {
                System.out.println("Character not found: " + ch);
                return results;
            }
            current = node;
        }
        findAllWordsOfBook(current, new StringBuilder(prefix), results);
        return results;
    }

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

    public ObservableList<Book> searchBookByAttribute(String attribute, String value) {
        List<Book> books = bookDAO.findBooksByAttribute(attribute, value);
        return FXCollections.observableArrayList(books);
    }

    public ObservableList<User> searchUserByUsername(String username) {
        List<User> users = userDAO.findUserByComponentOfUserName(username);
        return FXCollections.observableArrayList(users);
    }

}
