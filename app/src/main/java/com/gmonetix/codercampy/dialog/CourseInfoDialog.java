package com.gmonetix.codercampy.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.model.Category;
import com.gmonetix.codercampy.model.Language;
import com.gmonetix.codercampy.networking.APIClient;
import com.gmonetix.codercampy.networking.APIInterface;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gaurav Bordoloi on 3/31/2018.
 */
public class CourseInfoDialog extends AppCompatDialog {

    @BindView(R.id.course_image) ImageView courseImage;
    @BindView(R.id.course_name) TextView courseName;
    @BindView(R.id.course_category) TextView courseCategory;
    @BindView(R.id.course_language) TextView courseLanguage;
    @BindView(R.id.course_description) TextView courseDescription;

    private String image, name, description, categoryId;
    private List<String> languages;

    private APIInterface apiInterface;

    public CourseInfoDialog(Context context, String image, String courseName, String categoryId, List<String> languages, String description) {
        super(context, R.style.Theme_AppCompat_Dialog);
        this.image = image;
        this.name = courseName;
        this.categoryId = categoryId;
        this.description = description;
        this.languages = languages;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_course_info);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        apiInterface = APIClient.getClient().create(APIInterface.class);

        courseName.setText(name);
        courseDescription.setText(description);
        Glide.with(getContext()).load(image).into(courseImage);

        apiInterface.getCategory(categoryId).enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                if (response.body() != null) {
                    courseCategory.setText(response.body().name);
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

                courseLanguage.setText(s.toString());

            }

            @Override
            public void onFailure(Call<List<Language>> call, Throwable t) {
                Log.e("Error",t.getMessage());
            }
        });

    }

    @OnClick(R.id.close_dialog)
    void closeDialog() {
        dismiss();
    }

}
