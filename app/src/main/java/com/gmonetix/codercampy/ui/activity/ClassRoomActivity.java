package com.gmonetix.codercampy.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gmonetix.codercampy.App;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.adapter.LectureAdapter;
import com.gmonetix.codercampy.dialog.CourseInfoDialog;
import com.gmonetix.codercampy.dialog.CourseOverViewDialog;
import com.gmonetix.codercampy.dialog.RatingDialog;
import com.gmonetix.codercampy.listener.OnLectureClickListener;
import com.gmonetix.codercampy.model.Course;
import com.gmonetix.codercampy.model.Dummy;
import com.gmonetix.codercampy.model.Lecture;
import com.gmonetix.codercampy.model.Response;
import com.gmonetix.codercampy.networking.APIClient;
import com.gmonetix.codercampy.networking.APIInterface;
import com.gmonetix.codercampy.ui.fragment.DiscussionPanel;
import com.gmonetix.codercampy.util.DesignUtil;
import com.gmonetix.codercampy.util.NestedWebView;
import com.gmonetix.codercampy.viewmodel.RatingViewModel;
import com.gmonetix.codercampy.viewmodel.UserViewModel;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.sackcentury.shinebuttonlib.ShineButton;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;

public class ClassRoomActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener{

    private final static String COURSE = "course";

    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.lectures_recyclerView) RecyclerView recyclerView;
    @BindView(R.id.webView) NestedWebView webView;
    @BindView(R.id.course_info) FloatingActionButton courseInfo;
    @BindView(R.id.courseName) TextView courseName;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.favbtn) ShineButton favBtn;

    private YouTubePlayer youTubePlayer;
    private boolean isFullScreen = false;

    private List<String> videoIds;
    private LectureAdapter lectureAdapter;
    private int index = 0;

    private Course course;

    private DiscussionPanel discussionPanel;

    private APIInterface apiInterface;
    private UserViewModel userViewModel;
    private RatingViewModel ratingViewModel;

    String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/open_sans_regular.ttf\")}body {font-family: MyFont;font-size: medium;text-align: justify;}</style></head><body>";
    String pas = "</body></html>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_room);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        this.setTitle("");
        DesignUtil.applyFontForToolbarTitle(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (getIntent().hasExtra(COURSE))
            course = (Course) getIntent().getSerializableExtra(COURSE);
        else {
            Toast.makeText(this, "Some error occurred! Raise a support ticket", Toast.LENGTH_SHORT).show();
            this.finish();
        }

        apiInterface = APIClient.getClient().create(APIInterface.class);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        ratingViewModel = ViewModelProviders.of(this).get(RatingViewModel.class);

        if (App.getAuth().getCurrentUser() == null) {
            favBtn.setVisibility(View.GONE);
        } else {
            favBtn.setVisibility(View.VISIBLE);
            if (Home.user.favourites != null) {
                if (Home.user.favourites.contains(course.id)) {
                    favBtn.setChecked(true);
                } else {
                    favBtn.setChecked(false);
                }
            }
        }

        videoIds = new ArrayList<>();
        courseName.setText(course.name);

        for (Lecture l : course.lectures) {
            videoIds.add(l.video);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        lectureAdapter = new LectureAdapter(this, new OnLectureClickListener() {
            @Override
            public void onLectureClick(int pos, String htmlData, String videoId) {
                index = pos;
                youTubePlayer.pause();
                youTubePlayer.loadVideo(videoId);
                webView.loadDataWithBaseURL(null,pish + htmlData + pas, "text/html", "UTF-8", null);
            }
        });
        recyclerView.setAdapter(lectureAdapter);
        lectureAdapter.setList(course.lectures);

        YouTubePlayerSupportFragment frag = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_fragment);
        frag.initialize(getResources().getString(R.string.youtube_api_key), this);

        courseInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseInfoDialog.newInstance(course.image,course.name,course.category,course.languages,course.description).show(getFragmentManager(),"InfoDialog");
            }
        });

        discussionPanel = DiscussionPanel.newInstance(course.id,true);
        webView.loadDataWithBaseURL(null,pish + course.lectures.get(index).data + pas, "text/html", "UTF-8", null);

        favBtn.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean checked) {
                if (checked) {
                    apiInterface.addFav(course.id, App.getAuth().getCurrentUser().getUid()).enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                            if (response.body().code.equals("success")) {
                                Home.user.favourites.add(course.id);
                                Toasty.success(ClassRoomActivity.this,"Added to favourites", Toast.LENGTH_SHORT,true).show();
                                EventBus.getDefault().postSticky(new Dummy());
                            } else {
                                favBtn.setChecked(false);
                                Toasty.error(ClassRoomActivity.this,"Error adding to favourite", Toast.LENGTH_SHORT,true).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Response> call, Throwable t) {
                            favBtn.setChecked(false);
                            Toasty.error(ClassRoomActivity.this,"Error adding to favourite", Toast.LENGTH_SHORT,true).show();
                        }
                    });
                } else {
                    apiInterface.removeFav(course.id, App.getAuth().getCurrentUser().getUid()).enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                            if (response.body().code.equals("success")) {
                                Home.user.favourites.remove(course.id);
                                Toasty.success(ClassRoomActivity.this,"Removed from favourites", Toast.LENGTH_SHORT,true).show();
                                EventBus.getDefault().postSticky(new Dummy());
                            } else {
                                favBtn.setChecked(true);
                                Toasty.error(ClassRoomActivity.this,"Error removing from favourite", Toast.LENGTH_SHORT,true).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Response> call, Throwable t) {
                            favBtn.setChecked(true);
                            Toasty.error(ClassRoomActivity.this,"Error removing from favourite", Toast.LENGTH_SHORT,true).show();
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(Gravity.END)) {
            drawer.closeDrawer(Gravity.END);
        } else {
            if (isFullScreen) {
                youTubePlayer.setFullscreen(false);
                isFullScreen = false;
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.class_room, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_overview:
                new CourseOverViewDialog(this,course.name,course.overview,course.instructor).show();
                break;

            case R.id.menu_rating:
                RatingDialog.newInstance(course.id,true).show(getFragmentManager(),"RatingDialog");
                break;

            case R.id.menu_discussions:
                discussionPanel.show(getSupportFragmentManager(),"TAG");
                break;

            case R.id.menu_lectures:
                drawer.openDrawer(Gravity.END);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        this.youTubePlayer = youTubePlayer;

        youTubePlayer.loadVideos(videoIds);

        youTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
            @Override
            public void onFullscreen(boolean b) {
                isFullScreen = true;
            }
        });

        youTubePlayer.setPlaylistEventListener(new YouTubePlayer.PlaylistEventListener() {
            @Override
            public void onPrevious() {
                index--;
                webView.loadDataWithBaseURL(null,pish + course.lectures.get(index).data + pas, "text/html", "UTF-8", null);
            }

            @Override
            public void onNext() {
                index++;
                webView.loadDataWithBaseURL(null,pish + course.lectures.get(index).data + pas, "text/html", "UTF-8", null);
            }

            @Override
            public void onPlaylistEnded() {
                //TODO
            }
        });

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this, "" + youTubeInitializationResult.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        if (youTubePlayer != null)
            youTubePlayer.release();
        super.onDestroy();
    }

}
