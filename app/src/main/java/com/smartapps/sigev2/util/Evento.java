package com.smartapps.sigev2.util;

/**
 * Created by mparraga on 8/5/2018.
 */

public class Evento {
    public String eventName;
    public Object data;

    public Evento(String eventName, Object data) {
        this.eventName = eventName;
        this.data = data;
    }

    public Evento(String eventName) {
        this.eventName = eventName;
    }
}
