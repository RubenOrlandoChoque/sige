package com.smartapps.sigev2.models.documenttypes;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smartapps.sigev2.database.RoomDatabase;
import com.smartapps.sigev2.services.DownloadService;
import com.smartapps.sigev2.util.DateDeserializer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;

public class DocumentTypesRepository {
    private DocumentTypesDao documentTypesDao;

    DocumentTypesRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        documentTypesDao = db.documentTypesDao();
    }

    public void insert(DocumentTypes docType) {
        documentTypesDao.insert(docType);
    }

    public LiveData<List<DocumentTypes>> getAll() {
        return documentTypesDao.getAll();
    }

    public Observable<JSONObject> rxDownload() {
        return new DownloadService().rxDownload("api/documenttypes/all");
    }

    public void save(JSONObject documentTypes) {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
            Gson gson = gsonBuilder.create();

            JSONArray response = documentTypes.getJSONArray("Result");
            if (response.length() > 0) {
                ArrayList<DocumentTypes> items = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    DocumentTypes item = gson.fromJson(response.get(i).toString(), DocumentTypes.class);
                    documentTypesDao.insert(item);
                }
            }
        } catch (Exception e) {
            Log.e("-", e.getMessage());
        }
    }
}
