package com.smartapps.sigev2.models.teacher;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.smartapps.sigev2.database.RoomDatabase;
import com.smartapps.sigev2.models.teacher.pojo.TeacherData;
import com.smartapps.sigev2.models.teacher.pojo.TeacherListData;
import com.smartapps.sigev2.models.teacher.pojo.TeacherSync;

import java.util.List;

import io.reactivex.Maybe;

public class TeacherRepository {
    private TeacherDao teacherDao;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    TeacherRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        teacherDao = db.teacherDao();

    }

    public void insert(Teacher teacher) {
        teacherDao.insert(teacher);
    }

    public LiveData<List<TeacherListData>> getTeacherList(int shiftId) {
        return teacherDao.getTeacherList(shiftId);
    }

    public Maybe<TeacherData> getTeacher(String id, int shiftId) {
        return teacherDao.getTeacher(id, shiftId);
    }

    public void update(Teacher teacher) {
        teacherDao.update(teacher);
    }

    public Teacher getTeacherById(String id) {
        return teacherDao.getTeacherById(id);
    }

    List<TeacherSync> getDataToSync() {
        return teacherDao.getDataToSync();
    }
}
