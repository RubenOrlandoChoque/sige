package com.smartapps.sigev2.util;

/**
 * Created by USUARIO 004 on 10/5/2018.
 */

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateDeserializer implements JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
        String date = element.getAsString();
        if (date == null || date.isEmpty()) {
            return null;
        }
        date = date.split("\\.")[0];
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date result = null;
        try {
            result = format.parse(date);
        } catch (ParseException exp) {
            Log.e("DateDeserializer", exp.getMessage());
        } catch (Exception exp) {
            Log.e("DateDeserializer", exp.getMessage());
        }
        return result;
    }
}