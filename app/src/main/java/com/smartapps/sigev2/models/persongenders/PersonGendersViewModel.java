package com.smartapps.sigev2.models.persongenders;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observable;

public class PersonGendersViewModel extends AndroidViewModel {
    private PersonGendersRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.

    public PersonGendersViewModel(Application application) {
        super(application);
        mRepository = new PersonGendersRepository(application);
    }

    public void insert(PersonGenders personGender) {
        new Thread(() -> {
            mRepository.insert(personGender);
        }).start();
    }


    public LiveData<List<PersonGenders>> getAll() {
        return mRepository.getAll();
    }


    public Observable<JSONObject> rxDownload() {
        return mRepository.rxDownload();
    }

    public void save(JSONObject personGenders) {
        mRepository.save(personGenders);
    }
}
