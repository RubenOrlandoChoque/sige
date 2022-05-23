package com.smartapps.sigev2.models.educationlevels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smartapps.sigev2.database.RoomDatabase;
import com.smartapps.sigev2.models.educationlevels.pojo.EducationLevelData;
import com.smartapps.sigev2.services.DownloadService;
import com.smartapps.sigev2.util.DateDeserializer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;

public class EducationLevelsRepository {
    private EducationLevelsDao educationLevelsDao;

    EducationLevelsRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        educationLevelsDao = db.educationLevelsDao();
    }

    public void insert(EducationLevels educLevel) {
        educationLevelsDao.insert(educLevel);
    }

    public LiveData<List<EducationLevelData>> getAll() {
        return educationLevelsDao.getAll();
    }

    public Observable<JSONObject> rxDownload() {
        return new DownloadService().rxDownload("api/educationlevels/all");
    }

    public void save(JSONObject courseYears) {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
            Gson gson = gsonBuilder.create();

            JSONArray response = courseYears.getJSONArray("Result");
            if (response.length() > 0) {
                ArrayList<EducationLevels> items = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    EducationLevels item = gson.fromJson(response.get(i).toString(), EducationLevels.class);
                    educationLevelsDao.insert(item);
                }
            }
        } catch (Exception e) {
            Log.e("-", e.getMessage());
        }
    }
}

