package com.smartapps.sigev2.models.educationlevels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.smartapps.sigev2.models.educationlevels.pojo.EducationLevelData;
import com.smartapps.sigev2.models.persongenders.PersonGenders;
import com.smartapps.sigev2.models.persongenders.PersonGendersRepository;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observable;

public class EducationLevelsViewModel extends AndroidViewModel {
    private EducationLevelsRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.

    public EducationLevelsViewModel(Application application) {
        super(application);
        mRepository = new EducationLevelsRepository(application);
    }

    public void insert(EducationLevels educationLevels) {
        new Thread(() -> {
            mRepository.insert(educationLevels);
        }).start();
    }


    public LiveData<List<EducationLevelData>> getAll() {
        return mRepository.getAll();
    }


    public Observable<JSONObject> rxDownload() {
        return mRepository.rxDownload();
    }

    public void save(JSONObject educationLevels) {
        mRepository.save(educationLevels);
    }
}

