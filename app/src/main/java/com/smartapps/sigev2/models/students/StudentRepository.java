package com.smartapps.sigev2.models.students;

import android.app.Application;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.smartapps.sigev2.classes.SharedConfig;
import com.smartapps.sigev2.database.RoomDatabase;
import com.smartapps.sigev2.models.people.Person;
import com.smartapps.sigev2.models.people.PersonDao;
import com.smartapps.sigev2.models.students.pojo.ImageToSync;
import com.smartapps.sigev2.models.students.pojo.StudentData;
import com.smartapps.sigev2.models.students.pojo.StudentDataSync;
import com.smartapps.sigev2.models.students.pojo.StudentListData;
import com.smartapps.sigev2.models.tutor.Tutor;
import com.smartapps.sigev2.models.tutor.TutorDao;
import com.smartapps.sigev2.models.tutorStudents.TutorStudents;
import com.smartapps.sigev2.models.tutorStudents.TutorStudentsDao;
import com.smartapps.sigev2.services.DownloadService;
import com.smartapps.sigev2.util.DateDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;

public class StudentRepository {
    private StudentDao studentDao;
    private PersonDao personDao;
    private TutorDao tutorDao;
    private TutorStudentsDao tutorStudentsDao;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    StudentRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        studentDao = db.studentDao();
        personDao = db.personDao();
        tutorDao = db.tutorDao();
        tutorStudentsDao = db.tutorStudentsDao();

    }

    public void insert(Student student) {
        studentDao.insert(student);
    }

    public Maybe<StudentData> getStudent(String id) {
        return studentDao.getStudent(id);
    }

    public Student getStudentById(String id) {
        return studentDao.getStudentById(id);
    }

    public LiveData<List<StudentListData>> getStudentList(int courseYearId, int shiftId) {
        return studentDao.getStudentList(courseYearId, shiftId, SharedConfig.getCurrentSchoolYearId());
    }

    public void update(Student student) {
        studentDao.update(student);
    }

    List<StudentDataSync> getStudentToSync() {
        return studentDao.getStudentToSync();
    }

    List<ImageToSync> getImgProfileImageToSync() {
        return studentDao.getImgProfileImageToSync();
    }

    List<ImageToSync> getCardFrontImageToSync() {
        return studentDao.getCardFrontImageToSync();
    }

    List<ImageToSync> getCardBackImageToSync() {
        return studentDao.getCardBackImageToSync();
    }

    public Observable<JSONObject> rxDownload() {
        return new DownloadService().rxDownload("api/students/allmobile");
    }

    public void save(JSONObject data) {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
            Gson gson = gsonBuilder.create();
            JSONArray response = data.getJSONArray("Result");
            if (response.length() > 0) {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject item = response.getJSONObject(i);
                    // insertamos al alumno
                    Student student = gson.fromJson(item.getJSONObject("Student").toString(), Student.class);
                    Person person = gson.fromJson(item.getJSONObject("Student").toString(), Person.class);
                    personDao.insert(person);
                    studentDao.insert(student);

                    //insertamos al tutor
                    JSONArray tutors = item.getJSONArray("Tutors");
                    for (int t = 0; t < tutors.length(); t++) {
                        Person tutorPerson = gson.fromJson(tutors.get(t).toString(), Person.class);
                        Tutor tutor = gson.fromJson(tutors.get(t).toString(), Tutor.class);
                        TutorStudents tutorStudents = gson.fromJson(tutors.get(t).toString(), TutorStudents.class);
                        personDao.insert(tutorPerson);
                        tutorDao.insert(tutor);
                        tutorStudentsDao.insert(tutorStudents);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("-", e.getMessage());
        }
    }
}
