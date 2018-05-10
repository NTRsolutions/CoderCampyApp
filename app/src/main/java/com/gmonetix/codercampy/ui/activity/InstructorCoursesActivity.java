package com.gmonetix.codercampy.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.adapter.CourseAdapter;
import com.gmonetix.codercampy.model.Course;
import com.gmonetix.codercampy.networking.APIClient;
import com.gmonetix.codercampy.networking.APIInterface;
import com.gmonetix.codercampy.util.CourseItemAnimator;
import com.gmonetix.codercampy.util.GridItemDecoration;
import com.gmonetix.codercampy.util.DesignUtil;
import com.gmonetix.codercampy.viewmodel.InstructorViewModel;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class InstructorCoursesActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.courses_shimmer) ShimmerFrameLayout shimmer;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private List<Course> courseList;
    private CourseAdapter adapter;

    private APIInterface apiInterface;
    private InstructorViewModel instructorViewModel;

    private MenuItem searchItem;
    private SearchView searchView;

    private String instructorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_courses);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        DesignUtil.applyFontForToolbarTitle(this);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        if (getIntent() != null) {
            instructorId = getIntent().getStringExtra("instructor_id");
            this.setTitle(getIntent().getStringExtra("instructor_name").split(" ")[0] + "'s Courses");
        } else {
            //TODO
        }

        shimmer.startShimmerAnimation();

        apiInterface = APIClient.getClient().create(APIInterface.class);
        instructorViewModel = ViewModelProviders.of(this).get(InstructorViewModel.class);

        courseList = new ArrayList<>();
//        adapter = new CourseAdapter(this,apiInterface);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setItemAnimator(new CourseItemAnimator());
        recyclerView.addItemDecoration(new GridItemDecoration(50));
        recyclerView.setHasFixedSize(true);
      //  recyclerView.setAdapter(adapter);

        instructorViewModel.getCourses(instructorId).observe(this,courses->{

            courseList.addAll(courses);
            adapter.setList(courseList);

            shimmer.stopShimmerAnimation();
            shimmer.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        searchItem = menu.findItem(R.id.menu_search);
        searchView = new SearchView(this);
        int searchImgId = android.support.v7.appcompat.R.id.search_button;
        int searchCloseImgId = android.support.v7.appcompat.R.id.search_close_btn;
        int mag = android.support.v7.appcompat.R.id.search_src_text;
        ImageView v = (ImageView) searchView.findViewById(searchImgId);
        ImageView v1 = (ImageView) searchView.findViewById(searchCloseImgId);
        EditText v2 = (EditText) searchView.findViewById(mag);
        v.setImageResource(R.drawable.ic_search);
        v1.setImageResource(R.drawable.ic_close);
        v2.setHintTextColor(Color.parseColor("#9e9e9e"));
        v2.setTextColor(Color.BLACK);
        v2.setHint("search...");

        searchView.setOnQueryTextListener(this);

        searchItem.setActionView(searchView);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.isEmpty()) {
            adapter.getFilter().filter("");
        } else {
            adapter.getFilter().filter(newText.toLowerCase());
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (searchItem!= null) {
            if (searchItem.isVisible() && !searchView.isIconified()) {
                searchView.onActionViewCollapsed();
            } else super.onBackPressed();
        } else super.onBackPressed();
    }
}
