package org.example.libraryfxproject.Service;

import org.example.libraryfxproject.Dao.DatabaseConnection;
import org.example.libraryfxproject.Dao.GeneralDao;
import org.example.libraryfxproject.Model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class LoadService {
    public static void loadData(GeneralDao<?, ?> generalDao) {
        generalDao.loadData();
    }
}
