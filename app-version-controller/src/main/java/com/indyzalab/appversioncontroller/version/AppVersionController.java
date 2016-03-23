package com.indyzalab.appversioncontroller.version;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.indyzalab.appversioncontroller.version.comparator.AppVersionComparator;
import com.indyzalab.appversioncontroller.version.log.CLog;
import com.indyzalab.appversioncontroller.version.object.AppVersion;
import com.indyzalab.appversioncontroller.version.util.VersionUtils;
import com.indyzalab.appversioncontroller.version.view.UpdateDisplay;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by IndyZa on 3/19/16 AD.
 * Copyright by IndyZaLab
 */
public class AppVersionController {

    private AppVersionApplication appVersionApplication;
    private Context mContext;
    VersionPreference versionPreference;
    private String baseUrl;
    private String pathUrl;
    private String versionName;
    private String packageName;
    public AppVersionController(AppVersionApplication appVersionApplication
            , Context mContext, String baseUrl,String pathUrl,String versionName) {
        this.appVersionApplication = appVersionApplication;
        this.mContext = mContext;
        this.baseUrl = baseUrl;
        this.pathUrl = pathUrl;
        this.versionName = versionName;
        this.packageName = mContext.getPackageName();
        versionPreference = new VersionPreference(this.mContext);
    }

    public AppVersionController(AppVersionApplication appVersionApplication
            , Context mContext,  String baseUrl,String pathUrl
            , String versionName, String packageName) {
        this.appVersionApplication = appVersionApplication;
        this.mContext = mContext;
        this.pathUrl = pathUrl;
        this.baseUrl = baseUrl;
        this.versionName = versionName;
        this.packageName = packageName;
        versionPreference = new VersionPreference(this.mContext);
    }

    public interface AppVersionService {
        @POST
        Call<List<AppVersion>> loadAppVersionInfo(@Url String pathUrl,@Query("package") String mPackage
                , @Query("platform") String platform
                , @Query("current_version") String currentVersion);
    }

