package com.smartapps.sigev2.database;

import androidx.room.TypeConverter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(String value) {
        Date d = null;
        try {
            d = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(value);
//            int dd = Integer.parseInt(DateFormat.format("yyyy", d).toString());
//            if(dd > 2015 || dd < 2015){
//                Log.e(">>>>>>>", ">>>>>>>>>>>>");
//            }
        } catch (Exception e) {

        }
        return d;
    }

    @TypeConverter
    public static String dateToTimestamp(Date date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(date);
    }
}
