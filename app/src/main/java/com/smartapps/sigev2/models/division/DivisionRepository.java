package com.smartapps.sigev2.models.division;

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

public class DivisionRepository {
    private DivisionDao divisionDao;

    DivisionRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        divisionDao = db.divisionDao();
    }

    public void insert(Division person) {
        divisionDao.insert(person);
    }

    public LiveData<List<Division>> getAll() {
        return divisionDao.getAll();
    }

    public Observable<JSONObject> rxDownload() {
        return new DownloadService().rxDownload("api/divisions/allMobile");
    }

    public void save(JSONObject courseYears) {
        try {
            divisionDao.deleteAll();

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
            Gson gson = gsonBuilder.create();

            JSONArray response = courseYears.getJSONArray("Result");
            if (response.length() > 0) {
                ArrayList<Division> items = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    Division item = gson.fromJson(response.get(i).toString(), Division.class);
                    divisionDao.insert(item);
                }
            }
        } catch (Exception e) {
            Log.e("-", e.getMessage());
        }
    }
}
