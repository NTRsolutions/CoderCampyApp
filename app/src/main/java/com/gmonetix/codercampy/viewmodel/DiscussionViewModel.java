package com.gmonetix.codercampy.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.widget.Toast;
import com.gmonetix.codercampy.model.BlogDiscussion;
import com.gmonetix.codercampy.model.CourseDiscussion;
import com.gmonetix.codercampy.networking.APIClient;
import com.gmonetix.codercampy.networking.APIInterface;
import com.gmonetix.codercampy.util.NetworkConnectionUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gaurav Bordoloi on 4/2/2018.
 */
public class DiscussionViewModel extends AndroidViewModel {

    private Application context;
    private APIInterface apiInterface;

    private MutableLiveData<CourseDiscussion> courseDiscussion;
    private MutableLiveData<BlogDiscussion> blogDiscussion;

    public DiscussionViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    public LiveData<CourseDiscussion> getCourseDiscussion(String courseId) {
        if (courseDiscussion == null) {
            courseDiscussion = new MutableLiveData<>();
            loadCourseDiscussion(courseId);
        }
        return courseDiscussion;
    }

    public LiveData<BlogDiscussion> getBlogDiscussion(String blogId) {
        if (blogDiscussion == null) {
            blogDiscussion = new MutableLiveData<>();
            loadBlogDiscussion(blogId);
        }
        return blogDiscussion;
    }

    private void loadCourseDiscussion(String courseId) {

        /*if (NetworkConnectionUtil.isConnectedToInternet(context)) {
            apiInterface.getCourseDiscussionsById(courseId).enqueue(new Callback<CourseDiscussion>() {
                @Override
                public void onResponse(Call<CourseDiscussion> call, Response<CourseDiscussion> response) {
                    if (response.body() != null) {
                        courseDiscussion.setValue(response.body());
                    } else {
                        Toast.makeText(context, "No Discussion Data", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CourseDiscussion> call, Throwable t) {
                    Toast.makeText(context, "Not Logged In", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, "No Network Connection", Toast.LENGTH_SHORT).show();
        }*/

    }

    private void loadBlogDiscussion(String blogId) {

        /*if (NetworkConnectionUtil.isConnectedToInternet(context)) {
            apiInterface.getBlogDiscussionsById(blogId).enqueue(new Callback<BlogDiscussion>() {
                @Override
                public void onResponse(Call<BlogDiscussion> call, Response<BlogDiscussion> response) {
                    if (response.body() != null) {
                        blogDiscussion.setValue(response.body());
                    } else {
                        Toast.makeText(context, "No Discussion Data", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<BlogDiscussion> call, Throwable t) {
                    Toast.makeText(context, "Not Logged In", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, "No Network Connection", Toast.LENGTH_SHORT).show();
        }*/

    }

}
