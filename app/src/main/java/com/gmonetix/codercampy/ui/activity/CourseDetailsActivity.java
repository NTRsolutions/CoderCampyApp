package com.gmonetix.codercampy.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.adapter.ViewPagerAdapter;
import com.gmonetix.codercampy.listener.OnButtonClickListener;
import com.gmonetix.codercampy.listener.OnLectureClickListener;
import com.gmonetix.codercampy.model.Course;
import com.gmonetix.codercampy.ui.fragment.CourseGeneralFragment;
import com.gmonetix.codercampy.ui.fragment.CourseOverviewFragment;
import com.gmonetix.codercampy.ui.fragment.CurriculumFragment;
import com.gmonetix.codercampy.ui.fragment.DiscussionFragment;
import com.gmonetix.codercampy.ui.fragment.RatingFragment;
import com.gmonetix.codercampy.util.GlideOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseDetailsActivity extends AppCompatActivity implements OnLectureClickListener, OnButtonClickListener{

    @BindView(R.id.course_image) ImageView courseImage;
    @BindView(R.id.course_tabs) TabLayout tabLayout;
    @BindView(R.id.viewpager) ViewPager viewPager;

    private ViewPagerAdapter adapter;

    private Course course;

    private final static String COURSE_ID = "course_id";
    private final static String COURSE_LECTURES = "course_lectures";
    private final static String COURSE = "course";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        ButterKnife.bind(this);

        if (getIntent().hasExtra(COURSE))
            course = (Course) getIntent().getSerializableExtra(COURSE);
        else {
            Toast.makeText(this, "Some error occurred! Raise a support ticket", Toast.LENGTH_SHORT).show();
            this.finish();
        }

        Glide.with(this).load(course.image).apply(GlideOptions.getRequestOptions(R.drawable.course_default,R.drawable.course_default)).into(courseImage);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(CourseGeneralFragment.newInstance(course.id,course.name,course.description,course.category, (ArrayList<String>) course.languages),"Curriculum");
        adapter.addFragment(CourseOverviewFragment.newInstance(course.overview,course.instructor),"OverView");
        adapter.addFragment(CurriculumFragment.newInstance(course.lectures),"Curriculum");
        adapter.addFragment(DiscussionFragment.newInstance(course.id),"Discussion");
        adapter.addFragment(RatingFragment.newInstance(course.id, course.ratings),"Rating");
        viewPager.setAdapter(adapter);
    }

    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void onLectureClick(String lectureId, String videoId) {
        Intent intent = new Intent(this,ClassRoomActivity.class);
        intent.putExtra(COURSE_ID,course.id);
        intent.putExtra(COURSE_LECTURES, (Serializable) course.lectures);
        startActivity(intent);
    }

    @Override
    public void onButtonClicked() {
        Intent intent = new Intent(this,ClassRoomActivity.class);
        intent.putExtra(COURSE_ID,course.id);
        intent.putExtra(COURSE_LECTURES, (Serializable) course.lectures);
        startActivity(intent);
    }
}
