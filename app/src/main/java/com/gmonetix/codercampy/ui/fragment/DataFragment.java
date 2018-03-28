package com.gmonetix.codercampy.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gmonetix.codercampy.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import im.delight.android.webview.AdvancedWebView;

public class DataFragment extends Fragment implements AdvancedWebView.Listener{

    private View rootView;
    @BindView(R.id.webview) AdvancedWebView webview;

    private static final String ARG_PARAM1 = "courseId";
    private String courseId;

    public DataFragment() { }

    public static DataFragment newInstance(String course_id) {
        DataFragment fragment = new DataFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, course_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            courseId = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_data, container, false);
            ButterKnife.bind(this,rootView);

            webview.setListener(getActivity(), this);
            webview.setDesktopMode(false);

            webview.loadData("","text/html","UTF-8");


        }

        return rootView;
    }

    @SuppressLint("NewApi")
    @Override
    public void onResume() {
        super.onResume();
        webview.onResume();
        // ...
    }

    @SuppressLint("NewApi")
    @Override
    public void onPause() {
        webview.onPause();
        // ...
        super.onPause();
    }

    @Override
    public void onDestroy() {
        webview.onDestroy();
        // ...
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        webview.onActivityResult(requestCode, resultCode, intent);
        // ...
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) { }

    @Override
    public void onPageFinished(String url) { }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) { }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) { }

    @Override
    public void onExternalPageRequest(String url) { }

}
