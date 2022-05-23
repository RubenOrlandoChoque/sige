package com.smartapps.sigev2.models.students;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.smartapps.sigev2.models.base.BaseDao;
import com.smartapps.sigev2.models.students.pojo.ImageToSync;
import com.smartapps.sigev2.models.students.pojo.StudentCountData;
import com.smartapps.sigev2.models.students.pojo.StudentData;
import com.smartapps.sigev2.models.students.pojo.StudentDataSync;
import com.smartapps.sigev2.models.students.pojo.StudentListData;

import java.util.List;

import io.reactivex.Maybe;

@Dao
public interface StudentDao extends BaseDao<Student> {
    @Query("SELECT * from Students " +
            "inner join People on Students.Id = People.Id " +
            "left join StudentDivisions on Students.Id = StudentDivisions.StudentId and StudentDivisions.currentDivision = 1 " +
            "where Students.Id = :id")
    Maybe<StudentData> getStudent(String id);

    @Query("SELECT distinct p.FirstName, p.LastName,p.DocumentNumber,p.Birthdate,p.Id from StudentDivisions as sd " +
            "inner join divisions as d on sd.DivisionId = d.Id and sd.currentDivision = 1 " +
            "inner join students as s on s.Id = sd.StudentId " +
            "inner join people as p on p.Id = s.Id " +
            "where d.CourseYearId = :courseYearId and d.ShiftId = :shiftId and d.SchoolYearId = :schoolYearId order by p.LastName, p.FirstName")
    LiveData<List<StudentListData>> getStudentList(int courseYearId, int shiftId, int schoolYearId);

    @Query("SELECT * from Students where Id = :id")
    Student getStudentById(String id);

    @Query("select p.*,s.HasBrothersOfSchoolAge,sd.DivisionId, ts.tutorid as TutorId, s.IdCardBack, s.IdCardFront, s.IdImgProfile,s.Observation, s.SchoolOrigin, s.OtherPhoneBelongs, s.PhoneBelongs1, s.PhoneBelongs2, s.HighestEducLevelFatherId, s.HighestEducLevelMotherId from students as s " +
            "inner join studentDivisions as sd on sd.studentid = s.id and currentdivision = 1 " +
            "inner join tutorStudents as ts on ts.studentid = s.id and ts.currenttutor  = 1 " +
            "inner join people as p on p.id = s.id where s.Sync = 0")
    List<StudentDataSync> getStudentToSync();


    @Query("select IdCardFront as Path, Id, 1 as Type from Students where SyncCardFront = 0 and trim(ifnull(IdCardFront, '')) != ''")
    List<ImageToSync> getCardFrontImageToSync();

    @Query("select IdCardBack as Path, Id, 2 as Type from Students where SyncCardBack = 0 and trim(ifnull(IdCardBack, '')) != ''")
    List<ImageToSync> getCardBackImageToSync();

    @Query("select IdImgProfile as Path, Id, 3 as Type from Students where SyncImgProfile = 0 and trim(ifnull(IdImgProfile, '')) != ''")
    List<ImageToSync> getImgProfileImageToSync();

    @Query("delete from Students")
    void deleteAll();
}
