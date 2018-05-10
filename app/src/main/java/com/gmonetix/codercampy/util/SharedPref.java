package com.gmonetix.codercampy.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Gaurav Bordoloi on 3/28/2018.
 */

public class SharedPref {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SharedPref(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.editor = sharedPreferences.edit();
        this.context = context;
    }

    public boolean getCourseNotificationValue(){
        return sharedPreferences.getBoolean("course_notification", true);
    }

    public boolean getPostNotificationValue(){
        return sharedPreferences.getBoolean("post_notification", true);
    }

}
