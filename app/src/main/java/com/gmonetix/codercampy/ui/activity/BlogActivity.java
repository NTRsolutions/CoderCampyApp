package com.gmonetix.codercampy.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.adapter.BlogAdapter;
import com.gmonetix.codercampy.model.Blog;
import com.gmonetix.codercampy.model.Category;
import com.gmonetix.codercampy.networking.APIClient;
import com.gmonetix.codercampy.networking.APIInterface;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlogActivity extends AppCompatActivity {

    @BindView(R.id.category_spinner) TextView tvCategorySpinner;
    @BindView(R.id.blog_recyclerView) RecyclerView recyclerView;

    private APIInterface apiInterface;

    private List<String> categoryList;
    private int index = 0;

    private List<Blog> blogs;
    private BlogAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
        ButterKnife.bind(this);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        categoryList = new ArrayList<>();
        categoryList.add("All");

        blogs = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BlogAdapter(this);
        recyclerView.setAdapter(adapter);

        Call<List<Category>> c1 = apiInterface.getAllCategories();
        c1.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                for (Category c : response.body()) {
                    categoryList.add(c.name);
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });

        Call<List<Blog>> blogCall = apiInterface.getAllBlogs();
        blogCall.enqueue(new Callback<List<Blog>>() {
            @Override
            public void onResponse(Call<List<Blog>> call, Response<List<Blog>> response) {
                adapter.setList(response.body());
            }

            @Override
            public void onFailure(Call<List<Blog>> call, Throwable t) {
                Toast.makeText(BlogActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        tvCategorySpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new MaterialDialog.Builder(BlogActivity.this)
                        .title("Select Category")
                        .items(categoryList)
                        .alwaysCallSingleChoiceCallback()
                        .itemsCallbackSingleChoice(index, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                tvCategorySpinner.setText(text);
                                index = which;
                                return true;
                            }
                        })
                        .show();

            }
        });

    }

}
