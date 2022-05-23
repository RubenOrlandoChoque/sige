package com.smartapps.sigev2.models.studentdocumentations;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Maybe;

public class StudentDocumentationViewModel extends AndroidViewModel {
    private StudentDocumentationRepository studentDocumentationRepository;
    private StudentDocumentationRepositoryTemp studentDocumentationRepositoryTemp;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.

    public StudentDocumentationViewModel(Application application) {
        super(application);
        studentDocumentationRepository = new StudentDocumentationRepository(application);
        studentDocumentationRepositoryTemp = new StudentDocumentationRepositoryTemp(application);
    }

    public void insert(StudentDocumentation studentDocumentation) {
        new Thread(() -> {
            studentDocumentationRepository.insert(studentDocumentation);
        }).start();
    }


    public LiveData<List<StudentDocumentation>> getAll() {
        return studentDocumentationRepository.getAll();
    }

    public Maybe<List<Integer>> getDocumentations(String id) {
        return studentDocumentationRepository.getDocumentations(id);
    }

    public Maybe<List<Integer>> getDocumentationsTemp(String id) {
        return studentDocumentationRepositoryTemp.getDocumentations(id);
    }
}
