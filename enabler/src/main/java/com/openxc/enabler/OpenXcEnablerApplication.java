package com.openxc.enabler;

import android.app.Application;

public class OpenXcEnablerApplication extends Application {

    private static OpenXcEnablerApplication mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static OpenXcEnablerApplication getContext() {
        return mContext;
    }

}
