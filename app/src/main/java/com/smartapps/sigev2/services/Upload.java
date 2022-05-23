package com.smartapps.sigev2.services;

import com.smartapps.sigev2.classes.SyncResult;
import com.smartapps.sigev2.models.students.StudentViewModel;
import com.smartapps.sigev2.models.teacher.TeacherViewModel;

public class Upload {
    private StudentViewModel studentViewModel;
    private TeacherViewModel teacherViewModel;
    private UploadListner uploadListner;
    private SyncResult successImages;
    private SyncResult successStudent;
    private boolean successTeacher = true;

    public void Sync(StudentViewModel studentViewModel, TeacherViewModel teacherViewModel, UploadListner uploadListner) {
        this.studentViewModel = studentViewModel;
        this.teacherViewModel = teacherViewModel;
        this.uploadListner = uploadListner;
        syncImages();
    }

    private void syncImages() {
        new UploadImageService((success) -> {
            successImages = success;
            syncStudents();
        }).uploadImageStudents(studentViewModel);
    }

    private void syncStudents() {
        new UploadStudentService((success) -> {
            successStudent = success;
            syncTeachers();
        }).uploadUpdateStudents(studentViewModel);
    }

    private void syncTeachers() {
        new UploadTeacherService((success) -> {
            successTeacher = success;
            uploadListner.finish(successImages, successStudent, successTeacher);
        }).uploadUpdateTeachers(teacherViewModel);
    }

    public interface UploadListner {
        void finish(SyncResult successImages, SyncResult successStudent, boolean successTeacher);
    }
}
