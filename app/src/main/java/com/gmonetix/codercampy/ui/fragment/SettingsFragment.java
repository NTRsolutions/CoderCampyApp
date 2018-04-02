package com.gmonetix.codercampy.ui.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.widget.LinearLayoutManager;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.adapter.LibraryAdapter;
import com.gmonetix.codercampy.chrome.CustomTabActivityHelper;
import com.gmonetix.codercampy.util.CoderCampy;
import com.gmonetix.codercampy.util.DesignUtil;
import com.gmonetix.codercampy.util.IntentUtil;
import com.gmonetix.codercampy.util.LibraryDataProvider;

/**
 * Created by Gaurav Bordoloi on 2/25/2018.
 */

public class SettingsFragment extends PreferenceFragment {

    private LibraryAdapter libraryAdapter;

    private CustomTabActivityHelper mCustomTabActivityHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        mCustomTabActivityHelper = new CustomTabActivityHelper();
        libraryAdapter = new LibraryAdapter(getActivity());
        libraryAdapter.setList(LibraryDataProvider.getData(getActivity()));

        findPreference("libraries").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                new MaterialDialog.Builder(getActivity())
                        .title("Open Source Libraries")
                        .icon(getResources().getDrawable(R.drawable.ic_info))
                        .typeface(DesignUtil.getTypeFace(getActivity()),DesignUtil.getTypeFace(getActivity()))
                        .adapter(libraryAdapter, new LinearLayoutManager(getActivity()))
                        .positiveText("CLOSE")
                        .titleColor(getResources().getColor(R.color.colorPrimary))
                        .positiveColor(getResources().getColor(R.color.colorPrimary))
                        .show();

                return false;
            }
        });

        findPreference("faq").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                IntentUtil.openCustomChromeTab(getActivity(), Uri.parse(CoderCampy.FAQ));
                return false;
            }
        });

        findPreference("privacy_policy").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                IntentUtil.openCustomChromeTab(getActivity(), Uri.parse(CoderCampy.PRIVACY_POLICY));
                return false;
            }
        });

        findPreference("terms_and_conditions").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                IntentUtil.openCustomChromeTab(getActivity(), Uri.parse(CoderCampy.TERMS_AND_CONDITIONS));
                return false;
            }
        });

        findPreference("youtube_terms_of_service").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                IntentUtil.openCustomChromeTab(getActivity(), Uri.parse(CoderCampy.YOUTUBE_TERMS_OF_SERVICE));
                return false;
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mCustomTabActivityHelper.bindCustomTabsService(getActivity());
    }

    @Override
    public void onStop() {
        mCustomTabActivityHelper.unbindCustomTabsService(getActivity());
        super.onStop();
    }

}
