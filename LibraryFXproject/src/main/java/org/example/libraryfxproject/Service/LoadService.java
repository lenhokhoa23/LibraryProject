package org.example.libraryfxproject.Service;

import org.example.libraryfxproject.Dao.GeneralDAO;

/** General loader for all DAO.*/
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