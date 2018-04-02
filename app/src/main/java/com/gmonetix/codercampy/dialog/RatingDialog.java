package com.gmonetix.codercampy.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.gmonetix.codercampy.App;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.adapter.RatingAdapter;
import com.gmonetix.codercampy.model.BlogRating;
import com.gmonetix.codercampy.model.CourseRating;
import com.gmonetix.codercampy.model.Rating;
import com.gmonetix.codercampy.networking.APIClient;
import com.gmonetix.codercampy.networking.APIInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gaurav Bordoloi on 3/31/2018.
 */
public class RatingDialog extends AppCompatDialog {

    @BindView(R.id.rating_recyclerView) RecyclerView recyclerView;
    @BindView(R.id.course_rating_et) AppCompatEditText editText;
    @BindView(R.id.course_rating) AppCompatRatingBar ratingBar;
    @BindView(R.id.course_rating_send) MaterialRippleLayout addRating;

    private String id;
    private boolean isCourse;

    private APIInterface apiInterface;

    private List<Rating> ratingList;
    private RatingAdapter adapter;

    public RatingDialog(Context context, String id, boolean isCourse) {
        super(context, R.style.Theme_AppCompat_Dialog);
        this.id = id;
        this.isCourse = isCourse;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_course_rating);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        apiInterface = APIClient.getClient().create(APIInterface.class);

        ratingList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        adapter = new RatingAdapter(getContext(),apiInterface);
        recyclerView.setAdapter(adapter);

        if (isCourse) {
            apiInterface.getCourseRatingsById(id).enqueue(new Callback<CourseRating>() {
                @Override
                public void onResponse(Call<CourseRating> call, Response<CourseRating> response) {
                    if (response.body().ratings != null) {
                        ratingList.addAll(response.body().ratings);
                        adapter.setList(ratingList);
                    }
                }

                @Override
                public void onFailure(Call<CourseRating> call, Throwable t) {
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            apiInterface.getBlogRatingsById(id).enqueue(new Callback<BlogRating>() {
                @Override
                public void onResponse(Call<BlogRating> call, Response<BlogRating> response) {
                    if (response.body().ratings != null) {
                        ratingList.addAll(response.body().ratings);
                        adapter.setList(ratingList);
                    }
                }

                @Override
                public void onFailure(Call<BlogRating> call, Throwable t) {
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }

        addRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final int rating = (int) ratingBar.getRating();
                final String message = editText.getText().toString().trim();

                if (rating <= 0 || message.isEmpty()) {
                    Toasty.error(getContext(),"Please fill the provided information",Toast.LENGTH_SHORT,true).show();
                    return;
                }

                if (isCourse) {

                    apiInterface.addCourseRating(id, App.getAuth().getCurrentUser().getUid(),rating,message).enqueue(new Callback<com.gmonetix.codercampy.model.Response>() {
                        @Override
                        public void onResponse(Call<com.gmonetix.codercampy.model.Response> call, Response<com.gmonetix.codercampy.model.Response> response) {
                            if (response.body().code.equals("success")) {
                                Rating r = new Rating(App.getAuth().getCurrentUser().getUid(),rating,message);
                                ratingList.add(r);
                                adapter.notifyDataSetChanged();

                                editText.setText("");
                                Toasty.success(getContext(),"Rating submitted",Toast.LENGTH_SHORT,true).show();
                            } else {
                                Toasty.error(getContext(),"Some error occurred",Toast.LENGTH_SHORT,true).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<com.gmonetix.codercampy.model.Response> call, Throwable t) {
                            Toasty.error(getContext(),"Some error occurred",Toast.LENGTH_SHORT,true).show();
                        }
                    });

                } else {

                    apiInterface.addBlogRating(id, App.getAuth().getCurrentUser().getUid(),rating,message).enqueue(new Callback<com.gmonetix.codercampy.model.Response>() {
                        @Override
                        public void onResponse(Call<com.gmonetix.codercampy.model.Response> call, Response<com.gmonetix.codercampy.model.Response> response) {
                            if (response.body().code.equals("success")) {
                                Rating r = new Rating(App.getAuth().getCurrentUser().getUid(),rating,message);
                                ratingList.add(r);
                                adapter.notifyDataSetChanged();

                                editText.setText("");
                                Toasty.success(getContext(),"Rating submitted",Toast.LENGTH_SHORT,true).show();
                            } else {
                                Toasty.error(getContext(),"Some error occurred",Toast.LENGTH_SHORT,true).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<com.gmonetix.codercampy.model.Response> call, Throwable t) {
                            Toasty.error(getContext(),"Some error occurred",Toast.LENGTH_SHORT,true).show();
                        }
                    });

                }

            }
        });

    }

    @OnClick(R.id.close_dialog)
    void closeDialog() {
        dismiss();
    }

}
