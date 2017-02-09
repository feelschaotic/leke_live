package com.juss.mediaplay.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ramo.campuslive.application.MyApplication;

/**
 * Created by lenovo on 2016/7/26.
 */
public class PreferencesUtil {
    static Context context= MyApplication.getContext();
    public static void setString(final String key,final String val){
        final SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(context);
        setting.edit().putString(key,val).commit();
    }
    public static String getString(String key){
        final SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(context);
        return setting.getString(key,null);
    }
    public static void setInt(final String key,final int val){
        final SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(context);
        setting.edit().putInt(key,val).commit();
    }
    public static int getInt(final String key,final int defaultInt){
        final SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(context);
        return setting.getInt(key,defaultInt);
    }
    public static void setBoolean(final String key,final boolean val){
        final SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(context);
        setting.edit().putBoolean(key,val).commit();
    }
    public static boolean getBoolean(final String key,final boolean defaultval){
        final SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(context);
        return setting.getBoolean(key,defaultval);
    }
    public static void setLong(final String key,final Long val){
        final SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(context);
        setting.edit().putLong(key,val).commit();
    }
    public static long getBoolean(final String key,final long defaultval){
        final SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(context);
        return setting.getLong(key,defaultval);
    }public static void setFloat(final String key,final float val){
        final SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(context);
        setting.edit().putFloat(key,val).commit();
    }
    public static float getFloat(final String key,final float defaultval){
        final SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(context);
        return setting.getFloat(key,defaultval);
    }
    public static void clearPreferences(final SharedPreferences s){
        SharedPreferences.Editor editor = s.edit();
        editor.clear();
        editor.commit();
    }
}
