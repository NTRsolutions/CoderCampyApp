package com.gmonetix.codercampy.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmonetix.codercampy.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SplashFragment extends Fragment {

    private View view;
    @BindView(R.id.imageView) ImageView imageView;
    @BindView(R.id.textView) TextView textView;

    private int imageId;
    private String text;

    public SplashFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_splash, container, false);
        ButterKnife.bind(this,view);

        imageView.setImageResource(imageId);
        textView.setText(text);

        return view;
    }

    public void getData(int imageId, String text) {
        this.imageId = imageId;
        this.text = text;
    }

}
