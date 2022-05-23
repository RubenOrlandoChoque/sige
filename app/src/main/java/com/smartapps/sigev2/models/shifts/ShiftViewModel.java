package com.smartapps.sigev2.models.shifts;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observable;

public class ShiftViewModel extends AndroidViewModel {
    private ShiftRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.

    public ShiftViewModel(Application application) {
        super(application);
        mRepository = new ShiftRepository(application);
    }

    public void insert(Shift shift) {
        new Thread(() -> {
            mRepository.insert(shift);
        }).start();
    }

    public Observable<JSONObject> rxDownload() {
        return mRepository.rxDownload();
    }

    public void save(JSONObject shift) {
        mRepository.save(shift);
    }

    public LiveData<List<Shift>> getAll() {
        return mRepository.getAll();
    }
}

