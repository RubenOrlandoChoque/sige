package com.smartapps.sigev2.models.studentconditions;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.smartapps.sigev2.models.documenttypes.DocumentTypes;
import com.smartapps.sigev2.models.documenttypes.DocumentTypesRepository;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observable;

public class StudentConditionsViewModel extends AndroidViewModel {
    private StudentConditionsRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.

    public StudentConditionsViewModel(Application application) {
        super(application);
        mRepository = new StudentConditionsRepository(application);
    }

    public void insert(StudentConditions studentCondition) {
        new Thread(() -> {
            mRepository.insert(studentCondition);
        }).start();
    }


    public LiveData<List<StudentConditions>> getAll() {
        return mRepository.getAll();
    }


    public Observable<JSONObject> rxDownload() {
        return mRepository.rxDownload();
    }

    public void save(JSONObject studentConditions) {
        mRepository.save(studentConditions);
    }
}
