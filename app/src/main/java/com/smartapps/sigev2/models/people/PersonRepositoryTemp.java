package com.smartapps.sigev2.models.people;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.smartapps.sigev2.database.RoomDatabaseTemp;

import java.util.List;

public class PersonRepositoryTemp {
    private PersonDao personDao;
    private LiveData<List<Person>> all;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public PersonRepositoryTemp(Application application) {
        RoomDatabaseTemp db = RoomDatabaseTemp.getDatabase(application);
        personDao = db.personDao();
        all = personDao.getAll();
    }

    public Person getByDni(String documentNumber) {
        return personDao.getByDni(documentNumber);
    }

    public Person getById(String id) {
        return personDao.getById(id);
    }

    public void insert(Person person) {
        personDao.insert(person);
    }

    public void update(Person person) {
        personDao.update(person);
    }

    LiveData<List<Person>> getAll() {
        return all;
    }

    public void deleteAll() {
        personDao.deleteAll();
    }
    public void deleteAllExcept(List<String> keepData) {
        personDao.deleteAllExcept(keepData);
    }

    public void delete(String id) {
        personDao.delete(id);
    }
}
