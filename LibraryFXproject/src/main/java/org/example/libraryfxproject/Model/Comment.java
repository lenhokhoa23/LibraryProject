package org.example.libraryfxproject.Model;

import java.time.LocalDateTime;

public class Comment {
    private String author;
    private String content;
    private LocalDateTime timestamp;

    public Comment(String author, String content) {
        this.author = author;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    public Comment(String author, String content, LocalDateTime timestamp) {
        this.author = author;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
