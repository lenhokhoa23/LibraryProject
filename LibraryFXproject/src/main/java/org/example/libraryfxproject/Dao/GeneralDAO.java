package org.example.libraryfxproject.Dao;

import java.sql.Connection;
import java.util.HashMap;

public abstract class GeneralDAO<K, V> {
    protected HashMap<K, V> dataMap = new HashMap<>();
    protected final Connection connection = DatabaseConnection.getConnection();

    public abstract void loadData();

    public HashMap<K, V> getDataMap() {
        return dataMap;
    }

}
