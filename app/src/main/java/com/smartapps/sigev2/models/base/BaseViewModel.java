package com.smartapps.sigev2.models.base;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import io.reactivex.Completable;

public class BaseViewModel extends AndroidViewModel {
    private BaseRepository mRepository;

    public BaseViewModel(Application application) {
        super(application);
        mRepository = new BaseRepository(application);
    }

    public Completable seed() {
        return mRepository.seed();
    }
}
