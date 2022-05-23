package com.smartapps.sigev2.models.typeemployee;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observable;

public class TypeEmployeeViewModel extends AndroidViewModel {
    private TypeEmployeeRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.

    public TypeEmployeeViewModel(Application application) {
        super(application);
        mRepository = new TypeEmployeeRepository(application);
    }

    public void insert(TypeEmployee typeEmployee) {
        new Thread(() -> {
            mRepository.insert(typeEmployee);
        }).start();
    }


    public LiveData<List<TypeEmployee>> getAll() {
        return mRepository.getAll();
    }


    public Observable<JSONObject> rxDownload() {
        return mRepository.rxDownload();
    }

    public void save(JSONObject typeEmployees) {
        mRepository.save(typeEmployees);
    }
}
