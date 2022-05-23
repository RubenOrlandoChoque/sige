package com.smartapps.sigev2.models.tutorStudents;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.smartapps.sigev2.models.students.Student;
import com.smartapps.sigev2.models.tutor.Tutor;

@Entity(tableName = "TutorStudents",
        foreignKeys = {
                @ForeignKey(entity = Student.class,
                        parentColumns = "Id",
                        childColumns = "StudentId"
                ),
                @ForeignKey(entity = Tutor.class,
                        parentColumns = "Id",
                        childColumns = "TutorId"
                )
        },
        primaryKeys = {"TutorId", "StudentId"},
        indices = {@Index("StudentId"), @Index("RelationshipId")})
public class TutorStudents {
    @NonNull
    private String TutorId;
    @NonNull
    private String StudentId;
    private int RelationshipId;
    private boolean CurrentTutor;

    public int getRelationshipId() {
        return RelationshipId;
    }

    public void setRelationshipId(int relationshipId) {
        RelationshipId = relationshipId;
    }

    @NonNull
    public String getTutorId() {
        return TutorId;
    }

    public void setTutorId(@NonNull String tutorId) {
        TutorId = tutorId;
    }

    @NonNull
    public String getStudentId() {
        return StudentId;
    }

    public void setStudentId(@NonNull String studentId) {
        StudentId = studentId;
    }

    public boolean isCurrentTutor() {
        return CurrentTutor;
    }

    public void setCurrentTutor(boolean currentTutor) {
        CurrentTutor = currentTutor;
    }
}
