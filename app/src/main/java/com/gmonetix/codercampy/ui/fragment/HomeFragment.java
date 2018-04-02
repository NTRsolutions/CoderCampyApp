package com.gmonetix.codercampy.ui.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.adapter.BlogHorizontalAdapter;
import com.gmonetix.codercampy.adapter.CourseHorizontalAdapter;
import com.gmonetix.codercampy.model.Blog;
import com.gmonetix.codercampy.model.Course;
import com.gmonetix.codercampy.model.Dummy;
import com.gmonetix.codercampy.util.IntentUtil;
import com.gmonetix.codercampy.viewmodel.HomeViewModel;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.events.OnBannerClickListener;
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

    private HomeViewModel homeViewModel;

    public HomeFragment() { }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

            homeViewModel = ViewModelProviders.of(getActivity()).get(HomeViewModel.class);

            homeViewModel.getHomeData().observe(this,homeData->{

                List<Banner> banners = new ArrayList<>();
                for (com.gmonetix.codercampy.model.Banner b: homeData.banners) {
                    banners.add(new RemoteBanner(b.image));
                }
                bannerSlider.setBanners(banners);

                bannerSlider.setOnBannerClickListener(new OnBannerClickListener() {
                    @Override
                    public void onClick(int position) {
                        IntentUtil.openCustomChromeTab(getActivity(), Uri.parse(homeData.banners.get(position).link));
                    }
                });

            });

            homeViewModel.getAllCourses().observe(this,allCourseList->{

                courseList.clear();
                courseList.addAll(allCourseList);
                trendingCoursesAdapter.setList(courseList);
                popularCoursesAdapter.setList(courseList);

                trending_courses_shimmer.stopShimmerAnimation();
                popular_courses_shimmer.stopShimmerAnimation();
                trending_courses_shimmer.setVisibility(View.GONE);
                popular_courses_shimmer.setVisibility(View.GONE);

                trendingCoursesRecyclerView.setVisibility(View.VISIBLE);
                popularCoursesRecyclerView.setVisibility(View.VISIBLE);

            });

            homeViewModel.getAllBlogs().observe(this,allBlogList -> {

                blogList.addAll(allBlogList);
                trendingPostsAdapter.setList(blogList);
                popularPostsAdapter.setList(blogList);

                trending_posts_shimmer.stopShimmerAnimation();
                popular_posts_shimmer.stopShimmerAnimation();
                trending_posts_shimmer.setVisibility(View.GONE);
                popular_posts_shimmer.setVisibility(View.GONE);

                trendingPostsRecyclerView.setVisibility(View.VISIBLE);
                popularPostsRecyclerView.setVisibility(View.VISIBLE);

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
