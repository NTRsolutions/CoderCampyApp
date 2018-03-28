package com.gmonetix.codercampy.ui.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import com.gmonetix.codercampy.R;

/**
 * Created by Gaurav Bordoloi on 2/25/2018.
 */

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
