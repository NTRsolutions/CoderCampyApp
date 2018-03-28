package com.gmonetix.codercampy.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.adapter.ViewPagerAdapter;
import com.gmonetix.codercampy.listener.OnLectureClickListener;
import com.gmonetix.codercampy.model.Lecture;
import com.gmonetix.codercampy.ui.fragment.CurriculumFragment;
import com.gmonetix.codercampy.ui.fragment.DataFragment;
import com.gmonetix.codercampy.ui.fragment.DiscussionFragment;
import com.gmonetix.codercampy.ui.fragment.RatingFragment;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClassRoomActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener, OnLectureClickListener {

    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.viewpager) ViewPager viewPager;

    private final static String COURSE_ID = "course_id";
    private final static String COURSE_LECTURES = "course_lectures";

    private YouTubePlayer youTubePlayer;
    private boolean isFullScreen = false;

    private ViewPagerAdapter adapter;

    private String courseId;
    private List<Lecture> lectureList = new ArrayList<>();
    private List<String> videosList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_room);
        ButterKnife.bind(this);

        if (getIntent().hasExtra(COURSE_ID)) {
            courseId = getIntent().getStringExtra(COURSE_ID);
            lectureList = (List<Lecture>) getIntent().getSerializableExtra(COURSE_LECTURES);
        }
        else {
            Toast.makeText(this, "Some error occurred! Please contact us", Toast.LENGTH_SHORT).show();
            this.finish();
        }

        for (Lecture l : lectureList) {
            videosList.add(l.video);
        }

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        YouTubePlayerSupportFragment frag = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_fragment);
        frag.initialize(getResources().getString(R.string.youtube_api_key), this);

    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(CurriculumFragment.newInstance(lectureList),"Curriculum");
        adapter.addFragment(DataFragment.newInstance(courseId),"Data");
        adapter.addFragment(DiscussionFragment.newInstance(courseId),"Discussion");
        //adapter.addFragment(RatingFragment.newInstance(courseId),"Rating");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (isFullScreen) {
            youTubePlayer.setFullscreen(false);
            isFullScreen = false;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        ClassRoomActivity.this.youTubePlayer = youTubePlayer;

        youTubePlayer.loadVideos(videosList);

        youTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
            @Override
            public void onFullscreen(boolean b) {
                isFullScreen = true;
            }
        });

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this, "" + youTubeInitializationResult.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLectureClick(String lectureId, String videoId) {
        if (youTubePlayer.isPlaying()) {
            youTubePlayer.pause();
        }
        youTubePlayer.loadVideo(videoId);
    }

    @Override
    protected void onDestroy() {
        if (youTubePlayer != null)
            youTubePlayer.release();
        super.onDestroy();
    }
}
