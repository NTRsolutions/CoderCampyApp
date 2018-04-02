package com.gmonetix.codercampy.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.adapter.BlogHorizontalAdapter;
import com.gmonetix.codercampy.adapter.CourseHorizontalAdapter;
import com.gmonetix.codercampy.model.Blog;
import com.gmonetix.codercampy.model.Course;
import com.gmonetix.codercampy.model.Dummy;
import com.gmonetix.codercampy.model.HomeData;
import com.gmonetix.codercampy.networking.APIClient;
import com.gmonetix.codercampy.networking.APIInterface;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.views.BannerSlider;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private View rootView;
    @BindView(R.id.banner_slider) BannerSlider bannerSlider;
    @BindView(R.id.trending_courses_recyclerView) RecyclerView trendingCoursesRecyclerView;
    @BindView(R.id.popular_courses_recyclerView) RecyclerView popularCoursesRecyclerView;
    @BindView(R.id.trending_posts_recyclerView) RecyclerView trendingPostsRecyclerView;
    @BindView(R.id.popular_posts_recyclerView) RecyclerView popularPostsRecyclerView;

    @BindView(R.id.trending_courses_shimmer) ShimmerFrameLayout trending_courses_shimmer;
    @BindView(R.id.popular_courses_shimmer) ShimmerFrameLayout popular_courses_shimmer;
    @BindView(R.id.trending_posts_shimmer) ShimmerFrameLayout trending_posts_shimmer;
    @BindView(R.id.popular_posts_shimmer) ShimmerFrameLayout popular_posts_shimmer;

    private List<Course> courseList;
    private List<Blog> blogList;

    private CourseHorizontalAdapter trendingCoursesAdapter, popularCoursesAdapter;
    private BlogHorizontalAdapter trendingPostsAdapter, popularPostsAdapter;

    private APIInterface apiInterface;

    public HomeFragment() { }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_home, container, false);
            ButterKnife.bind(this,rootView);

            trending_courses_shimmer.startShimmerAnimation();
            popular_courses_shimmer.startShimmerAnimation();
            trending_posts_shimmer.startShimmerAnimation();
            popular_posts_shimmer.startShimmerAnimation();

            courseList = new ArrayList<>();
            blogList = new ArrayList<>();
            trendingCoursesAdapter = new CourseHorizontalAdapter(getActivity());
            popularCoursesAdapter = new CourseHorizontalAdapter(getActivity());
            trendingPostsAdapter = new BlogHorizontalAdapter(getActivity());
            popularPostsAdapter = new BlogHorizontalAdapter(getActivity());

            trendingCoursesRecyclerView.setHasFixedSize(true);
            trendingCoursesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
            trendingCoursesRecyclerView.setAdapter(trendingCoursesAdapter);

            popularCoursesRecyclerView.setHasFixedSize(true);
            popularCoursesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
            popularCoursesRecyclerView.setAdapter(popularCoursesAdapter);

            trendingPostsRecyclerView.setHasFixedSize(true);
            trendingPostsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
            trendingPostsRecyclerView.setAdapter(trendingPostsAdapter);

            popularPostsRecyclerView.setHasFixedSize(true);
            popularPostsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
            popularPostsRecyclerView.setAdapter(popularPostsAdapter);


            apiInterface = APIClient.getClient().create(APIInterface.class);

            apiInterface.getAllCourses().enqueue(new Callback<List<Course>>() {
                @Override
                public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                    if (response.body() != null) {
                        courseList.addAll(response.body());
                        trendingCoursesAdapter.setList(courseList);
                        popularCoursesAdapter.setList(courseList);

                        trending_courses_shimmer.stopShimmerAnimation();
                        popular_courses_shimmer.stopShimmerAnimation();
                        trending_courses_shimmer.setVisibility(View.GONE);
                        popular_courses_shimmer.setVisibility(View.GONE);

                        trendingCoursesRecyclerView.setVisibility(View.VISIBLE);
                        popularCoursesRecyclerView.setVisibility(View.VISIBLE);
                    } else {
                        //TODO
                    }
                }

                @Override
                public void onFailure(Call<List<Course>> call, Throwable t) {
                    Log.e("Error",t.getMessage());
                }
            });

            apiInterface.getAllBlogs().enqueue(new Callback<List<Blog>>() {
                @Override
                public void onResponse(Call<List<Blog>> call, Response<List<Blog>> response) {
                    blogList.addAll(response.body());
                    trendingPostsAdapter.setList(blogList);
                    popularPostsAdapter.setList(blogList);

                    trending_posts_shimmer.stopShimmerAnimation();
                    popular_posts_shimmer.stopShimmerAnimation();
                    trending_posts_shimmer.setVisibility(View.GONE);
                    popular_posts_shimmer.setVisibility(View.GONE);

                    trendingPostsRecyclerView.setVisibility(View.VISIBLE);
                    popularPostsRecyclerView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(Call<List<Blog>> call, Throwable t) {
                    Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            apiInterface.getHomeData().enqueue(new Callback<HomeData>() {
                @Override
                public void onResponse(Call<HomeData> call, Response<HomeData> response) {

                    if (response.body() != null) {

                        HomeData homeData = response.body();

                        List<Banner> banners = new ArrayList<>();
                        for (com.gmonetix.codercampy.model.Banner b: homeData.banners) {
                            banners.add(new RemoteBanner(b.image));
                        }
                        bannerSlider.setBanners(banners);

                        Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onFailure(Call<HomeData> call, Throwable t) {
                    Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onFavouriteChanged(Dummy dummy) {

    }

}
