package com.gmonetix.codercampy.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.gmonetix.codercampy.util.GlideOptions;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstructorDetailsActivity extends AppCompatActivity {

    @BindView(R.id.instructor_image) ImageView imageView;
    @BindView(R.id.instructor_name) TextView tvName;
    @BindView(R.id.instructor_email) TextView tvEmail;
    @BindView(R.id.instructor_phone) TextView tvPhone;
    @BindView(R.id.instructor_bio) TextView tvBio;
    @BindView(R.id.see_courses) MaterialRippleLayout seeAllCourses;
    @BindView(R.id.recycelrview_categories) RecyclerView categoriesRecyclerView;
    @BindView(R.id.recycelrview_languages) RecyclerView languagesRecyclerView;

    private Instructor instructor;

    private APIInterface apiInterface;

    private ImageAdapter categoriesAdapter;
    private ImageAdapter languagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_details);
        ButterKnife.bind(this);

        instructor = (Instructor) getIntent().getSerializableExtra("instructor");
        apiInterface = APIClient.getClient().create(APIInterface.class);

        tvName.setText(instructor.name);
        Glide.with(this).load(instructor.image).apply(GlideOptions.getRequestOptions(R.drawable.ic_default_user,R.drawable.ic_default_user)).into(imageView);
        tvEmail.setText(instructor.email);
        tvPhone.setText(instructor.phone);
        tvBio.setText(instructor.bio);

        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        languagesRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        categoriesAdapter = new ImageAdapter(this);
        languagesAdapter= new ImageAdapter(this);
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

        seeAllCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    public void back(View view) {
        onBackPressed();
    }

}
