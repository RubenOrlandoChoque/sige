package com.smartapps.sigev2.models.students.pojo;

public class ImageToSync {
    public static final int CARD_FRONT = 1;
    public static final int CARD_BACK = 2;
    public static final int IMG_PROFILE = 3;

    private int Type;
    private String Path;
    private String Id;

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        this.Path = path;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
