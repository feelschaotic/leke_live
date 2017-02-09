package com.juss.live.skin.simple.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferenceUtil {
	private SharedPreferences preferences;
	private Context context;

	private SharedPreferenceUtil(Context context,String name) {
		this.context = context;
		preferences = context.getSharedPreferences(name,
				Context.MODE_MULTI_PROCESS);
	}
	
	public static SharedPreferenceUtil getInstance(Context context) {
		return new SharedPreferenceUtil(context,"setting");
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public boolean contains(String key) {
		if (preferences == null) {
			return false;
		}
		return preferences.contains(key);
	}

	public void putInt(String key, Integer value) {
		Editor edit = preferences.edit();
		edit.putInt(key, value);
		edit.commit();
	}

	public void putBoolean(String key, Boolean value) {
		Editor edit = preferences.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}

	public void putFloat(String key, Float value) {
		Editor edit = preferences.edit();
		edit.putFloat(key, value);
		edit.commit();
	}

	public void putLong(String key, Long value) {
		Editor edit = preferences.edit();
		edit.putLong(key, value);
		edit.commit();
	}

	public void putString(String key, String value) {
		Editor edit = preferences.edit();
		edit.putString(key, value);
		edit.commit();
	}

	public int getInt(String key, int defValue) {
		return preferences.getInt(key, defValue);
	}

	public boolean getBoolean(String key, boolean defValue) {
		return preferences.getBoolean(key, defValue);
	}

	public float getFloat(String key, float defValue) {
		return preferences.getFloat(key, defValue);
	}

	public long getLong(String key, long defValue) {
		return preferences.getLong(key, defValue);
	}

	public String getString(String key, String defValue) {
		String value = preferences.getString(key, defValue);
		return value == null ? "" : value.trim();
	}
	
	public void clear(){
		preferences.edit().clear().commit();
	}
}
