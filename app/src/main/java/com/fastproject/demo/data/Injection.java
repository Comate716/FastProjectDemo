package com.fastproject.demo.data;

import android.app.Application;

import com.fastproject.demo.data.local.LocalDataSource;
import com.fastproject.demo.data.remote.RemoteDataSource;


public class Injection {
    public static DataRepository getDataRepository(Application application) {
        return DataRepository.getInstance(RemoteDataSource.getInstance(),
                LocalDataSource.getInstance(), application);
    }
}
