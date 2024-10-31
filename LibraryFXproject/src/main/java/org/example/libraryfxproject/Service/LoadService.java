package org.example.libraryfxproject.Service;

import org.example.libraryfxproject.Dao.AccountDAO;
import org.example.libraryfxproject.Dao.BookDAO;
import org.example.libraryfxproject.Dao.DatabaseConnection;
import org.example.libraryfxproject.Dao.GeneralDao;
import org.example.libraryfxproject.Model.Account;

public class LoadService {
    private final BookDAO bookDAO = new BookDAO();

    public static void loadData(GeneralDao<?, ?> generalDao) {
        generalDao.loadData();
    }

}