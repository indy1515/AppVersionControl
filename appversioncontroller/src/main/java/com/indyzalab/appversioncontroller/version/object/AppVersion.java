package com.indyzalab.appversioncontroller.version.object;


import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.indyzalab.appversioncontroller.R;

/**
 * Created by IndyZa on 3/19/16 AD.
 * Copyright by IndyZaLab
 */
public class AppVersion {

    @SerializedName("is_forced")
    private boolean isForcedUpdate;

    @SerializedName("is_recommended")
    private boolean isRecommendedUpdate;

    @SerializedName("version")
    private String appVersion = "";

    @SerializedName("title")
    private String title = "";

    @SerializedName("description")
    private String description = "";

    @SerializedName("enable_link")
    private boolean enableLink;

    @SerializedName("link_title")
    private String linkTitle = "";

    @SerializedName("link_url")
    private String linkUrl = "";

    @SerializedName("allow_dont_show_again")
    private boolean allowDontShowAgain;

    @SerializedName("can_repeat_alert")
    private boolean canRepeatAlert;

    @SerializedName("repeat_alert_every")
    private int repeatAlertAfter;

    public AppVersion(boolean isForcedUpdate, boolean isRecommendedUpdate, String appVersion
            , String title, String description, boolean enableLink, String linkTitle, String linkUrl
            , boolean allowDontShowAgain, boolean canRepeatAlert, int repeatAlertAfter) {
        this.isForcedUpdate = isForcedUpdate;
        this.isRecommendedUpdate = isRecommendedUpdate;
        this.appVersion = appVersion;
        this.title = title;
        this.description = description;
        this.enableLink = enableLink;
        this.linkTitle = linkTitle;
        this.linkUrl = linkUrl;
        this.allowDontShowAgain = allowDontShowAgain;
        this.canRepeatAlert = canRepeatAlert;
        this.repeatAlertAfter = repeatAlertAfter;
    }

    public void setIsForcedUpdate(boolean isForcedUpdate) {
        this.isForcedUpdate = isForcedUpdate;
    }

    public boolean isForcedUpdate() {
        return isForcedUpdate;
    }

    public boolean isRecommendedUpdate() {
        return isRecommendedUpdate;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUpdateTitle(Context mContext) {
        if(isEnableLink()){
            return getLinkTitle();
        }else{
            return mContext.getString(R.string.update);
        }
    }


    public boolean isEnableLink() {
        return enableLink;
    }

    private String getLinkTitle() {
        return linkTitle;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public boolean isAllowDontShowAgain() {
        return allowDontShowAgain;
    }

    public boolean isCanRepeatAlert() {
        return canRepeatAlert;
    }

    public int getRepeatAlertAfter() {
        return repeatAlertAfter;
    }

    @Override
    public String toString() {
        return "AppVersion{" +
                "isForcedUpdate=" + isForcedUpdate +
                ", isRecommendedUpdate=" + isRecommendedUpdate +
                ", appVersion='" + appVersion + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", enableLink=" + enableLink +
                ", linkTitle='" + linkTitle + '\'' +
                ", linkUrl='" + linkUrl + '\'' +
                ", allowDontShowAgain=" + allowDontShowAgain +
                ", canRepeatAlert=" + canRepeatAlert +
                ", repeatAlertAfter=" + repeatAlertAfter +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppVersion)) return false;

        AppVersion that = (AppVersion) o;

        if (isForcedUpdate() != that.isForcedUpdate()) return false;
        if (isRecommendedUpdate() != that.isRecommendedUpdate()) return false;
        if (isEnableLink() != that.isEnableLink()) return false;
        if (isAllowDontShowAgain() != that.isAllowDontShowAgain()) return false;
        if (isCanRepeatAlert() != that.isCanRepeatAlert()) return false;
        if (getRepeatAlertAfter() != that.getRepeatAlertAfter()) return false;
        if (!getAppVersion().equals(that.getAppVersion())) return false;
        if (!getTitle().equals(that.getTitle())) return false;
        if (!getDescription().equals(that.getDescription())) return false;
        if (!getLinkTitle().equals(that.getLinkTitle())) return false;
        return getLinkUrl().equals(that.getLinkUrl());

    }

    @Override
    public int hashCode() {
        int result = (isForcedUpdate() ? 1 : 0);
        result = 31 * result + (isRecommendedUpdate() ? 1 : 0);
        result = 31 * result + getAppVersion().hashCode();
        result = 31 * result + getTitle().hashCode();
        result = 31 * result + getDescription().hashCode();
        result = 31 * result + (isEnableLink() ? 1 : 0);
        result = 31 * result + getLinkTitle().hashCode();
        result = 31 * result + getLinkUrl().hashCode();
        result = 31 * result + (isAllowDontShowAgain() ? 1 : 0);
        result = 31 * result + (isCanRepeatAlert() ? 1 : 0);
        result = 31 * result + getRepeatAlertAfter();
        return result;
    }
}
