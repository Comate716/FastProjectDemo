package com.fastproject.demo.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.fastproject.demo.data.local.LocalDataSource;
import com.fastproject.demo.data.remote.RemoteDataSource;
import com.fastproject.demo.data.remote.model.TestData;

import java.util.List;


public class DataRepository {

    private static DataRepository INSTANCE = null;

    private static Application app = null;


    private final RemoteDataSource mRemoteDataSource;

    private final LocalDataSource mLocalDataSource;

    private DataRepository(@NonNull RemoteDataSource remoteDataSource, @NonNull LocalDataSource localDataSource) {
        mRemoteDataSource = remoteDataSource;
        mLocalDataSource = localDataSource;
    }

    static DataRepository getInstance(@NonNull RemoteDataSource remoteDataSource, @NonNull LocalDataSource localDataSource, Application application) {
        if (INSTANCE == null) {
            synchronized (DataRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DataRepository(remoteDataSource, localDataSource);
                    app = application;
                }
            }
        }
        return INSTANCE;
    }


    //网络更新test
    public MutableLiveData<List<TestData.TestDataChildBean>> getRemoteTestDemo(String info) {
        return mRemoteDataSource.getNetTest(info);
    }

}
