package com.gmonetix.codercampy;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Gaurav Bordoloi on 2/14/2018.
 */

public class App extends Application{

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private static FirebaseAuth auth;

    @Override
    public void onCreate() {
        super.onCreate();

        auth = FirebaseAuth.getInstance();

    }

    public static FirebaseAuth getAuth() {
        return auth;
    }

}
