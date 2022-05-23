package com.smartapps.sigev2.models.tutor.pojo;

import com.smartapps.sigev2.models.people.Person;

public class TutorDataToSync extends Person {
    private int RelationshipId;
    private String Ocupation;

    public int getRelationshipId() {
        return RelationshipId;
    }

    public void setRelationshipId(int relationshipId) {
        RelationshipId = relationshipId;
    }

    public String getOcupation() {
        return Ocupation;
    }

    public void setOcupation(String ocupation) {
        Ocupation = ocupation;
    }
}
