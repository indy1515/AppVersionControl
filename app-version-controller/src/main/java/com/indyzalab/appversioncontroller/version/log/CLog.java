package com.indyzalab.appversioncontroller.version.log;

import android.util.Log;

/**
 * Created by IndyZa on 3/21/16 AD.
 * Copyright by IndyZaLab
 */
public class CLog {

    public static boolean allowLog = false;
    public static void d(String description){
        if(allowLog){
            Log.d(getCallerClassName(), description);
        }
    }

    public static void setAllowLog(boolean allowLog) {
        CLog.allowLog = allowLog;
    }

    public static String getCallerClassName() {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        for (int i=1; i<stElements.length; i++) {
            StackTraceElement ste = stElements[i];
            if (!ste.getClassName().equals(CLog.class.getName()) && ste.getClassName().indexOf("java.lang.Thread")!=0) {
                return ste.getClassName();
            }
        }
        return null;
    }
}
