package com.gmonetix.codercampy.ui.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.balysv.materialripple.MaterialRippleLayout;
import com.gmonetix.codercampy.App;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.ui.fragment.SplashFragment;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.skip) MaterialRippleLayout skip;
    @BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.slide_indicator) CircleIndicator circleIndicator;

    private int[] imageIds;
    private String[] texts;

    private PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        imageIds = new int[] {R.drawable.udacity,R.drawable.slide1,R.drawable.udacity};
        texts = new String[]{"where code creates magic",
                "We provide bike sharing services",
                "No docking stations"};

        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        circleIndicator.setViewPager(viewPager);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startHomeActivity();
            }
        });

    }

    class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            SplashFragment f = new SplashFragment();
            f.getData(imageIds[position],texts[position]);
            return f;
        }

        @Override
        public int getCount() {
            return 3;
        }

    }

    @OnClick(R.id.btn_login)
    void login() {
        startActivity(new Intent(this,LoginActivity.class));
    }

    @OnClick(R.id.btn_signup)
    void signUp() {
        startActivity(new Intent(this,SignUpActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUI(App.getAuth().getCurrentUser());
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            startHomeActivity();
        }
    }

    private void startHomeActivity() {
        Intent intent = new Intent(this,Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        this.finish();
    }

}
