package com.smartapps.sigev2.models.relationship.pojo;

public class RelationshipData {
    private int Id;
    private String Description;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    @Override
    public String toString() {
        return this.Description;
    }
}
