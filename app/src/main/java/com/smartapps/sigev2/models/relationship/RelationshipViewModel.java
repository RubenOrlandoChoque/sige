package com.smartapps.sigev2.models.relationship;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.smartapps.sigev2.models.relationship.pojo.RelationshipData;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observable;

public class RelationshipViewModel extends AndroidViewModel {
    private RelationshipRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.

    public RelationshipViewModel(Application application) {
        super(application);
        mRepository = new RelationshipRepository(application);
    }

    public void insert(Relationship relationship) {
        new Thread(() -> {
            mRepository.insert(relationship);
        }).start();
    }


    public LiveData<List<RelationshipData>> getAll() {
        return mRepository.getAll();
    }

    public Observable<JSONObject> rxDownload() {
        return mRepository.rxDownload();
    }

    public void save(JSONObject courseYears) {
        mRepository.save(courseYears);
    }
}
