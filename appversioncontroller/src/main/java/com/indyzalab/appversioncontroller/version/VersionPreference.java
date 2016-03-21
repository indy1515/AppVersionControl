package com.indyzalab.appversioncontroller.version;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.indyzalab.appversioncontroller.version.object.AppVersion;


/**
 * Created by IndyZa on 3/20/16 AD.
 * Copyright by IndyZaLab
 */
public class VersionPreference {
    public static final String PREF_NAME = "AppVersionControllerPreference";
    public static final String LATEST_APP_VERSION = "lastest_app_version";
    public static final String OPEN_APP_VERSION_COUNTER = "open_counter";
    public static final String UPDATER_SHOW = "updaterShow";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public Context mContext;

    public VersionPreference(Context mContext) {
        this.mContext = mContext;
        this.sharedPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    public boolean getAppUpdaterShow() {
        return sharedPreferences.getBoolean(UPDATER_SHOW, true);
    }

    public void setAppUpdaterShow(Boolean res) {
        editor.putBoolean(UPDATER_SHOW, res);
        editor.commit();
    }

    public void setAppVersion(AppVersion appVersion){
        // Check if the current version is lower version than the pending save
        // If not halt save

        Gson gson = new Gson();
        String json = gson.toJson(appVersion);
        editor.putString(VersionPreference.LATEST_APP_VERSION, json);
        editor.apply();
    }

    public AppVersion getAppVersion(){
        Gson gson = new Gson();
        String json = sharedPreferences.getString(VersionPreference.LATEST_APP_VERSION, "");
        AppVersion obj = gson.fromJson(json, AppVersion.class);
        return obj;
    }

    public int getOpenAppVersionCounter(){
        return sharedPreferences.getInt(OPEN_APP_VERSION_COUNTER, 0);
    }

    public void setOpenAppVersionCounter(int appVersionCounter){
        editor.putInt(OPEN_APP_VERSION_COUNTER, appVersionCounter);
        editor.commit();
    }



}
