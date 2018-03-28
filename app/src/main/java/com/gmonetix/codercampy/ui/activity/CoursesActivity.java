package com.gmonetix.codercampy.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.balysv.materialripple.MaterialRippleLayout;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.adapter.CourseAdapter;
import com.gmonetix.codercampy.model.Category;
import com.gmonetix.codercampy.model.Course;
import com.gmonetix.codercampy.model.Language;
import com.gmonetix.codercampy.networking.APIClient;
import com.gmonetix.codercampy.networking.APIInterface;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoursesActivity extends AppCompatActivity {

    @BindView(R.id.back) MaterialRippleLayout back;
    @BindView(R.id.search_clear) MaterialRippleLayout searchClear;
    @BindView(R.id.search_et) AppCompatEditText searchBar;
    @BindView(R.id.category_spinner) TextView categorySpinner;
    @BindView(R.id.language_spinner) TextView languageSpinner;
    @BindView(R.id.courses_recyclerview) RecyclerView recyclerView;

    private List<Course> courseList;

    private List<String> categoryList;
    private List<String> languageList;
    private Integer[] selectedIndices;
    private int index = 0;

    private CourseAdapter adapter;

    private APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        ButterKnife.bind(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        searchClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBar.setText("");
            }
        });

        apiInterface = APIClient.getClient().create(APIInterface.class);

        courseList = new ArrayList<>();
        categoryList = new ArrayList<>();
        categoryList.add("All");
        languageList = new ArrayList<>();

        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setHasFixedSize(true);
        adapter = new CourseAdapter(this);
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

        apiInterface.getAllLanguages().enqueue(new Callback<List<Language>>() {
            @Override
            public void onResponse(Call<List<Language>> call, Response<List<Language>> response) {
                for (Language l : response.body()) {
                    languageList.add(l.name);
                }
            }

            @Override
            public void onFailure(Call<List<Language>> call, Throwable t) {

            }
        });

        languageSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(CoursesActivity.this)
                        .title("Select Languages")
                        .items(languageList)
                        .itemsCallbackMultiChoice(selectedIndices, new MaterialDialog.ListCallbackMultiChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                selectedIndices = which;

                                String s = "";
                                for (CharSequence cs : text) {
                                    s = s + " , " + cs;
                                }
                                languageSpinner.setText(s);

                                return true;
                            }
                        })
                        .positiveText("CHOOSE")
                        .show();
            }
        });

        categorySpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new MaterialDialog.Builder(CoursesActivity.this)
                        .title("Select Category")
                        .items(categoryList)
                        .alwaysCallSingleChoiceCallback()
                        .itemsCallbackSingleChoice(index, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                categorySpinner.setText(text);
                                index = which;
                                return true;
                            }
                        })
                        .show();

            }
        });


        Call<List<Course>> c2 = apiInterface.getAllCourses();
        c2.enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                courseList.addAll(response.body());

                adapter.setList(courseList);
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                Log.e("Error",t.getMessage());
            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("TAG",s.toString());
                adapter.getFilter().filter(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty())
                    searchClear.setVisibility(View.GONE);
                else searchClear.setVisibility(View.VISIBLE);
            }

        });

    }
}
