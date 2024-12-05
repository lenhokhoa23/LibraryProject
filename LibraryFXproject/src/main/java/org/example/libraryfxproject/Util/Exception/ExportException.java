package org.example.libraryfxproject.Util.Exception;

public class ExportException extends Exception {

    // Custom exception for export file.
    public ExportException(String message) {
        super(message);
    }

    public ExportException(String message, Throwable cause) {
        super(message, cause);
    }
}
