package com.gmonetix.codercampy.networking;

import com.gmonetix.codercampy.model.Banner;
import com.gmonetix.codercampy.model.Blog;
import com.gmonetix.codercampy.model.BlogDiscussion;
import com.gmonetix.codercampy.model.Category;
import com.gmonetix.codercampy.model.Course;
import com.gmonetix.codercampy.model.CourseDiscussion;
import com.gmonetix.codercampy.model.Instructor;
import com.gmonetix.codercampy.model.Language;
import com.gmonetix.codercampy.model.Response;
import com.gmonetix.codercampy.model.User;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Gaurav Bordoloi on 2/28/2018.
 */

public interface APIInterface {

    @POST("/add-user")
    Call<Response> createUser(@Body User user);

    @GET("/user-uid/{uid}")
    Call<User> getUser(@Path("uid") String uid);

    @GET("/instructors")
    Call<List<Instructor>> getAllInstructors();

    @GET("/banners")
    Call<List<Banner>> getAllBanners();

    @GET("/instructor/{id}")
    Call<Instructor> getInstructor(@Path("id") String id);

    @GET("/courses")
    Call<List<Course>> getAllCourses();

    @GET("/courses/{ids}")
    Call<List<Course>> getCourses(@Path("ids") String ids);

    @GET("/course/{id}")
    Call<Course> getCourse(@Path("id") String id);

    @GET("/languages")
    Call<List<Language>> getAllLanguages();

    @GET("/language/{id}")
    Call<Language> getLanguage(@Path("id") String id);

    @GET("/languages/{ids}")
    Call<List<Language>> getLanguages(@Path("ids") String ids);

    @GET("/categories")
    Call<List<Category>> getAllCategories();

    @GET("/category/{id}")
    Call<Category> getCategory(@Path("id") String id);

    @GET("/categories/{ids}")
    Call<List<Category>> getCategories(@Path("ids") String ids);

    @GET("/blogs")
    Call<List<Blog>> getAllBlogs();

    @GET("/blog/{id}")
    Call<Blog> getBlog(@Path("id") String id);

    @GET("/discussions/course/{id}")
    Call<CourseDiscussion> getCourseDiscussionsById(@Path("id") String id);

    @GET("/discussions/blog/{id}")
    Call<BlogDiscussion> getBlogDiscussionsById(@Path("id") String id);

    /*
    @GET("/api/users?")
    Call<UserList> doGetUserList(@Query("page") String page);

    @FormUrlEncoded
    @POST("/api/users?")
    Call<UserList> doCreateUserWithField(@Field("name") String name, @Field("job") String job);*/

    /*@FormUrlEncoded
    @POST("user/edit")
    Call<User> updateUser (@Field("uid") String first, @Field("last_name") String last);*/

}
