package com.smartapps.sigev2.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.smartapps.sigev2.App;

import java.util.HashSet;
import java.util.Set;

/*Esta clase mantiene las variables de configuracion es equivalente a un config.ini*/
public class SharedConfig {

    public static final String PREFS_FILENAME = "CONFIG_PREFS";
    private static final String TOKEN_CODE = "TOKEN_CODE";
    private static final String USER_NAME = "USER_NAME";
    private static final String USER_CODE = "USER_CODE";
    private static final String USER_SURNAME = "USER_SURNAME";
    private static final String USER_PHOTO = "USER_PHOTO";
    private static final String SHIFT_ID = "SHIFT_ID";
    private static final String SHIFT_DESCRIPTION = "SHIFT_DESCRIPTION";
    private static final String USER_SHIFT_IDS = "USER_SHIFT_IDS";
    private static final String SERVER_URL = "SERVER_URL";
    private static final String CURRENT_SCHOOL_YEAR_ID = "CURRENT_SCHOOL_YEAR_ID";
    private static final String CURRENT_SCHOOL_YEAR = "CURRENT_SCHOOL_YEAR";

    private static final String SCAN_LICENCE = "SCAN_LICENCE";
    private static final String KEY_VALID = "KEY_VALID";


    public static void setToken(String token) {
        SharedPreferences settings;
        Editor editor;
        settings = App.getAppContext().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putString(TOKEN_CODE, token); //3

        editor.commit(); //4
    }

    public static String getToken() {
        SharedPreferences settings;
        settings = App.getAppContext().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE);
        return settings.getString(TOKEN_CODE, "");
    }

    public static void setScanLicence(String token) {
        SharedPreferences settings;
        Editor editor;
        settings = App.getAppContext().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putString(SCAN_LICENCE, token); //3

        editor.commit(); //4
    }

    public static boolean isKeyValid() {
        SharedPreferences settings;
        settings = App.getAppContext().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE);
        return settings.getBoolean(KEY_VALID, false);
    }

    public static void setKeyValid(boolean token) {
        SharedPreferences settings;
        Editor editor;
        settings = App.getAppContext().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putBoolean(KEY_VALID, token); //3

        editor.commit(); //4
    }

    public static String getScanLicence() {
        SharedPreferences settings;
        settings = App.getAppContext().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE);
        return settings.getString(SCAN_LICENCE, "");
    }

    public static void setUserName(String token) {
        SharedPreferences settings;
        Editor editor;
        settings = App.getAppContext().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putString(USER_NAME, token); //3

        editor.commit(); //4
    }

    public static String getUserName() {
        SharedPreferences settings;
        settings = App.getAppContext().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE);
        return settings.getString(USER_NAME, "");
    }

    public static void setUserCode(String token) {
        SharedPreferences settings;
        Editor editor;
        settings = App.getAppContext().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putString(USER_CODE, token); //3

        editor.commit(); //4
    }

    public static String getUserCode() {
        SharedPreferences settings;
        settings = App.getAppContext().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE);
        return settings.getString(USER_CODE, "");
    }

    public static void setUserSurname(String token) {
        SharedPreferences settings;
        Editor editor;
        settings = App.getAppContext().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putString(USER_SURNAME, token); //3

        editor.commit(); //4
    }

    public static String getUserSurname() {
        SharedPreferences settings;
        settings = App.getAppContext().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE);
        return settings.getString(USER_SURNAME, "");
    }

    public static void setUserPhoto(String token) {
        SharedPreferences settings;
        Editor editor;
        settings = App.getAppContext().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putString(USER_PHOTO, token); //3

        editor.commit(); //4
    }

    public static String getUserPhoto() {
        SharedPreferences settings;
        settings = App.getAppContext().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE);
        return settings.getString(USER_PHOTO, "");
    }

    public static void setShiftId(int token) {
        SharedPreferences settings;
        Editor editor;
        settings = App.getAppContext().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putInt(SHIFT_ID, token); //3

        editor.commit(); //4
    }

    public static int getShiftId() {
        SharedPreferences settings;
        settings = App.getAppContext().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE);
        return settings.getInt(SHIFT_ID, 0);
    }

    public static void setShiftDescription(String token) {
        SharedPreferences settings;
        Editor editor;
        settings = App.getAppContext().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putString(SHIFT_DESCRIPTION, token); //3

        editor.commit(); //4
    }

    public static String getShiftDescription() {
        SharedPreferences settings;
        settings = App.getAppContext().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE);
        return settings.getString(SHIFT_DESCRIPTION, "");
    }

    public static void setUserShiftIds(Set<String> token) {
        SharedPreferences settings;
        Editor editor;
        settings = App.getAppContext().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putStringSet(USER_SHIFT_IDS, token); //3

        editor.commit(); //4
    }

    public static Set<String> getUserShiftIds() {
        SharedPreferences settings;
        settings = App.getAppContext().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE);
        return settings.getStringSet(USER_SHIFT_IDS, new HashSet<>());
    }

    public static void setServerUrl(String token) {
        SharedPreferences settings;
        Editor editor;
        settings = App.getAppContext().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putString(SERVER_URL, token); //3

        editor.commit(); //4
    }

    public static String getServerUrl() {
        SharedPreferences settings;
        settings = App.getAppContext().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE);
        return settings.getString(SERVER_URL, "");
    }

    public static void setCurrentSchoolYearId(int schoolYearId) {
        SharedPreferences settings;
        Editor editor;
        settings = App.getAppContext().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putInt(CURRENT_SCHOOL_YEAR_ID, schoolYearId); //3

        editor.commit(); //4
    }

    public static int getCurrentSchoolYearId() {
        SharedPreferences settings;
        settings = App.getAppContext().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE);
        return settings.getInt(CURRENT_SCHOOL_YEAR_ID, 0);
    }

    public static void setCurrentSchoolYear(String schoolYear) {
        SharedPreferences settings;
        Editor editor;
        settings = App.getAppContext().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putString(CURRENT_SCHOOL_YEAR, schoolYear); //3

        editor.commit(); //4
    }

    public static String getCurrentSchoolYear() {
        SharedPreferences settings;
        settings = App.getAppContext().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE);
        return settings.getString(CURRENT_SCHOOL_YEAR, "");
    }
}

