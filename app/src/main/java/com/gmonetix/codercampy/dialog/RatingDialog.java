package com.gmonetix.codercampy.dialog;

import android.app.DialogFragment;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.gmonetix.codercampy.App;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.adapter.RatingAdapter;
import com.gmonetix.codercampy.listener.OnLoadMoreListener;
import com.gmonetix.codercampy.model.BlogRating;
import com.gmonetix.codercampy.model.CourseRating;
import com.gmonetix.codercampy.model.Rating;
import com.gmonetix.codercampy.networking.APIClient;
import com.gmonetix.codercampy.networking.APIInterface;
import com.gmonetix.codercampy.viewmodel.RatingViewModel;
import com.gmonetix.codercampy.viewmodel.UserViewModel;
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
public class RatingDialog extends DialogFragment {

    private View rootView;
    @BindView(R.id.rating_recyclerView) RecyclerView recyclerView;
    @BindView(R.id.course_rating_et) AppCompatEditText editText;
    @BindView(R.id.course_rating) AppCompatRatingBar ratingBar;
    @BindView(R.id.course_rating_send) ImageView addRating;

    private String id;
    private boolean isCourse;

    private APIInterface apiInterface;

    private UserViewModel userViewModel;
    private RatingViewModel ratingViewModel;

    private List<Rating> ratingList;
    private RatingAdapter adapter;

    private int offset = 0;
    private int limit = 10;
    private int page = 1;
    
    public RatingDialog() {}

    public static RatingDialog newInstance(String id, boolean isCourse) {
        RatingDialog f = new RatingDialog();
        Bundle bundle = new Bundle();
        bundle.putString("id",id);
        bundle.putBoolean("is_course",isCourse);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments().getString("id");
        isCourse = getArguments().getBoolean("is_course");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {

            rootView = inflater.inflate(R.layout.dialog_rating,container,false);
            ButterKnife.bind(this,rootView);

            apiInterface = APIClient.getClient().create(APIInterface.class);
            userViewModel = ViewModelProviders.of((FragmentActivity) getActivity()).get(UserViewModel.class);
            ratingViewModel = ViewModelProviders.of((FragmentActivity) getActivity()).get(RatingViewModel.class);

            ratingList = new ArrayList<>();
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
            adapter = new RatingAdapter(getActivity(),recyclerView);
            recyclerView.setAdapter(adapter);

            if (isCourse) {
                ratingViewModel.getCourseRating(id).observe((LifecycleOwner) getActivity(), discussion->{

                    ratingList.addAll(discussion.ratings);
                    adapter.setList(ratingList);

                });
            } else {
                ratingViewModel.getBlogRating(id).observe((LifecycleOwner) getActivity(), discussion->{

                    ratingList.addAll(discussion.ratings);
                    adapter.setList(ratingList);

                });
            }

            addRating.setOnClickListener(v -> {

                final int rating = (int) ratingBar.getRating();
                final String message = editText.getText().toString().trim();

                if (rating <= 0 || message.isEmpty()) {
                    Toasty.error(getActivity(),"Please fill the provided information",Toast.LENGTH_SHORT,true).show();
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
                                Toasty.success(getActivity(),"Rating submitted",Toast.LENGTH_SHORT,true).show();
                            } else {
                                Toasty.error(getActivity(),"Some error occurred",Toast.LENGTH_SHORT,true).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<com.gmonetix.codercampy.model.Response> call, Throwable t) {
                            Toasty.error(getActivity(),"Some error occurred",Toast.LENGTH_SHORT,true).show();
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
                                Toasty.success(getActivity(),"Rating submitted",Toast.LENGTH_SHORT,true).show();
                            } else {
                                Toasty.error(getActivity(),"Some error occurred",Toast.LENGTH_SHORT,true).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<com.gmonetix.codercampy.model.Response> call, Throwable t) {
                            Toasty.error(getActivity(),"Some error occurred",Toast.LENGTH_SHORT,true).show();
                        }
                    });

                }

            });

            adapter.setOnLoadMoreListener(() -> {

                if (ratingList.size() % 10 == 0) {

                    offset = offset + limit;
                    page++;

                    if (isCourse) {

                        apiInterface.getCourseRatingsById(id,offset,limit).enqueue(new Callback<CourseRating>() {
                            @Override
                            public void onResponse(Call<CourseRating> call, Response<CourseRating> response) {

                                if (response.body() != null) {

                                    ratingList.addAll(response.body().ratings);
                                    adapter.notifyDataSetChanged();
                                    adapter.setLoaded();

                                }

                            }

                            @Override
                            public void onFailure(Call<CourseRating> call, Throwable t) {
                                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {

                        apiInterface.getBlogRatingsById(id,offset,limit).enqueue(new Callback<BlogRating>() {
                            @Override
                            public void onResponse(Call<BlogRating> call, Response<BlogRating> response) {

                                if (response.body() != null) {

                                    ratingList.addAll(response.body().ratings);
                                    adapter.notifyDataSetChanged();
                                    adapter.setLoaded();

                                }

                            }

                            @Override
                            public void onFailure(Call<BlogRating> call, Throwable t) {
                                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                } else {
                    Toast.makeText(getActivity(), "Loading data completed", Toast.LENGTH_SHORT).show();
                }

            });
            
        }
        
        return rootView;
    }

    @OnClick(R.id.close_dialog)
    void closeDialog() {
        dismiss();
    }

}
