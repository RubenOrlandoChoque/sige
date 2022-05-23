package com.smartapps.sigev2.models.relationship;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.smartapps.sigev2.database.RoomDatabase;
import com.smartapps.sigev2.models.relationship.pojo.RelationshipData;
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

public class RelationshipRepository {
    private RelationshipDao relationshipDao;

    RelationshipRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        relationshipDao = db.relationshipDao();
    }

    public void insert(Relationship person) {
        relationshipDao.insert(person);
    }

    public LiveData<List<RelationshipData>> getAll() {
        return relationshipDao.getAll();
    }

    public Observable<JSONObject> rxDownload() {
        return new DownloadService().rxDownload("api/relationships/all");
    }

    public void save(JSONObject courseYears) {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
            Gson gson = gsonBuilder.create();

            JSONArray response = courseYears.getJSONArray("Result");
            if (response.length() > 0) {
                ArrayList<Relationship> items = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    Relationship item = gson.fromJson(response.get(i).toString(), Relationship.class);
                    relationshipDao.insert(item);
                }
            }
        } catch (Exception e) {
            Log.e("-", e.getMessage());
        }
    }
}
