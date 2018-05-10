package com.gmonetix.codercampy.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.widget.Toast;
import com.gmonetix.codercampy.App;
import com.gmonetix.codercampy.model.Blog;
import com.gmonetix.codercampy.model.Category;
import com.gmonetix.codercampy.model.Course;
import com.gmonetix.codercampy.model.HomeData;
import com.gmonetix.codercampy.model.Instructor;
import com.gmonetix.codercampy.model.Language;
import com.gmonetix.codercampy.model.User;
import com.gmonetix.codercampy.networking.APIClient;
import com.gmonetix.codercampy.networking.APIInterface;
import com.gmonetix.codercampy.util.NetworkConnectionUtil;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gaurav Bordoloi on 4/2/2018.
 */

public class HomeViewModel extends AndroidViewModel {

    private Application context;
    private APIInterface apiInterface;

    private MutableLiveData<HomeData> homeData;
    private MutableLiveData<List<Course>> allCourseList;
    private MutableLiveData<List<Blog>> allBlogList;
    private MutableLiveData<List<Language>> languageList;
    private MutableLiveData<List<Category>> categoryList;
    private MutableLiveData<List<Instructor>> instructorList;
    private MutableLiveData<User> user;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    public LiveData<User> getUser() {
        if (user == null) {
            user = new MutableLiveData<>();
            loadUserData();
        }
        return user;
    }

    public LiveData<HomeData> getHomeData() {
        if (homeData == null) {
            homeData = new MutableLiveData<>();
            loadHomeData();
        }
        return homeData;
    }

   /* public LiveData<List<Course>> getAllCourses() {
        if (allCourseList == null) {
            allCourseList = new MutableLiveData<>();
            loadAllCourses();
        }
        return allCourseList;
    }*/

    public LiveData<List<Blog>> getAllBlogs() {
        if (allBlogList == null) {
            allBlogList = new MutableLiveData<>();
            loadAllBlogs();
        }
        return allBlogList;
    }

    public LiveData<List<Category>> getAllCategories() {
        if (categoryList == null) {
            categoryList = new MutableLiveData<>();
            loadAllCategories();
        }
        return categoryList;
    }

    public LiveData<List<Language>> getAllLanguages() {
        if (languageList == null) {
            languageList = new MutableLiveData<>();
            loadAllLanguages();
        }
        return languageList;
    }

    public LiveData<List<Instructor>> getAllInstructors() {
        if (instructorList == null) {
            instructorList = new MutableLiveData<>();
            loadAllInstructors();
        }
        return instructorList;
    }

    private void loadAllInstructors() {

        if (NetworkConnectionUtil.isConnectedToInternet(context)) {

            apiInterface.getAllInstructors().enqueue(new Callback<List<Instructor>>() {
                @Override
                public void onResponse(Call<List<Instructor>> call, Response<List<Instructor>> response) {
                    if (response.body() != null) {
                        instructorList.setValue(response.body());
                    } else {
                        Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Instructor>> call, Throwable t) {
                    Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(context, "No Network Connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void loadUserData() {

        if (NetworkConnectionUtil.isConnectedToInternet(context)) {

            if (App.getAuth().getCurrentUser() != null) {
                apiInterface.getUser(App.getAuth().getCurrentUser().getUid()).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.body() != null) {
                            user.setValue(response.body());
                        } else {
                            Toast.makeText(context, "No User Data", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(context, "Not Logged In", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(context, "No Network Connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void loadAllLanguages() {

        if (NetworkConnectionUtil.isConnectedToInternet(context)) {

            apiInterface.getAllLanguages().enqueue(new Callback<List<Language>>() {
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

    private void loadAllCategories() {

        if (NetworkConnectionUtil.isConnectedToInternet(context)) {

            apiInterface.getAllCategories().enqueue(new Callback<List<Category>>() {
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

    private void loadAllBlogs() {

        if (NetworkConnectionUtil.isConnectedToInternet(context)) {

            apiInterface.getAllBlogs().enqueue(new Callback<List<Blog>>() {
                @Override
                public void onResponse(Call<List<Blog>> call, Response<List<Blog>> response) {

                    if (response.body() != null) {

                        allBlogList.setValue(response.body());

                    } else {
                        Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<List<Blog>> call, Throwable t) {
                    Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(context, "No Network Connection", Toast.LENGTH_SHORT).show();
        }

    }
/*
    private void loadAllCourses() {

        if (NetworkConnectionUtil.isConnectedToInternet(context)) {

            apiInterface.getAllCourses().enqueue(new Callback<List<Course>>() {
                @Override
                public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {

                    if (response.body() != null) {

                        allCourseList.setValue(response.body());

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

    }*/

    private void loadHomeData() {

        if (NetworkConnectionUtil.isConnectedToInternet(context)) {

            apiInterface.getHomeData().enqueue(new Callback<HomeData>() {
                @Override
                public void onResponse(Call<HomeData> call, Response<HomeData> response) {

                    if (response.body() != null) {

                        homeData.setValue(response.body());

                    } else {
                        Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<HomeData> call, Throwable t) {
                    Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(context, "No Network Connection", Toast.LENGTH_SHORT).show();
        }

    }

}
