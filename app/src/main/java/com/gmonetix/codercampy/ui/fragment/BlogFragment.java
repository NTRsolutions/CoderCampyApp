package com.gmonetix.codercampy.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.adapter.BlogAdapter;
import com.gmonetix.codercampy.model.Blog;
import com.gmonetix.codercampy.networking.APIClient;
import com.gmonetix.codercampy.networking.APIInterface;
import com.gmonetix.codercampy.ui.activity.BlogActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlogFragment extends Fragment {

    private View rootView;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    private APIInterface apiInterface;

    private List<Blog> blogList;
    private BlogAdapter adapter;

    public BlogFragment() { }

    public static BlogFragment newInstance() {
        return new BlogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_blog, container, false);
            ButterKnife.bind(this,rootView);

            apiInterface = APIClient.getClient().create(APIInterface.class);

            blogList = new ArrayList<>();
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setHasFixedSize(true);
            adapter = new BlogAdapter(getActivity());
            recyclerView.setAdapter(adapter);

            apiInterface.getAllBlogs().enqueue(new Callback<List<Blog>>() {
                @Override
                public void onResponse(Call<List<Blog>> call, Response<List<Blog>> response) {
                    blogList.addAll(response.body());
                    adapter.setList(blogList);
                }

                @Override
                public void onFailure(Call<List<Blog>> call, Throwable t) {
                    Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        return rootView;
    }

}
