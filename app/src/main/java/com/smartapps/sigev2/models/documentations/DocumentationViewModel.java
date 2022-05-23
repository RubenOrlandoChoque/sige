package com.smartapps.sigev2.models.documentations;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observable;

public class DocumentationViewModel extends AndroidViewModel {
    private DocumentationRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.

    public DocumentationViewModel(Application application) {
        super(application);
        mRepository = new DocumentationRepository(application);
    }

    public void insert(Documentation documentation) {
        new Thread(() -> {
            mRepository.insert(documentation);
        }).start();
    }


    public LiveData<List<Documentation>> getAll() {
        return mRepository.getAll();
    }


    public Observable<JSONObject> rxDownload() {
        return mRepository.rxDownload();
    }

    public void save(JSONObject documentation) {
        mRepository.save(documentation);
    }
}
