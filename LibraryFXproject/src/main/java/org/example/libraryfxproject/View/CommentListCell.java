package org.example.libraryfxproject.View;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.libraryfxproject.Model.Comment;

import java.time.format.DateTimeFormatter;

public class CommentListCell extends ListCell<Comment> {
    private VBox content;
    private Label authorLabel;
    private Label commentLabel;
    private Label timestampLabel;

    public CommentListCell() {
        super();
        authorLabel = new Label();
        commentLabel = new Label();
        commentLabel.setWrapText(true);
        timestampLabel = new Label();
        content = new VBox(5, new HBox(10, authorLabel, timestampLabel), commentLabel);
        content.setPadding(new Insets(10));
        authorLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #3498db; -fx-font-size: 14px;");
        commentLabel.setStyle("-fx-wrap-text: true; -fx-text-fill: #2c3e50; -fx-font-size: 13px;");
        timestampLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 11px;");
        content.setStyle(
                "-fx-background-color: white; " +
                        "-fx-padding: 12px; " +
                        "-fx-border-radius: 8px; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3); " +
                        "-fx-border-color: #ecf0f1; " +
                        "-fx-border-width: 1px;"
        );
    }

    @Override
    protected void updateItem(Comment comment, boolean empty) {
        super.updateItem(comment, empty);
        if (empty || comment == null) {
            setGraphic(null);
        } else {
            authorLabel.setText(comment.getAuthor());
            commentLabel.setText(comment.getContent());
            timestampLabel.setText(comment.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            setGraphic(content);
        }
    }
}
