package com.smartapps.sigev2.models.payments;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

public class PaymentViewModel extends AndroidViewModel {
    private PaymentRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.

    public PaymentViewModel(Application application) {
        super(application);
        mRepository = new PaymentRepository(application);
    }

}
