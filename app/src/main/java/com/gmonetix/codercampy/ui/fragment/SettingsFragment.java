package com.gmonetix.codercampy.ui.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.adapter.LibraryAdapter;
import com.gmonetix.codercampy.chrome.CustomTabActivityHelper;
import com.gmonetix.codercampy.notification.NotificationUtil;
import com.gmonetix.codercampy.util.CoderCampy;
import com.gmonetix.codercampy.util.DesignUtil;
import com.gmonetix.codercampy.util.IntentUtil;
import com.gmonetix.codercampy.util.LibraryDataProvider;
import es.dmoral.toasty.Toasty;

/**
 * Created by Gaurav Bordoloi on 2/25/2018.
 */

public class SettingsFragment extends PreferenceFragment {

    private LibraryAdapter libraryAdapter;

    private CustomTabActivityHelper mCustomTabActivityHelper;

    private SwitchPreference courseNotiPref, postNotiPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        courseNotiPref = (SwitchPreference) findPreference("course_notification");
        postNotiPref = (SwitchPreference) findPreference("post_notification");

        mCustomTabActivityHelper = new CustomTabActivityHelper();
        libraryAdapter = new LibraryAdapter(getActivity());
        libraryAdapter.setList(LibraryDataProvider.getData(getActivity()));

        courseNotiPref.setOnPreferenceChangeListener((preference, newValue) -> {
            if ((Boolean) newValue) {
                NotificationUtil.subscribeToTopic(CoderCampy.TOPIC_COURSE);
                Toasty.success(getActivity(),"Subscribed to course notification", Toast.LENGTH_SHORT,true).show();
            } else {
                NotificationUtil.unSubscribeToTopic(CoderCampy.TOPIC_COURSE);
                Toasty.success(getActivity(),"UnSubscribed to course notification", Toast.LENGTH_SHORT,true).show();
            }
            return true;
        });

        postNotiPref.setOnPreferenceChangeListener((preference, newValue) -> {
            if ((Boolean) newValue) {
                NotificationUtil.subscribeToTopic(CoderCampy.TOPIC_POST);
                Toasty.success(getActivity(),"Subscribed to post notification", Toast.LENGTH_SHORT,true).show();
            } else {
                NotificationUtil.unSubscribeToTopic(CoderCampy.TOPIC_POST);
                Toasty.success(getActivity(),"UnSubscribed to post notification", Toast.LENGTH_SHORT,true).show();
            }
            return true;
        });

        findPreference("libraries").setOnPreferenceClickListener(preference -> {

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
        });

        findPreference("faq").setOnPreferenceClickListener(preference -> {
            IntentUtil.openCustomChromeTab(getActivity(), Uri.parse(CoderCampy.FAQ));
            return false;
        });

        findPreference("privacy_policy").setOnPreferenceClickListener(preference -> {
            IntentUtil.openCustomChromeTab(getActivity(), Uri.parse(CoderCampy.PRIVACY_POLICY));
            return false;
        });

        findPreference("terms_and_conditions").setOnPreferenceClickListener(preference -> {
            IntentUtil.openCustomChromeTab(getActivity(), Uri.parse(CoderCampy.TERMS_AND_CONDITIONS));
            return false;
        });

        findPreference("youtube_terms_of_service").setOnPreferenceClickListener(preference -> {
            IntentUtil.openCustomChromeTab(getActivity(), Uri.parse(CoderCampy.YOUTUBE_TERMS_OF_SERVICE));
            return false;
        });

        findPreference("clear_cache").setOnPreferenceClickListener(preference -> {
            Glide.get(getActivity()).clearMemory();
            Toasty.success(getActivity(), "Cache Cleared", Toast.LENGTH_SHORT,true).show();
            return false;
        });

        findPreference("bug_report").setOnPreferenceClickListener(preference -> {
            IntentUtil.sendEmail(getActivity(),CoderCampy.OFFICIAL_EMAIL,"App Bugs");
            return false;
        });

        findPreference("version").setOnPreferenceClickListener(preference -> {
            IntentUtil.openLink(getActivity(),CoderCampy.PLAYSTORE_LINK);
            return false;
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
