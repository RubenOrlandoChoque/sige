package com.smartapps.sigev2.models.typeemployee;

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

public class TypeEmployeeRepository {
    private TypeEmployeeDao typeEmployeeDao;

    TypeEmployeeRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        typeEmployeeDao = db.typeEmployeeDao();
    }

    public void insert(TypeEmployee typeEmployee) {
        typeEmployeeDao.insert(typeEmployee);
    }

    public LiveData<List<TypeEmployee>> getAll() {
        return typeEmployeeDao.getAll();
    }

    public Observable<JSONObject> rxDownload() {
        return new DownloadService().rxDownload("api/typeemployee/all");
    }

    public void save(JSONObject courseYears) {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
            Gson gson = gsonBuilder.create();

            JSONArray response = courseYears.getJSONArray("Result");
            if (response.length() > 0) {
                ArrayList<TypeEmployeeDao> items = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    TypeEmployee item = gson.fromJson(response.get(i).toString(), TypeEmployee.class);
                    typeEmployeeDao.insert(item);
                }
            }
        } catch (Exception e) {
            Log.e("-", e.getMessage());
        }
    }
}
