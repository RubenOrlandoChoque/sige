package com.smartapps.sigev2.models.people;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.smartapps.sigev2.database.RoomDatabase;

import java.util.List;

public class PersonRepository {
    private PersonDao personDao;
    private LiveData<List<Person>> all;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public PersonRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
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
}
