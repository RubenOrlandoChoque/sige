package com.smartapps.sigev2.models.teacher;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.smartapps.sigev2.classes.SharedConfig;
import com.smartapps.sigev2.models.employeejobtitles.EmployeeJobTitle;
import com.smartapps.sigev2.models.employeejobtitles.EmployeeJobTitleRepository;
import com.smartapps.sigev2.models.people.Person;
import com.smartapps.sigev2.models.people.PersonRepository;
import com.smartapps.sigev2.models.students.Student;
import com.smartapps.sigev2.models.teacher.pojo.TeacherData;
import com.smartapps.sigev2.models.teacher.pojo.TeacherListData;
import com.smartapps.sigev2.models.teacher.pojo.TeacherSync;

import java.util.List;
import java.util.UUID;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

public class TeacherViewModel extends AndroidViewModel {

    private TeacherRepository teacherRepository;
    private PersonRepository personRepository;
    private EmployeeJobTitleRepository employeeJobTitleRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private LiveData<List<Student>> all;
    private Single<Object> dataToSync;

    public TeacherViewModel(Application application) {
        super(application);
        teacherRepository = new TeacherRepository(application);
        personRepository = new PersonRepository(application);
        employeeJobTitleRepository = new EmployeeJobTitleRepository(application);
    }

    public void insert(Teacher teacher) {
        new Thread(() -> {
            teacherRepository.insert(teacher);
        }).start();
    }

    public LiveData<List<TeacherListData>> getTeacherList(int shiftId) {
        return teacherRepository.getTeacherList(shiftId);
    }

    public Maybe<TeacherData> getTeacher(String id, int shiftId) {
        return teacherRepository.getTeacher(id, shiftId);
    }

    public Completable saveTeacher(TeacherData teacherData) {

        return Completable.fromAction(() -> {
            // if there's no use, create a new user.
            // if we already have a user, then, since the user object is immutable,
            // create a new user, with the id of the previous user and the updated user name.

            // Save Student
            Person person = personRepository.getByDni(teacherData.getDocumentNumber());
            if (person == null) {
                person = new Person();
                String id = UUID.randomUUID().toString();
                person.setId(id);
            }
            person.setFirstName(teacherData.getFirstName());
            person.setLastName(teacherData.getLastName());
            person.setDocumentNumber(teacherData.getDocumentNumber());
            person.setPersonGenderId(teacherData.getPersonGenderId());
            person.setPhoneNumber(teacherData.getPhoneNumber());
            person.setMobilePhoneNumber(teacherData.getMobilePhoneNumber());
            person.setAddress(teacherData.getAddress());
            person.setBirthdate(teacherData.getBirthdate());
            if (!teacherData.getScannerResult().trim().isEmpty()) {
                person.setScannerResult(teacherData.getScannerResult());
            }
            personRepository.insert(person);

            Teacher teacher = teacherRepository.getTeacherById(person.getId());
            if (teacher == null) {
                teacher = new Teacher();
                teacher.setId(person.getId());
            }
            teacher.setCUIL(teacherData.getCUIL());
            teacher.setHireDate(teacherData.getHireDate());
            teacher.setSync(false);
            teacherRepository.insert(teacher);

            EmployeeJobTitle employeeJobTitle = employeeJobTitleRepository.get(person.getId(), SharedConfig.getShiftId());
            if (employeeJobTitle == null) {
                employeeJobTitle = new EmployeeJobTitle();
                employeeJobTitle.setShiftId(SharedConfig.getShiftId());
                employeeJobTitle.setEmployeeId(person.getId());
            }
            employeeJobTitle.setTypeEmployeeId(teacherData.getTypeEmployeeId());
            employeeJobTitle.setJobTitleId(4); // 4 is Id of JobTitle for Teacher

            employeeJobTitleRepository.insert(employeeJobTitle);


        });
    }

    public Single<String> syncStudentTutor(String teacherId) {
        return Single.create(emitter -> {
            // Save Student
            try {
                Teacher teacher = teacherRepository.getTeacherById(teacherId);
                if (teacher != null) {
                    teacher.setSync(true);
                    teacherRepository.insert(teacher);
                }
                emitter.onSuccess("success");
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    public Single<List<TeacherSync>> getDataToSync() {
        return Single.create(new SingleOnSubscribe<List<TeacherSync>>() {
            @Override
            public void subscribe(SingleEmitter<List<TeacherSync>> emitter) throws Exception {
                List<TeacherSync> data = teacherRepository.getDataToSync();
                emitter.onSuccess(data);
            }
        });
    }
}
