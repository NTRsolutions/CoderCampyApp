package com.gmonetix.codercampy.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.model.Banner;
import com.gmonetix.codercampy.networking.APIClient;
import com.gmonetix.codercampy.networking.APIInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.views.BannerSlider;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private View rootView;
    @BindView(R.id.banner_slider) BannerSlider bannerSlider;

    private APIInterface apiInterface;

    public HomeFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_home, container, false);
            ButterKnife.bind(this,rootView);

            apiInterface = APIClient.getClient().create(APIInterface.class);

            apiInterface.getAllBanners().enqueue(new Callback<List<Banner>>() {
                @Override
                public void onResponse(Call<List<Banner>> call, Response<List<Banner>> response) {

                    List<ss.com.bannerslider.banners.Banner> banners= new ArrayList<>();

                    for (Banner banner : response.body()) {
                        banners.add(new RemoteBanner(banner.image));
                    }
                    bannerSlider.setBanners(banners);

                }

                @Override
                public void onFailure(Call<List<Banner>> call, Throwable t) {

                }
            });

        }
        return rootView;
    }

}
