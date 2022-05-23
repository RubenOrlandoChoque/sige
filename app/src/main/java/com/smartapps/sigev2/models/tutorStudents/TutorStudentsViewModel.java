package com.smartapps.sigev2.models.tutorStudents;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TutorStudentsViewModel extends AndroidViewModel {
    private TutorStudentsRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.

    public TutorStudentsViewModel(Application application) {
        super(application);
        mRepository = new TutorStudentsRepository(application);
    }

    public void insert(TutorStudents object) {
        new Thread(() -> {
            mRepository.insert(object);
        }).start();
    }


    public LiveData<List<TutorStudents>> getAll() {
        return mRepository.getAll();
    }

    public void saveStudentTutor(String studentId, String tutorId) {
        TutorStudents tutorStudents = new TutorStudents();
        tutorStudents.setStudentId(studentId);
        tutorStudents.setTutorId(tutorId);
        tutorStudents.setCurrentTutor(true);
        try {
            mRepository.disableAll(studentId); // deshabilitar anteriores
            mRepository.insert(tutorStudents); // insertar nuevo.
        } catch (Exception e) {

        }
    }
}
