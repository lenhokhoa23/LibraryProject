package org.example.libraryfxproject.Export;

import org.example.libraryfxproject.Util.Exception.ExportException;

import java.util.List;

public interface DataExporter {
    void exportData(List<?> data, String filePath) throws ExportException;
    String getFileExtension();
}
