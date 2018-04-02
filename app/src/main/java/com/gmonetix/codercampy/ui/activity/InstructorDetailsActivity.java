package com.gmonetix.codercampy.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.adapter.ImageAdapter;
import com.gmonetix.codercampy.model.Category;
import com.gmonetix.codercampy.model.Instructor;
import com.gmonetix.codercampy.model.Language;
import com.gmonetix.codercampy.networking.APIClient;
import com.gmonetix.codercampy.networking.APIInterface;
import com.gmonetix.codercampy.util.DesignUtil;
import com.gmonetix.codercampy.util.GlideOptions;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstructorDetailsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.instructor_image) CircleImageView imageView;
    @BindView(R.id.instructor_bio) TextView tvBio;
    @BindView(R.id.recycelrview_categories) RecyclerView categoriesRecyclerView;
    @BindView(R.id.recycelrview_languages) RecyclerView languagesRecyclerView;

    //social
    @BindView(R.id.fb) ImageView fb;
    @BindView(R.id.linkedIn) ImageView linkedIn;
    @BindView(R.id.call) ImageView call;
    @BindView(R.id.email) ImageView email;
    @BindView(R.id.whatsapp) ImageView whatsapp;

    private Instructor instructor;

    private APIInterface apiInterface;

    private ImageAdapter categoriesAdapter;
    private ImageAdapter languagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        DesignUtil.applyFontForToolbarTitle(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        instructor = (Instructor) getIntent().getSerializableExtra("instructor");
        this.setTitle(instructor.name);

        if (instructor.facebook == null)
            fb.setVisibility(View.GONE);
        if (instructor.whatsapp == null)
            whatsapp.setVisibility(View.GONE);
        if (instructor.linkedin == null)
            linkedIn.setVisibility(View.GONE);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        Glide.with(this).load(instructor.image).apply(GlideOptions.getRequestOptions(R.drawable.ic_default_user,R.drawable.ic_default_user)).into(imageView);
        tvBio.setText(instructor.bio);

        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        languagesRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        categoriesAdapter = new ImageAdapter(this, new ImageAdapter.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleTooltip.Builder(InstructorDetailsActivity.this)
                        .anchorView(v)
                        .text("Texto do Tooltip")
                        .gravity(Gravity.BOTTOM)
                        .animated(true)
                        .transparentOverlay(false)
                        .build()
                        .show();
            }
        });
        languagesAdapter= new ImageAdapter(this, new ImageAdapter.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleTooltip.Builder(InstructorDetailsActivity.this)
                        .anchorView(v)
                        .text("Texto do Tooltip")
                        .gravity(Gravity.BOTTOM)
                        .animated(true)
                        .transparentOverlay(false)
                        .build()
                        .show();
            }
        });
        categoriesRecyclerView.setAdapter(categoriesAdapter);
        languagesRecyclerView.setAdapter(languagesAdapter);

        Call<List<Language>> languagesCall = apiInterface.getLanguages(instructor.getLanguagesAsString());
        languagesCall.enqueue(new Callback<List<Language>>() {
            @Override
            public void onResponse(Call<List<Language>> call, Response<List<Language>> response) {

                List<String> list = new ArrayList<>();
                for (Language l : response.body()) {
                    list.add(l.image);
                }

                languagesAdapter.setList(list);
            }

            @Override
            public void onFailure(Call<List<Language>> call, Throwable t) {
                Toast.makeText(InstructorDetailsActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Call<List<Category>> categoriesCall = apiInterface.getCategories(instructor.getCategoriesAsString());
        categoriesCall.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {

                List<String> list = new ArrayList<>();
                for (Category c : response.body()) {
                    list.add(c.image);
                }

                categoriesAdapter.setList(list);
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(InstructorDetailsActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        linkedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.instructor_details_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_all_courses:

                Intent intent = new Intent(this,InstructorCoursesActivity.class);
                intent.putExtra("instructor_id",instructor.id);
                intent.putExtra("instructor_name",instructor.name);
                startActivity(intent);

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void back(View view) {
        onBackPressed();
    }

}
