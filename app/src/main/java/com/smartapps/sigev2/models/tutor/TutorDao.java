package com.smartapps.sigev2.models.tutor;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.smartapps.sigev2.models.base.BaseDao;
import com.smartapps.sigev2.models.tutor.pojo.TutorData;
import com.smartapps.sigev2.models.tutor.pojo.TutorDataToSync;
import com.smartapps.sigev2.models.tutorStudents.pojo.TutorStudentRelationshipData;

import java.util.List;

import io.reactivex.Maybe;

@Dao
public interface TutorDao extends BaseDao<Tutor> {
    @Query("SELECT * from Tutors where Id = :id")
    Tutor getTutorById(String id);

    @Query("SELECT p.*,ts.RelationshipId, t.Ocupation from Tutors as t " +
            "inner join people as p on p.Id = t.Id " +
            "inner join tutorstudents as ts on ts.TutorId = t.Id and ts.StudentId=:studentId and ts.currenttutor  = 1 " +
            "where t.Id = :tutorId")
    Maybe<TutorData> getTutorData(String tutorId, String studentId);

    @Query("SELECT p.*,ts.RelationshipId, t.Ocupation from Tutors as t " +
            "inner join people as p on p.Id = t.Id " +
            "inner join tutorstudents as ts on ts.TutorId = t.Id and ts.StudentId=:studentId and ts.currenttutor  = 1 " +
            "where t.Id = :tutorId")
    TutorDataToSync getTutorDataToSync(String tutorId, String studentId);

    @Query("SELECT * from Tutors where Id = :id")
    Maybe<Tutor> getTutor(String id);

    @Query("SELECT p.*,ts.RelationshipId, t.Ocupation from Tutors as t " +
            "inner join people as p on p.Id = t.Id " +
            "inner join tutorstudents as ts on ts.TutorId = t.Id and ts.currenttutor  = 1 " +
            "where ts.StudentId = :studentId")
    LiveData<List<TutorData>> getStudentTutors(String studentId);

    @Query("SELECT p.*,ts.RelationshipId, t.Ocupation from Tutors as t " +
            "inner join people as p on p.Id = t.Id " +
            "inner join tutorstudents as ts on ts.TutorId = t.Id and ts.currenttutor  = 1 " +
            "where ts.StudentId = :studentId")
    List<TutorData> getStudentTutorsList(String studentId);

    @Query("delete from Tutors")
    void deleteAll();

    @Query("SELECT * from Tutors")
    List<Tutor> getAll();

    @Query("SELECT ts.RelationshipId from TutorStudents as ts " +
            "where ts.TutorId = :tutorId and ts.StudentId=:studentId and ts.currenttutor  = 1 limit 1")
    Maybe<TutorStudentRelationshipData> getTutorStudentRelationship(String tutorId, String studentId);
}
