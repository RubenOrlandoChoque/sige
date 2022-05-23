package com.smartapps.sigev2.models.schoolyear;

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

import java.util.Date;
import java.util.List;

import io.reactivex.Observable;

public class SchoolYearRepository {
    private SchoolYearDao schoolYearDao;

    SchoolYearRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        schoolYearDao = db.schoolYearDao();
    }

    public Observable<JSONObject> rxDownload() {
        return new DownloadService().rxDownload("api/schoolyears/allMobile");
    }

    public void save(JSONObject courseYears) {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
            Gson gson = gsonBuilder.create();

            JSONArray response = courseYears.getJSONArray("Result");
            if (response.length() > 0) {
                for (int i = 0; i < response.length(); i++) {
                    SchoolYear item = gson.fromJson(response.get(i).toString(), SchoolYear.class);
                    schoolYearDao.insert(item);
                }
            }
        } catch (Exception e) {
            Log.e("-", e.getMessage());
        }
    }

    public LiveData<List<SchoolYear>> getAll() {
        return schoolYearDao.getAll();
    }
}
