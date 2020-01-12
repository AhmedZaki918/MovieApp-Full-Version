package com.example.android.moviesapp.database;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.example.android.moviesapp.model.AllData;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    /**
     * Wrapping the <list<AllData> with LiveData
     * to avoid requiring the data every time
     **/
    private LiveData<List<AllData>> mAllDataList;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase dataBase = AppDatabase.getInstance(this.getApplication());
        mAllDataList = dataBase.movieDao().loadAllResults();
    }

    public LiveData<List<AllData>> getAllDataList() {
        return mAllDataList;
    }
}