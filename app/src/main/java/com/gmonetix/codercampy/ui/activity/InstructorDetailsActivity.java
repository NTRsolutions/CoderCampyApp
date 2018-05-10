package com.gmonetix.codercampy.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.adapter.ImageAdapter;
import com.gmonetix.codercampy.model.Category;
import com.gmonetix.codercampy.model.Instructor;
import com.gmonetix.codercampy.model.Language;
import com.gmonetix.codercampy.util.DesignUtil;
import com.gmonetix.codercampy.util.GlideOptions;
import com.gmonetix.codercampy.util.ImageViewItemAnimator;
import com.gmonetix.codercampy.util.IntentUtil;
import com.gmonetix.codercampy.viewmodel.InstructorViewModel;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class InstructorDetailsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.instructor_image) CircleImageView imageView;
    @BindView(R.id.instructor_bio) TextView tvBio;
    @BindView(R.id.recycelrview_categories) RecyclerView categoriesRecyclerView;
    @BindView(R.id.recycelrview_languages) RecyclerView languagesRecyclerView;
    @BindView(R.id.categories_shimmer) ShimmerFrameLayout categoriesShimmer;
    @BindView(R.id.languages_shimmer) ShimmerFrameLayout languagesShimmer;

    //social
    @BindView(R.id.fb) ImageView fb;
    @BindView(R.id.linkedIn) ImageView linkedIn;
    @BindView(R.id.call) ImageView call;
    @BindView(R.id.email) ImageView email;
    @BindView(R.id.whatsapp) ImageView whatsapp;

    private Instructor instructor;

    private InstructorViewModel instructorViewModel;

    private ImageAdapter categoriesAdapter;
    private ImageAdapter languagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        DesignUtil.applyFontForToolbarTitle(this);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        categoriesShimmer.startShimmerAnimation();
        languagesShimmer.startShimmerAnimation();

        instructor = (Instructor) getIntent().getSerializableExtra("instructor");
        this.setTitle(instructor.name);

        if (instructor.facebook == null)
            fb.setVisibility(View.GONE);
        if (instructor.whatsapp == null)
            whatsapp.setVisibility(View.GONE);
        if (instructor.linkedin == null)
            linkedIn.setVisibility(View.GONE);

        instructorViewModel = ViewModelProviders.of(this).get(InstructorViewModel.class);

        Glide.with(this).load(instructor.image).apply(GlideOptions.getRequestOptions(R.drawable.ic_default_user,R.drawable.ic_default_user)).into(imageView);
        tvBio.setText(instructor.bio);

        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        languagesRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        categoriesRecyclerView.setItemAnimator(new ImageViewItemAnimator());
        languagesRecyclerView.setItemAnimator(new ImageViewItemAnimator());

        categoriesAdapter = new ImageAdapter(this, pos->{

            instructorViewModel.getCategories(instructor.getCategoriesAsString()).observe(this,categories->{

                Toasty.success(InstructorDetailsActivity.this,categories.get(pos).name,Toast.LENGTH_SHORT,false).show();

            });

        });

        languagesAdapter= new ImageAdapter(this, pos->{

            instructorViewModel.getLanguages(instructor.getLanguagesAsString()).observe(this,languages->{

                Toasty.success(InstructorDetailsActivity.this,languages.get(pos).name,Toast.LENGTH_SHORT,false).show();

            });

        });

        categoriesRecyclerView.setAdapter(categoriesAdapter);
        languagesRecyclerView.setAdapter(languagesAdapter);

        instructorViewModel.getLanguages(instructor.getLanguagesAsString()).observe(this,languages->{

            List<String> list = new ArrayList<>();
            for (Language l : languages) {
                list.add(l.image);
            }

            languagesAdapter.setList(list);

            languagesShimmer.stopShimmerAnimation();
            languagesShimmer.setVisibility(View.GONE);
            languagesRecyclerView.setVisibility(View.VISIBLE);

        });

        instructorViewModel.getCategories(instructor.getCategoriesAsString()).observe(this,categories->{

            List<String> list = new ArrayList<>();
            for (Category c : categories) {
                list.add(c.image);
            }

            categoriesAdapter.setList(list);

            categoriesShimmer.stopShimmerAnimation();
            categoriesShimmer.setVisibility(View.GONE);
            categoriesRecyclerView.setVisibility(View.VISIBLE);

        });

        fb.setOnClickListener(v -> IntentUtil.openLink(InstructorDetailsActivity.this,instructor.facebook));

        linkedIn.setOnClickListener(v -> IntentUtil.openLink(InstructorDetailsActivity.this,instructor.linkedin));

        call.setOnClickListener(v -> IntentUtil.call(InstructorDetailsActivity.this,instructor.phone));

        email.setOnClickListener(v -> IntentUtil.sendEmail(InstructorDetailsActivity.this,instructor.email,"Contact From Codercampy App"));

        whatsapp.setOnClickListener(v -> {

            String s = instructor.whatsapp;
            if (s.contains("+")) {
                s = s.replace("+","");
            }

            IntentUtil.whatsApp(InstructorDetailsActivity.this,s);
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

}
