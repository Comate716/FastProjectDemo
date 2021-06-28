package com.fastproject.demo.data.local;

import com.fastproject.demo.app.AppApplication;
import com.fastproject.demo.app.AppExecutors;


public class DbManager {

    private static final String DATABASE_NAME = AppApplication.dbPath + "MobileWorkOrder.db";
    private static  DbManager INSTANCE = null;
//    private static AppDatabase appDatabase = null;
    private static AppExecutors executors = null;

    private DbManager() {
    }

    public static  DbManager getInstance() {
        executors = AppApplication.getInstance().getAppExecutors();
        if (INSTANCE == null) {
            synchronized (DbManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new  DbManager();
                }
            }
        }
        return INSTANCE;
    }
}
