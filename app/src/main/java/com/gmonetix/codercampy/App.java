package com.gmonetix.codercampy;

import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;
import com.gmonetix.codercampy.util.SharedPref;
import com.google.firebase.auth.FirebaseAuth;
import io.github.kbiakov.codeview.classifier.CodeProcessor;

/**
 * Created by Gaurav Bordoloi on 2/14/2018.
 */

public class App extends MultiDexApplication {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private static FirebaseAuth auth;
    private static SharedPref sharedPref;

    @Override
    public void onCreate() {
        super.onCreate();

        sharedPref = new SharedPref(getApplicationContext());

        auth = FirebaseAuth.getInstance();

        CodeProcessor.init(this);

    }

    public static FirebaseAuth getAuth() {
        return auth;
    }

    public static SharedPref getSharedPref() {
        return sharedPref;
    }

}
