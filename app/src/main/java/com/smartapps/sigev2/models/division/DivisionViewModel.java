package com.smartapps.sigev2.models.division;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observable;

public class DivisionViewModel extends AndroidViewModel {
    private DivisionRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.

    public DivisionViewModel(Application application) {
        super(application);
        mRepository = new DivisionRepository(application);
    }

    public void insert(Division division) {
        new Thread(() -> {
            mRepository.insert(division);
        }).start();
    }

    public LiveData<List<Division>> getAll() {
        return mRepository.getAll();
    }

    public Observable<JSONObject> rxDownload() {
        return mRepository.rxDownload();
    }

    public void save(JSONObject divisions) {
        mRepository.save(divisions);
    }
}
