package com.indyzalab.appversioncontroller.version.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.indyzalab.appversioncontroller.R;
import com.indyzalab.appversioncontroller.version.VersionPreference;
import com.indyzalab.appversioncontroller.version.object.AppVersion;

/**
 * Created by IndyZa on 3/20/16 AD.
 * Copyright by IndyZaLab
 */
public class UpdateDisplay {


    static MaterialDialog  materialDialog;

    public static void showUpdateAvailableDialog(final Context context,final AppVersion appVersion){
        showUpdateAvailableDialog(context, appVersion,false);
    }


    public static void showUpdateAvailableDialog(final Context context, final AppVersion appVersion, final boolean disableShowWhenEngage) {
        final VersionPreference versionPreference = new VersionPreference(context);

        MaterialDialog.Builder materialDialogBuilder = new MaterialDialog.Builder(context)
                .title(appVersion.getTitle())
                .content(appVersion.getDescription())
                .positiveText(appVersion.getUpdateTitle(context))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        if(disableShowWhenEngage){
                            versionPreference.setAppUpdaterShow(false);
                        }
                        if (appVersion.isEnableLink()) {
                            try {
                                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(appVersion.getLinkUrl())));
                            } catch (android.content.ActivityNotFoundException anfe) {

                            }
                        } else {
                            // Go to update
                            final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
                            try {
                                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            }
                        }

                    }
                });
        materialDialogBuilder.canceledOnTouchOutside(false);
        if(appVersion.isForcedUpdate()){
            materialDialogBuilder.cancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {

                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }
            });
            materialDialogBuilder.autoDismiss(false);
            materialDialogBuilder.cancelable(true);
            materialDialogBuilder.canceledOnTouchOutside(false);
        }else{
            materialDialogBuilder.negativeText(context.getResources().getString(android.R.string.cancel));
            if(appVersion.isAllowDontShowAgain()){
                materialDialogBuilder.neutralText(context.getResources().getString(R.string.dont_show_again))
                        .onNeutral(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                versionPreference.setAppUpdaterShow(false);
                            }
                        });
            }
        }

        materialDialogBuilder.dismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (disableShowWhenEngage) {
                    versionPreference.setAppUpdaterShow(false);
                }
            }
        });
        materialDialogBuilder.onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog dialog, DialogAction which) {

            }
        });


        if(materialDialog == null) {
            materialDialog = materialDialogBuilder.show();
        }else{
            if(materialDialog.isShowing()){
                materialDialog.dismiss();
            }
            materialDialog = materialDialogBuilder.show();
        }

    }


}
