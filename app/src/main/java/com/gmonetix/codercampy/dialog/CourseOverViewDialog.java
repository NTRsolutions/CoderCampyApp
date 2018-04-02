package com.gmonetix.codercampy.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.model.Instructor;
import com.gmonetix.codercampy.networking.APIClient;
import com.gmonetix.codercampy.networking.APIInterface;
import com.gmonetix.codercampy.util.NestedWebView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gaurav Bordoloi on 3/31/2018.
 */
public class CourseOverViewDialog extends AppCompatDialog {

    @BindView(R.id.blog_name) TextView blogName;
    @BindView(R.id.instructor_name) TextView instructorName;
    @BindView(R.id.webView) NestedWebView webView;

    private String name, overview, instructorId;

    private APIInterface apiInterface;

    public CourseOverViewDialog(Context context, String name, String overview, String instructorId) {
        super(context, R.style.Theme_AppCompat_Dialog);
        this.overview = overview;
        this.instructorId = instructorId;
        this.name = name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_course_overview);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        blogName.setText(name);
        String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/open_sans_regular.ttf\")}body {font-family: MyFont;font-size: medium;text-align: justify;}</style></head><body>";
        String pas = "</body></html>";
        String myHtmlString = pish + overview + pas;

        webView.loadDataWithBaseURL(null,myHtmlString, "text/html", "UTF-8", null);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        apiInterface.getInstructor(instructorId).enqueue(new Callback<Instructor>() {
            @Override
            public void onResponse(Call<Instructor> call, Response<Instructor> response) {
                instructorName.setText(response.body().name);
            }

            @Override
            public void onFailure(Call<Instructor> call, Throwable t) {
                Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @OnClick(R.id.close_dialog)
    void closeDialog() {
        dismiss();
    }

}
