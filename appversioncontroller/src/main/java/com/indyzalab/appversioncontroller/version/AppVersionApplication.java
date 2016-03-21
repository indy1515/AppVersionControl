package com.indyzalab.appversioncontroller.version;

import android.app.Application;
import android.content.res.Configuration;

import com.indyzalab.appversioncontroller.version.log.CLog;

import java.util.Date;


/**
 * Created by IndyZa on 3/20/16 AD.
 * Copyright by IndyZaLab
 */
public class AppVersionApplication extends Application{
    private long lastConfigChange;

    /** @param buffer the number of milliseconds required after an orientation change to still be considered the same event*/
    public boolean wasLastConfigChangeRecent(int buffer)
    {
        return (new Date().getTime() - lastConfigChange <= buffer);
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        lastConfigChange = new Date().getTime();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        lastConfigChange = new Date().getTime();
        CLog.d("Last Config Change Trigger");
    }
}