    Thread t;
    public void start(){
        //note that you can use getPreferences(MODE_PRIVATE), but this is easier to use from Fragments.
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!isNetworkAvailable()){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                checkVersion();


            }
        });
        t.start();

    }

    public void stop(){
        if(t!=null){
            t.interrupt();
        }
    }



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private synchronized void startCount(AppVersion appVersion){
        if(appVersion == null) return;
        CLog.d("Save App Version: " + versionPreference.getAppVersion());
        int appOpenedCount = versionPreference.getOpenAppVersionCounter();

        // For orientation
        if (!appVersionApplication.wasLastConfigChangeRecent(20000))//within 20 seconds - a huge buffer
        {
            CLog.d("Count Open Not Last Config Change " + versionPreference.getOpenAppVersionCounter());
            appOpenedCount += 1;
            versionPreference.setOpenAppVersionCounter(appOpenedCount);
        }


        if(appVersion.isForcedUpdate()){
            UpdateDisplay.showUpdateAvailableDialog(mContext, appVersion);
        }else if(appVersion.isRecommendedUpdate()){
            if(!appVersion.isCanRepeatAlert()){
                if(versionPreference.getAppUpdaterShow()) {
                    UpdateDisplay.showUpdateAvailableDialog(mContext, appVersion,true);
//                    versionPreference.setAppUpdaterShow(false);
                }
            }else{
                //now say you want to do something every 10th time they open the app:
                boolean shouldDoThing = (appOpenedCount % appVersion.getRepeatAlertAfter() == 0);
                if (shouldDoThing) {
                    // Check for popup
                    CLog.d("Count Open Before Check Version: " + versionPreference.getOpenAppVersionCounter());
                    if (appVersion.isRecommendedUpdate()) {
                        if( appOpenedCount == 0){
                            if(versionPreference.getAppUpdaterShow())
                                UpdateDisplay.showUpdateAvailableDialog(mContext,appVersion);
                        }

                    }
                    appOpenedCount += 1;
                    //this ensures that the thing does not happen again on an orientation change.
                    versionPreference.setOpenAppVersionCounter(appOpenedCount);
                }
            }

        }
        CLog.d("Count Open After Check Version: " + versionPreference.getOpenAppVersionCounter());
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void checkVersion(){


        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AppVersionService service = restAdapter.create(AppVersionService.class);
        CLog.d("Start Check Version");
//        Timber.d( "Network Start" + location.latitude + " " + location.longitude + " " + radius + " " + excludeLineId);
        String versionName = this.versionName;
        Call<List<AppVersion>> call = service.loadAppVersionInfo(pathUrl,packageName
                , "android", versionName);
        CLog.d("Url: "+call.request().url());
        call.enqueue(callback);


    }

    /**
     * Check if it should be updated
     * @param appVersion
     * @return
     */
    private boolean shouldBeUpdate(AppVersion appVersion){
        AppVersion currentAppVersion = versionPreference.getAppVersion();
        if(currentAppVersion != null){

            // Current version is equal to or less than the received version
            if(VersionUtils.versionCompare(currentAppVersion.getAppVersion()
                    , appVersion.getAppVersion())<=0){
                if(VersionUtils.versionCompare(currentAppVersion.getAppVersion()
                        ,appVersion.getAppVersion())==0) {
                    // Compare if all content is the same don't save
                    if (currentAppVersion.equals(appVersion)) {
                        return false;
                    }else{
                        return true;
                    }
                }else if(VersionUtils.versionCompare(currentAppVersion.getAppVersion()
                        ,appVersion.getAppVersion())<0){
                    return true;
                }
                return true;
            }else{
                // Version is higher expecting wrong fetching
                return true;
            }
        }else{
            return true;
        }

    }

    int number_of_retry = 3;
    int retry_count = 0;
    Callback<List<AppVersion>> callback = new Callback<List<AppVersion>>() {
        @Override
        public void onResponse(Call<List<AppVersion>> call, Response<List<AppVersion>> response) {
            List<AppVersion> appVersion = null;
            Log.d(this.getClass().getSimpleName(),"On Success Network: "+call.request().url());
            if (response.body() != null) {
                Log.d(this.getClass().getSimpleName(),"Network: " + response.body().toString());
                // Load Version
                appVersion = response.body();
                if (appVersion.isEmpty()) {
                    //No new version

                    //Reset Count to 0
                    versionPreference.setAppVersion(null);
                    return;
                } else {
                    // Sort ASCEND (1.0.0, 1.0.1,1.0.3)
                    Collections.sort(appVersion, new AppVersionComparator());
                    // Reverse to DESCEND (1.0.3, 1.0.1,1.0.0)
                    Collections.reverse(appVersion);
//                        Timber.d("App Version: " + appVersion);
                    // Check if there are force in the version
                    boolean foundPreviousForce = false;
                    AppVersion force_appversion = null;
                    for (AppVersion av : appVersion) {
                        //check if force
                        if (av.isForcedUpdate()) {
                            // found force! break!
                            foundPreviousForce = true;
                            force_appversion = av;
                            break;
                        }

                    }

                    // No force found we use the latest version
                    AppVersion latestVersion = appVersion.get(0);
                    // compare latest version to the saved version
                    // If the saved and the latest version is the same (everything)
                    // don't change anything but if not or saved version is null overwrite
                    // **** All comparing are handled in {@link saveVersionPref} ****
                    if (shouldBeUpdate(latestVersion)) {
                        // Update new app version in cache
                        versionPreference.setAppVersion(latestVersion);
                        // Reset the pref
                        versionPreference.setAppUpdaterShow(true);
                        versionPreference.setOpenAppVersionCounter(0);

                        // Notice
//                            UpdateDisplay.showUpdateAvailableDialog(mContext,latestVersion);
//                            return;
                    } else {
                        // Do nothing
                    }
                    if (foundPreviousForce) {
                        AppVersion av = versionPreference.getAppVersion();
                        av.setIsForcedUpdate(true);
                        versionPreference.setAppVersion(av);
                    }

                }

            }
            startCount(versionPreference.getAppVersion());


        }

        @Override
        public void onFailure(Call<List<AppVersion>> call, Throwable t) {
            CLog.d("Network Error: " + call.request().url());
            if(t.getCause() == null)
                CLog.d("Network Error: " + t.getMessage());
            CLog.d("Network Error: " + t.getCause());
//                dismissProgressDialog();
            if(retry_count >= number_of_retry){
                retry_count++;
                start();
            }
            call.cancel();
        }
    };


}
