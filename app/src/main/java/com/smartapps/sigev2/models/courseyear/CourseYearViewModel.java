package com.smartapps.sigev2.models.courseyear;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.smartapps.sigev2.models.courseyear.pojo.CourseYearAndAllDivisions;
import com.smartapps.sigev2.models.courseyear.pojo.CourseYearStudentsCount;
import com.smartapps.sigev2.models.students.pojo.StudentCountData;

import org.json.JSONObject;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.Single;

public class CourseYearViewModel extends AndroidViewModel {
    private CourseYearRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.

    public CourseYearViewModel(Application application) {
        super(application);
        mRepository = new CourseYearRepository(application);
    }

    public void insert(CourseYear courseYear) {
        new Thread(() -> {
            mRepository.insert(courseYear);
        }).start();
    }


    public LiveData<List<CourseYear>> getAll() {
        return mRepository.getAll();
    }

    public Single<List<CourseYear>> getAllSingle() {
        return mRepository.getAllSingle();
    }

    public LiveData<List<CourseYearStudentsCount>> getAllWithStudentsCount(int shiftId) {
        return mRepository.getAllWithStudentsCount(shiftId);
    }

    public LiveData<List<StudentCountData>> getAllCountStudents(int courseYearId, int shiftId) {
        return mRepository.GetAllCountStudents(courseYearId,shiftId);
    }

    public LiveData<List<CourseYearAndAllDivisions>> getCourseYearsDivisionsByShiftId(int shiftId) {
        return mRepository.getCourseYearsDivisionsByShiftId(shiftId);
    }

    public Observable<JSONObject> rxDownload() {
        return mRepository.rxDownload();
    }

    public void save(JSONObject courseYears) {
        mRepository.save(courseYears);
    }
}
