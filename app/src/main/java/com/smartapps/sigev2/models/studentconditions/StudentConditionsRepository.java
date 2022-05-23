package com.smartapps.sigev2.models.studentconditions;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smartapps.sigev2.database.RoomDatabase;
import com.smartapps.sigev2.models.documenttypes.DocumentTypes;
import com.smartapps.sigev2.models.documenttypes.DocumentTypesDao;
import com.smartapps.sigev2.services.DownloadService;
import com.smartapps.sigev2.util.DateDeserializer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;

public class StudentConditionsRepository {
    private StudentConditionsDao studentConditionsDao;

    StudentConditionsRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        studentConditionsDao = db.studentConditionsDao();
    }

    public void insert(StudentConditions studentCondition) {
        studentConditionsDao.insert(studentCondition);
    }

    public LiveData<List<StudentConditions>> getAll() {
        return studentConditionsDao.getAll();
    }

    public Observable<JSONObject> rxDownload() {
        return new DownloadService().rxDownload("api/studentconditions/all");
    }

    public void save(JSONObject studentConditions) {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
            Gson gson = gsonBuilder.create();

            JSONArray response = studentConditions.getJSONArray("Result");
            if (response.length() > 0) {
                ArrayList<StudentConditions> items = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    StudentConditions item = gson.fromJson(response.get(i).toString(), StudentConditions.class);
                    studentConditionsDao.insert(item);
                }
            }
        } catch (Exception e) {
            Log.e("-", e.getMessage());
        }
    }
}


