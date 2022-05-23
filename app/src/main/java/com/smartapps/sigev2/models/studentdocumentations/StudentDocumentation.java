package com.smartapps.sigev2.models.studentdocumentations;


import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(tableName = "StudentDocumentations", primaryKeys =
        {"StudentId", "DocumentationId"})
public class StudentDocumentation {
    @NonNull
    private String StudentId;
    @NonNull
    private int DocumentationId;
    private int SchoolYearId;

    @NonNull
    public int getDocumentationId() {
        return DocumentationId;
    }

    public void setDocumentationId(@NonNull int documentationId) {
        DocumentationId = documentationId;
    }

    public int getSchoolYearId() {
        return SchoolYearId;
    }

    public void setSchoolYearId(int schoolYearId) {
        SchoolYearId = schoolYearId;
    }

    @NonNull
    public String getStudentId() {
        return StudentId;
    }

    public void setStudentId(@NonNull String studentId) {
        StudentId = studentId;
    }
}
