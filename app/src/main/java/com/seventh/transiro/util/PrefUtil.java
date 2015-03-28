package com.seventh.transiro.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

public class PrefUtil {
    private static final String HALTENAME = "halte_name";
    private static final String HALTEID = "halte_id";
    private static final String LAT = "latitude";
    private static final String LNG = "longitude";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(
                "transiro_app_preference", Context.MODE_PRIVATE);
    }

    public static void clearPreferences(Context context){
        getSharedPreferences(context).edit().clear().commit();
    }

    public static String getLocationUSer(Context context) {
        return getSharedPreferences(context).getString(HALTENAME, null);
    }

    public static void saveHalteName(Context context, String name) {
        getSharedPreferences(context).edit().putString(HALTENAME, name).commit();
    }

    public static void saveHalteId(Context context, String id){
        getSharedPreferences(context).edit().putString(HALTEID, id).commit();
    }

    public static String getHalteId(Context context){
        return getSharedPreferences(context).getString(HALTEID, null);
    }

    public static void saveLocation(Context context, Location location) {
        getSharedPreferences(context).edit().putFloat(LAT, (float) location.getLatitude()).commit();
        getSharedPreferences(context).edit().putFloat(LNG, (float) location.getLongitude()).commit();
    }

    public static double getLatitude(Context context){
        return Double.valueOf(getSharedPreferences(context).getFloat(LAT, 0));
    }

    public static double getLongitude(Context context){
        return Double.valueOf(getSharedPreferences(context).getFloat(LNG, 0));
    }
}
