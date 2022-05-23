package com.smartapps.sigev2.models.people;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Single;

public class PersonViewModel extends AndroidViewModel {
    private PersonRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.

    public PersonViewModel(Application application) {
        super(application);
        mRepository = new PersonRepository(application);
    }

    public LiveData<List<Person>> getAll() {
        return mRepository.getAll();
    }

    public Single<Person> getByDni(String documentNumber) {
        return Single.create(emitter -> {
            // Save Student
            try {
                Person person = mRepository.getByDni(documentNumber);
                emitter.onSuccess(person);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }
}
