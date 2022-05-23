package com.smartapps.sigev2.models.documentations;

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

public class DocumentationRepository {
    private DocumentationDao documentationDao;

    DocumentationRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        documentationDao = db.documentationDao();
    }

    public void insert(Documentation person) {
        documentationDao.insert(person);
    }

    public LiveData<List<Documentation>> getAll() {
        return documentationDao.getAll();
    }

    public Observable<JSONObject> rxDownload() {
        return new DownloadService().rxDownload("api/documentations/all");
    }

    public void save(JSONObject courseYears) {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
            Gson gson = gsonBuilder.create();

            JSONArray response = courseYears.getJSONArray("Result");
            if (response.length() > 0) {
                ArrayList<Documentation> items = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    Documentation item = gson.fromJson(response.get(i).toString(), Documentation.class);
                    documentationDao.insert(item);
                }
            }
        } catch (Exception e) {
            Log.e("-", e.getMessage());
        }
    }
}
