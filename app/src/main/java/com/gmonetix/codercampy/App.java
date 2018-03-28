package com.gmonetix.codercampy;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

/**
 * Created by Gaurav Bordoloi on 2/14/2018.
 */

public class App extends Application{

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private static FirebaseFirestoreSettings firebaseFirestoreSettings;
    private static FirebaseAuth auth;

    @Override
    public void onCreate() {
        super.onCreate();

        firebaseFirestoreSettings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();

        auth = FirebaseAuth.getInstance();

    }

    public static FirebaseFirestoreSettings getFirestoreOfflineSettings() {
        return firebaseFirestoreSettings;
    }

    public static FirebaseAuth getAuth() {
        return auth;
    }

}
