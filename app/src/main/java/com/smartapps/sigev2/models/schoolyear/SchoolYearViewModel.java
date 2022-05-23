package com.smartapps.sigev2.models.schoolyear;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observable;

public class SchoolYearViewModel extends AndroidViewModel {
    private SchoolYearRepository mRepository;

    public SchoolYearViewModel(Application application) {
        super(application);
        mRepository = new SchoolYearRepository(application);
    }

    public Observable<JSONObject> rxDownload() {
        return mRepository.rxDownload();
    }

    public void save(JSONObject students) {
        mRepository.save(students);
    }

    public LiveData<List<SchoolYear>> getAll() {
        return mRepository.getAll();
    }
}
