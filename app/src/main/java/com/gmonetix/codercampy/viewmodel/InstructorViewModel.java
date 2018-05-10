package com.gmonetix.codercampy.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.widget.Toast;
import com.gmonetix.codercampy.model.Category;
import com.gmonetix.codercampy.model.Course;
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
public class InstructorViewModel extends AndroidViewModel{

    private Application context;
    private APIInterface apiInterface;

    private MutableLiveData<List<Language>> languageList;
    private MutableLiveData<List<Category>> categoryList;
    private MutableLiveData<List<Course>> courseList;

    public InstructorViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    public LiveData<List<Course>> getCourses(String ids) {
        if (courseList == null) {
            courseList = new MutableLiveData<>();
            loadCourses(ids);
        }
        return courseList;
    }

    public LiveData<List<Category>> getCategories(String ids) {
        if (categoryList == null) {
            categoryList = new MutableLiveData<>();
            loadCategories(ids);
        }
        return categoryList;
    }

    public LiveData<List<Language>> getLanguages(String ids) {
        if (languageList == null) {
            languageList = new MutableLiveData<>();
            loadLanguages(ids);
        }
        return languageList;
    }

    private void loadLanguages(String ids) {

        if (NetworkConnectionUtil.isConnectedToInternet(context)) {

            apiInterface.getLanguages(ids).enqueue(new Callback<List<Language>>() {
                @Override
                public void onResponse(Call<List<Language>> call, Response<List<Language>> response) {
                    if (response.body() != null) {

                        languageList.setValue(response.body());

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

    private void loadCategories(String ids) {

        if (NetworkConnectionUtil.isConnectedToInternet(context)) {

            apiInterface.getCategories(ids).enqueue(new Callback<List<Category>>() {
                @Override
                public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                    if (response.body() != null) {

                        categoryList.setValue(response.body());

                    } else {
                        Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Category>> call, Throwable t) {
                    Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(context, "No Network Connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void loadCourses(String ids) {

        if (NetworkConnectionUtil.isConnectedToInternet(context)) {

            apiInterface.getCoursesByInstructorId(ids).enqueue(new Callback<List<Course>>() {
                @Override
                public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {

                    if (response.body() != null) {

                        courseList.setValue(response.body());

                    } else {
                        Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Course>> call, Throwable t) {
                    Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(context, "No Network Connection", Toast.LENGTH_SHORT).show();
        }

    }

}
