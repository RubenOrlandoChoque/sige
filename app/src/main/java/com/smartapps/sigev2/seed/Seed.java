package com.smartapps.sigev2.seed;

import com.smartapps.sigev2.models.courseyear.CourseYear;
import com.smartapps.sigev2.models.courseyear.CourseYearDao;
import com.smartapps.sigev2.models.division.Division;
import com.smartapps.sigev2.models.division.DivisionDao;
import com.smartapps.sigev2.models.persongenders.PersonGenders;
import com.smartapps.sigev2.models.persongenders.PersonGendersDao;
import com.smartapps.sigev2.models.relationship.Relationship;
import com.smartapps.sigev2.models.relationship.RelationshipDao;

public class Seed {
    public void seedCourseYear(CourseYearDao courseYearDao) {
        CourseYear courseYear = new CourseYear();
        courseYear.setId(1);
        courseYear.setCourseYearName("1°");
        courseYearDao.insert(courseYear);
    
        courseYear = new CourseYear();
        courseYear.setId(2);
        courseYear.setCourseYearName("2°");
        courseYearDao.insert(courseYear);

        courseYear = new CourseYear();
        courseYear.setId(3);
        courseYear.setCourseYearName("3°");
        courseYearDao.insert(courseYear);

        courseYear = new CourseYear();
        courseYear.setId(4);
        courseYear.setCourseYearName("4°");
        courseYearDao.insert(courseYear);

        courseYear = new CourseYear();
        courseYear.setId(5);
        courseYear.setCourseYearName("5°");
        courseYearDao.insert(courseYear);
    }

    public void seedDivision(DivisionDao divisionDao) {
        Division division = new Division();
        division.setId(1);
        division.setCourseYearId(1);
        division.setDivisionName("1° 1°");
        division.setSchoolYearId(1);
        divisionDao.insert(division);

        division = new Division();
        division.setId(2);
        division.setCourseYearId(1);
        division.setDivisionName("1° 2°");
        division.setSchoolYearId(1);
        divisionDao.insert(division);

        division = new Division();
        division.setId(3);
        division.setCourseYearId(1);
        division.setDivisionName("1° 3°");
        division.setSchoolYearId(1);
        divisionDao.insert(division);

        division = new Division();
        division.setId(4);
        division.setCourseYearId(1);
        division.setDivisionName("1° 4°");
        division.setSchoolYearId(1);
        divisionDao.insert(division);


        division = new Division();
        division.setId(5);
        division.setCourseYearId(2);
        division.setDivisionName("2° 1°");
        division.setSchoolYearId(1);
        divisionDao.insert(division);

        division = new Division();
        division.setId(6);
        division.setCourseYearId(2);
        division.setDivisionName("2° 2°");
        division.setSchoolYearId(1);
        divisionDao.insert(division);

        division = new Division();
        division.setId(7);
        division.setCourseYearId(2);
        division.setDivisionName("2° 3°");
        division.setSchoolYearId(1);
        divisionDao.insert(division);


        division = new Division();
        division.setId(8);
        division.setCourseYearId(3);
        division.setDivisionName("3° 1°");
        division.setSchoolYearId(1);
        divisionDao.insert(division);

        division = new Division();
        division.setId(9);
        division.setCourseYearId(3);
        division.setDivisionName("3° 2°");
        division.setSchoolYearId(1);
        divisionDao.insert(division);

        division = new Division();
        division.setId(10);
        division.setCourseYearId(3);
        division.setDivisionName("3° 3°");
        division.setSchoolYearId(1);
        divisionDao.insert(division);


        division = new Division();
        division.setId(11);
        division.setCourseYearId(4);
        division.setDivisionName("4° 1°");
        division.setSchoolYearId(1);
        divisionDao.insert(division);

        division = new Division();
        division.setId(12);
        division.setCourseYearId(4);
        division.setDivisionName("4° 2°");
        division.setSchoolYearId(1);
        divisionDao.insert(division);


        division = new Division();
        division.setId(13);
        division.setCourseYearId(5);
        division.setDivisionName("5° 1°");
        division.setSchoolYearId(1);
        divisionDao.insert(division);
    }

    public void seedPesonGenders(PersonGendersDao personGendersDao) {
        PersonGenders personGenders = new PersonGenders();
        personGenders.setId(1);
        personGenders.setDescription("Masculino");
        personGenders.setIsActive(true);
        personGendersDao.insert(personGenders);

        personGenders = new PersonGenders();
        personGenders.setId(2);
        personGenders.setDescription("Femenino");
        personGenders.setIsActive(true);
        personGendersDao.insert(personGenders);
    }

    public void seedRelationship(RelationshipDao relationshipDao) {
        Relationship relationship = new Relationship();
        relationship.setId(1);
        relationship.setDescription("Padre");
        relationship.setIsActive(true);
        relationshipDao.insert(relationship);

        relationship = new Relationship();
        relationship.setId(2);
        relationship.setDescription("Madre");
        relationship.setIsActive(true);
        relationshipDao.insert(relationship);

        relationship = new Relationship();
        relationship.setId(3);
        relationship.setDescription("Tío/a");
        relationship.setIsActive(true);
        relationshipDao.insert(relationship);

        relationship = new Relationship();
        relationship.setId(4);
        relationship.setDescription("Abuelo/a");
        relationship.setIsActive(true);
        relationshipDao.insert(relationship);

        relationship = new Relationship();
        relationship.setId(5);
        relationship.setDescription("Hermano/a");
        relationship.setIsActive(true);
        relationshipDao.insert(relationship);


        relationship = new Relationship();
        relationship.setId(6);
        relationship.setDescription("Esposo/a");
        relationship.setIsActive(true);
        relationshipDao.insert(relationship);

        relationship = new Relationship();
        relationship.setId(7);
        relationship.setDescription("Concubino/a");
        relationship.setIsActive(true);
        relationshipDao.insert(relationship);

        relationship = new Relationship();
        relationship.setId(8);
        relationship.setDescription("Sobrino/a");
        relationship.setIsActive(true);
        relationshipDao.insert(relationship);

        relationship = new Relationship();
        relationship.setId(9);
        relationship.setDescription("Cuñado/a");
        relationship.setIsActive(true);
        relationshipDao.insert(relationship);

        relationship = new Relationship();
        relationship.setId(10);
        relationship.setDescription("Primo/a");
        relationship.setIsActive(true);
        relationshipDao.insert(relationship);

        relationship = new Relationship();
        relationship.setId(11);
        relationship.setDescription("Otro familiar");
        relationship.setIsActive(true);
        relationshipDao.insert(relationship);

        relationship = new Relationship();
        relationship.setId(12);
        relationship.setDescription("No familiar");
        relationship.setIsActive(true);
        relationshipDao.insert(relationship);
    }
}