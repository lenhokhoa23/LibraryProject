package org.example.libraryfxproject.Service;

import org.example.libraryfxproject.Dao.AccountDAO;
import org.example.libraryfxproject.Dao.BookDAO;
import org.example.libraryfxproject.Dao.DatabaseConnection;
import org.example.libraryfxproject.Dao.GeneralDAO;
import org.example.libraryfxproject.Model.Account;

public class LoadService {
    private static LoadService loadService;
    private LoadService() {
    }
    public static synchronized LoadService getInstance() {
        if (loadService == null) {
            loadService = new LoadService();
        }
        return loadService;
    }

    public static void loadData(GeneralDAO<?, ?> generalDao) {
        generalDao.loadData();
    }

}