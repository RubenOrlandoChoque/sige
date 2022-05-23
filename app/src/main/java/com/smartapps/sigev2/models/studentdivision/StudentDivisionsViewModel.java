package com.smartapps.sigev2.models.studentdivision;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class StudentDivisionsViewModel extends AndroidViewModel {
    private StudentDivisionsRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.

    public StudentDivisionsViewModel(Application application) {
        super(application);
        mRepository = new StudentDivisionsRepository(application);
    }

    public void insert(StudentDivisions division) {
        new Thread(() -> {
            mRepository.insert(division);
        }).start();
    }


    public LiveData<List<StudentDivisions>> getAll() {
        return mRepository.getAll();
    }
}
