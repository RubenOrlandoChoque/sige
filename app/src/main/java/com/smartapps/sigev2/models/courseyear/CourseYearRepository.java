package com.smartapps.sigev2.models.courseyear;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.smartapps.sigev2.classes.SharedConfig;
import com.smartapps.sigev2.database.RoomDatabase;
import com.smartapps.sigev2.models.courseyear.pojo.CourseYearAndAllDivisions;
import com.smartapps.sigev2.models.courseyear.pojo.CourseYearStudentsCount;
import com.smartapps.sigev2.models.students.pojo.StudentCountData;
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
import io.reactivex.Single;

public class CourseYearRepository {
    private CourseYearDao courseYearDao;

    CourseYearRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        courseYearDao = db.courseYearDao();
    }

    public void insert(CourseYear person) {
        courseYearDao.insert(person);
    }

    public LiveData<List<CourseYear>> getAll() {
        return courseYearDao.getAll();
    }

    public Single<List<CourseYear>> getAllSingle() {
        return courseYearDao.getAllSingle();
    }

    public LiveData<List<CourseYearStudentsCount>> getAllWithStudentsCount(int shiftId) {
        return courseYearDao.getAllWithStudentsCount(shiftId, SharedConfig.getCurrentSchoolYearId());
    }

    public LiveData<List<StudentCountData>> GetAllCountStudents(int courseYearId, int shiftId) {
        return courseYearDao.GetAllCountStudents(courseYearId, shiftId, SharedConfig.getCurrentSchoolYearId());
    }

    public LiveData<List<CourseYearAndAllDivisions>> getCourseYearsDivisionsByShiftId(int shiftId) {
        return courseYearDao.getCourseYearsDivisionsByShiftId(shiftId, SharedConfig.getCurrentSchoolYearId());
    }

    public Observable<JSONObject> rxDownload() {
        return new DownloadService().rxDownload("api/courseyear/all");
    }

    public void save(JSONObject courseYears) {
        try {
            courseYearDao.deleteAll();

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
            Gson gson = gsonBuilder.create();

            JSONArray response = courseYears.getJSONArray("Result");
            if (response.length() > 0) {
                ArrayList<CourseYear> items = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    CourseYear item = gson.fromJson(response.get(i).toString(), CourseYear.class);
                    courseYearDao.insert(item);
                }
            }
        } catch (Exception e) {
            Log.e("-", e.getMessage());
        }
    }
}
