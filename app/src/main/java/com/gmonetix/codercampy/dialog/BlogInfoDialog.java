package com.gmonetix.codercampy.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.model.Category;
import com.gmonetix.codercampy.model.Instructor;
import com.gmonetix.codercampy.model.Language;
import com.gmonetix.codercampy.networking.APIClient;
import com.gmonetix.codercampy.networking.APIInterface;
import com.gmonetix.codercampy.util.GetTimeAgo;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gaurav Bordoloi on 4/1/2018.
 */
public class BlogInfoDialog extends AppCompatDialog {

    @BindView(R.id.blog_name) TextView blogName;
    @BindView(R.id.blog_category) TextView blogCategory;
    @BindView(R.id.blog_language) TextView blogLanguage;
    @BindView(R.id.blog_instructor) TextView blogInstructor;
    @BindView(R.id.blog_time) TextView blogTime;

    private String name, instructorId,time, categoryId;
    private List<String> languages;

    private APIInterface apiInterface;

    public BlogInfoDialog(Context context, String name, String instructorId, String time, String categoryId, List<String> languages) {
        super(context, R.style.Theme_AppCompat_Dialog);
        this.name = name;
        this.instructorId = instructorId;
        this.time = time;
        this.categoryId = categoryId;
        this.languages = languages;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_blog_info);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        blogName.setText(name);
        blogTime.setText(GetTimeAgo.getTimeAgo(Long.parseLong(time),getContext()));

        apiInterface = APIClient.getClient().create(APIInterface.class);

        apiInterface.getCategory(categoryId).enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                if (response.body() != null) {
                    blogCategory.setText(response.body().name);
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                Log.e("Error",t.getMessage());
            }
        });

        apiInterface.getAllLanguages().enqueue(new Callback<List<Language>>() {
            @Override
            public void onResponse(Call<List<Language>> call, Response<List<Language>> response) {

                StringBuilder s = new StringBuilder();

                for (Language language : response.body()) {

                    if (languages.contains(language.id)) {
                        if (!s.toString().isEmpty())
                            s.append(" , ");
                        s.append(language.name);
                    }

                }

                blogLanguage.setText(s.toString());

            }

            @Override
            public void onFailure(Call<List<Language>> call, Throwable t) {
                Log.e("Error",t.getMessage());
            }
        });


        apiInterface.getInstructor(instructorId).enqueue(new Callback<Instructor>() {
            @Override
            public void onResponse(Call<Instructor> call, Response<Instructor> response) {
                blogInstructor.setText(response.body().name);
            }

            @Override
            public void onFailure(Call<Instructor> call, Throwable t) {
                Log.e("Error",t.getMessage());
            }
        });

    }

    @OnClick(R.id.close_dialog)
    void closeDialog() {
        dismiss();
    }

}
