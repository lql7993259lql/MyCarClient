package com.mycar.config;


import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

import com.mycar.Application;
import com.mycar.util.FileUtil;


public class AppConfig {

    private static AppConfig appConfig;

    private SharedPreferences preferences;

    /**
     * 是否是测试环境.
     */
    public static final boolean DEBUG = false;

    /**
     * App根目录.
     */
    public String APP_PATH_ROOT;

    private AppConfig() {
        preferences = Application.getInstance().getSharedPreferences("nohttp_sample", Context.MODE_PRIVATE);

        APP_PATH_ROOT = FileUtil.getRootPath().getAbsolutePath() + File.separator + "NoHttpSample";
        FileUtil.initDirectory(APP_PATH_ROOT);
    }

    public static AppConfig getInstance() {
        if (appConfig == null)
            appConfig = new AppConfig();
        return appConfig;
    }

    public void putInt(String key, int value) {
        preferences.edit().putInt(key, value).commit();
    }

    public int getInt(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    public void putString(String key, String value) {
        preferences.edit().putString(key, value).commit();
    }

    public String getString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public void putBoolean(String key, boolean value) {
        preferences.edit().putBoolean(key, value).commit();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    public void putLong(String key, long value) {
        preferences.edit().putLong(key, value).commit();
    }

    public long getLong(String key, long defValue) {
        return preferences.getLong(key, defValue);
    }

    public void putFloat(String key, float value) {
        preferences.edit().putFloat(key, value).commit();
    }

    public float getFloat(String key, float defValue) {
        return preferences.getFloat(key, defValue);
    }
}
