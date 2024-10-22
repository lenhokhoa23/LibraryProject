package org.example.libraryfxproject.Service;

import org.example.libraryfxproject.Dao.BookDAO;
import org.example.libraryfxproject.Model.TrieNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchService {
    BookDAO bookDAO = new BookDAO();

    public List<String> searchBookByPrefix(String prefix) {
        prefix = prefix.toUpperCase();
        List<String> results = new ArrayList<>();
        TrieNode current = bookDAO.getTrie().getRoot();
        for (char ch : prefix.toCharArray()) {
            System.out.println("Checking character: " + ch);
            System.out.println("Current node children: " + current.getChildren().keySet()); // In các key con của node hiện tại
            TrieNode node = current.getChildren().get(ch);
            if (node == null) {
                System.out.println("Character not found: " + ch); // In ra nếu không tìm thấy ký tự
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
}
