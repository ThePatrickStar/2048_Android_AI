package com.lyk.ai_2048.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by lyk on 5/7/16.
 */
public abstract class PrefUtil {
    //keys
    public static final String HIGH_SCORE = "high_score";
    public static final String SCORE = "score";
    // board is stored as a string "number:number:number..."
    public static final String BOARD = "board";

    public static void clearPreference(Context context){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.apply();
    }

    public static void setBooleanPreference(String key,boolean info, Context context){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, info);
        editor.apply();
    }

    public static boolean getBooleanPreference(String key,Context context){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getBoolean(key,false);
    }

    public static void setStringPreference(String key,String info, Context context){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, info);
        editor.apply();
    }

    public static String getStringPreference(String key,Context context){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getString(key, null);
    }

    public static void setLongPreference(String key,long info, Context context){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, info);
        editor.apply();
    }

    public static long getLongPreference(String key,Context context){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getLong(key, -1);
    }

    public static void setIntPreference(String key,int info, Context context){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, info);
        editor.apply();
    }

    public static int getIntPreference(String key,Context context){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getInt(key, -1);
    }
}