package com.indyzalab.appversioncontroller.version.comparator;


import com.indyzalab.appversioncontroller.version.object.AppVersion;
import com.indyzalab.appversioncontroller.version.util.VersionUtils;

import java.util.Comparator;

/**
 * Created by IndyZa on 3/20/16 AD.
 * Copyright by IndyZaLab
 */
public class AppVersionComparator implements Comparator<AppVersion> {
    @Override
    public int compare(AppVersion lhs, AppVersion rhs) {
        return VersionUtils.versionCompare(lhs.getAppVersion(), rhs.getAppVersion());
    }
}
