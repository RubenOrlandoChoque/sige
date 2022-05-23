package com.smartapps.sigev2.models.employeejobtitles;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.smartapps.sigev2.database.RoomDatabase;

import java.util.List;

public class EmployeeJobTitleRepository {
    private EmployeeJobTitleDao employeeJobTitleDao;
    private LiveData<List<EmployeeJobTitle>> all;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public EmployeeJobTitleRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        employeeJobTitleDao = db.employeeJobTitleDao();
        all = employeeJobTitleDao.getAll();
    }

    public EmployeeJobTitle getById(int id) {
        return employeeJobTitleDao.getById(id);
    }

    public EmployeeJobTitle get(String employeeId, int shiftId) {
        return employeeJobTitleDao.get(employeeId, shiftId);
    }

    public void insert(EmployeeJobTitle employeeJobTitle) {
        employeeJobTitleDao.insert(employeeJobTitle);
    }

    public void update(EmployeeJobTitle person) {
        employeeJobTitleDao.update(person);
    }

    LiveData<List<EmployeeJobTitle>> getAll() {
        return all;
    }
}
