package com.smartapps.sigev2.models.shifts;

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

public class ShiftRepository {
    private ShiftDao shiftDao;

    ShiftRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        shiftDao = db.shiftDao();
    }

    public void insert(Shift shift) {
        shiftDao.insert(shift);
    }

//    public LiveData<List<RelationshipData>> getAll() {
//        return relationshipDao.getAll();
//    }

    public LiveData<List<Shift>> getAll() {
        return shiftDao.getAll();
    }

    public Observable<JSONObject> rxDownload() {
        return new DownloadService().rxDownload("api/shifts/all");
    }

    public void save(JSONObject courseYears) {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
            Gson gson = gsonBuilder.create();

            JSONArray response = courseYears.getJSONArray("Result");
            if (response.length() > 0) {
                ArrayList<Shift> items = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    Shift item = gson.fromJson(response.get(i).toString(), Shift.class);
                    shiftDao.insert(item);
                }
            }
        } catch (Exception e) {
            Log.e("-", e.getMessage());
        }
    }
}
