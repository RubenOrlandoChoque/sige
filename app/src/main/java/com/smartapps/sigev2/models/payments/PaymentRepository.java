package com.smartapps.sigev2.models.payments;

import android.app.Application;

import com.smartapps.sigev2.database.RoomDatabase;

public class PaymentRepository {
    private PaymentDao paymentDao;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public PaymentRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
//        paymentDao = db.paymentDao();
    }
}
