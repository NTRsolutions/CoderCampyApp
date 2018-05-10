package com.gmonetix.codercampy.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;
import com.gmonetix.codercampy.model.Category;
import com.gmonetix.codercampy.model.Instructor;
import com.gmonetix.codercampy.model.Language;
import com.gmonetix.codercampy.networking.APIClient;
import com.gmonetix.codercampy.networking.APIInterface;
import com.gmonetix.codercampy.util.NetworkConnectionUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gaurav Bordoloi on 4/3/2018.
 */
public class CourseViewModel extends AndroidViewModel {

    private Application context;
    private APIInterface apiInterface;

    private MutableLiveData<Instructor> courseInstructor;
    private MutableLiveData<Category> category;
    private MutableLiveData<List<Language>> languages;

    public CourseViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    public LiveData<Instructor> getCourseInstructor(String instructorId) {
        if (courseInstructor == null) {
            courseInstructor = new MutableLiveData<>();
            loadCourseInstructor(instructorId);
        }
        return courseInstructor;
    }

    public LiveData<Category> getCategory(String categoryId) {
        if (category == null) {
            category = new MutableLiveData<>();
            loadCategory(categoryId);
        }
        return category;
    }

    public LiveData<List<Language>> getAllLanguages() {
        if (languages == null) {
            languages = new MutableLiveData<>();
            loadAllLanguages();
        }
        return languages;
    }

    private void loadAllLanguages() {

        if (NetworkConnectionUtil.isConnectedToInternet(context)) {

            apiInterface.getAllLanguages().enqueue(new Callback<List<Language>>() {
                @Override
                public void onResponse(Call<List<Language>> call, Response<List<Language>> response) {
                    if (response.body() != null) {

                        languages.setValue(response.body());

                    } else {
                        Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<List<Language>> call, Throwable t) {
                    Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(context, "No Network Connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void loadCourseInstructor(String instructorId) {

        if (NetworkConnectionUtil.isConnectedToInternet(context)) {

            apiInterface.getInstructor(instructorId).enqueue(new Callback<Instructor>() {
                @Override
                public void onResponse(Call<Instructor> call, Response<Instructor> response) {
                    courseInstructor.setValue(response.body());
                }

                @Override
                public void onFailure(Call<Instructor> call, Throwable t) {
                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(context, "No Network Connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void loadCategory(String categoryId) {

        if (NetworkConnectionUtil.isConnectedToInternet(context)) {

            apiInterface.getCategory(categoryId).enqueue(new Callback<Category>() {
                @Override
                public void onResponse(Call<Category> call, Response<Category> response) {
                    if (response.body() != null) {
                        category.setValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<Category> call, Throwable t) {
                    Log.e("Error",t.getMessage());
                }
            });

        } else {
            Toast.makeText(context, "No Network Connection", Toast.LENGTH_SHORT).show();
        }

    }

}
