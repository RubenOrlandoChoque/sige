package com.smartapps.sigev2.models.persongenders;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.smartapps.sigev2.database.RoomDatabase;
import com.smartapps.sigev2.services.DownloadService;
import com.smartapps.sigev2.util.DateDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;

public class PersonGendersRepository {
    private PersonGendersDao personGendersDao;

    PersonGendersRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        personGendersDao = db.personGendersDao();
    }

    public void insert(PersonGenders person) {
        personGendersDao.insert(person);
    }

    public LiveData<List<PersonGenders>> getAll() {
        return personGendersDao.getAll();
    }

    public Observable<JSONObject> rxDownload() {
        return new DownloadService().rxDownload("api/persongenders/all");
    }

    public void save(JSONObject courseYears) {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
            Gson gson = gsonBuilder.create();

            JSONArray response = courseYears.getJSONArray("Result");
            if (response.length() > 0) {
                ArrayList<PersonGenders> items = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    PersonGenders item = gson.fromJson(response.get(i).toString(), PersonGenders.class);
                    personGendersDao.insert(item);
                }
            }
        } catch (Exception e) {
            Log.e("-", e.getMessage());
        }
    }
}
