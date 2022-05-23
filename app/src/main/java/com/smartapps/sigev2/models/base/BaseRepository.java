package com.smartapps.sigev2.models.base;

import android.app.Application;

import com.smartapps.sigev2.database.RoomDatabase;
import com.smartapps.sigev2.models.courseyear.CourseYearDao;
import com.smartapps.sigev2.models.division.DivisionDao;
import com.smartapps.sigev2.models.persongenders.PersonGendersDao;
import com.smartapps.sigev2.models.relationship.RelationshipDao;

import io.reactivex.Completable;

public class BaseRepository {
    private final CourseYearDao courseYearDao;
    private final DivisionDao divisionDao;
    private final PersonGendersDao personGendersDao;
    private final RelationshipDao relationshipDao;

    BaseRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        courseYearDao = db.courseYearDao();
        divisionDao = db.divisionDao();
        personGendersDao = db.personGendersDao();
        relationshipDao = db.relationshipDao();
    }

    public Completable seed() {
        return Completable.fromAction(() -> {
//            Seed seed = new Seed();
//            seed.seedCourseYear(courseYearDao);
//            seed.seedDivision(divisionDao);
//            seed.seedPesonGenders(personGendersDao);
//            seed.seedRelationship(relationshipDao);
        });
    }
}
