package com.smartapps.sigev2.models.students.pojo;

import androidx.room.Ignore;

import com.smartapps.sigev2.models.people.Person;

import java.util.ArrayList;
import java.util.List;

public class StudentData extends Person {
    public boolean HasBrothersOfSchoolAge;
    public int DivisionId;
    public String IdImgProfile;
    public String IdCardFront;
    public String IdCardBack;
    public String Observation;
    public String SchoolOrigin;
    public String OtherPhoneBelongs;
    public String PhoneBelongs1;
    public String PhoneBelongs2;
    public int HighestEducLevelFatherId;
    public int HighestEducLevelMotherId;
    public Float Amount = Float.parseFloat("15");
    public int ConditionId;

    @Ignore
    public List<Integer> documentations = new ArrayList<>();

    // campo solo para verificar que se respondio la pregunta has brother, ya que es obligatoria
    @Ignore
    public boolean HasBrothersOfSchoolAgeAnswered;
    @Ignore
    public boolean BelongsEthnicGroupAnswered;
    @Ignore
    public boolean HasHealthProblemsAnswered;
    @Ignore
    public boolean HasLegalProblemsAnswered;
}
