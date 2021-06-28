package com.fastproject.demo.data.local;

public class LocalDataSource {

    private static   LocalDataSource INSTANCE = null;
    private   DbManager dbManager;

    private LocalDataSource() {
        dbManager =   DbManager.getInstance();
    }

    public static   LocalDataSource getInstance() {
        if (INSTANCE == null) {
            synchronized (  LocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new   LocalDataSource();
                }
            }
        }
        return INSTANCE;
    }
}
