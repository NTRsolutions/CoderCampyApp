package com.gmonetix.codercampy.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.widget.Toast;
import com.gmonetix.codercampy.model.BlogRating;
import com.gmonetix.codercampy.model.CourseRating;
import com.gmonetix.codercampy.networking.APIClient;
import com.gmonetix.codercampy.networking.APIInterface;
import com.gmonetix.codercampy.util.NetworkConnectionUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gaurav Bordoloi on 4/3/2018.
 */
public class RatingViewModel extends AndroidViewModel{

    private Application context;
    private APIInterface apiInterface;

    private MutableLiveData<CourseRating> courseRating;
    private MutableLiveData<BlogRating> blogRating;
    
    public RatingViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    public LiveData<CourseRating> getCourseRating(String courseId) {
        if (courseRating == null) {
            courseRating = new MutableLiveData<>();
            loadCourseRating(courseId);
        }
        return courseRating;
    }

    public LiveData<BlogRating> getBlogRating(String blogId) {
        if (blogRating == null) {
            blogRating = new MutableLiveData<>();
            loadBlogRating(blogId);
        }
        return blogRating;
    }

    private void loadCourseRating(String courseId) {
/*
        if (NetworkConnectionUtil.isConnectedToInternet(context)) {
            apiInterface.getCourseRatingsById(courseId).enqueue(new Callback<CourseRating>() {
                @Override
                public void onResponse(Call<CourseRating> call, Response<CourseRating> response) {
                    if (response.body() != null) {
                        courseRating.setValue(response.body());
                    } else {
                        Toast.makeText(context, "No Rating Data", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CourseRating> call, Throwable t) {
                    Toast.makeText(context, "Not Logged In", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, "No Network Connection", Toast.LENGTH_SHORT).show();
        }*/

    }

    private void loadBlogRating(String blogId) {

        /*if (NetworkConnectionUtil.isConnectedToInternet(context)) {
            apiInterface.getBlogRatingsById(blogId).enqueue(new Callback<BlogRating>() {
                @Override
                public void onResponse(Call<BlogRating> call, Response<BlogRating> response) {
                    if (response.body() != null) {
                        blogRating.setValue(response.body());
                    } else {
                        Toast.makeText(context, "No Rating Data", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<BlogRating> call, Throwable t) {
                    Toast.makeText(context, "Not Logged In", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, "No Network Connection", Toast.LENGTH_SHORT).show();
        }*/

    }
    
}
