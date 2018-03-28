package com.gmonetix.codercampy.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.adapter.DiscussionAdapter;
import com.gmonetix.codercampy.model.BlogDiscussion;
import com.gmonetix.codercampy.networking.APIClient;
import com.gmonetix.codercampy.networking.APIInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiscussionFragment extends Fragment {

    private View rootView;
    @BindView(R.id.discussion_recyclerView) RecyclerView recyclerView;
    @BindView(R.id.discussion_et) EditText discussionET;
    @BindView(R.id.send) MaterialRippleLayout sendBtn;

    private static final String ARG_PARAM1 = "courseId";
    private String courseId;

    private DiscussionAdapter adapter;

    public DiscussionFragment() { }

    public static DiscussionFragment newInstance(String course_id) {
        DiscussionFragment fragment = new DiscussionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, course_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            courseId = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_discussion, container, false);
            ButterKnife.bind(this,rootView);

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter = new DiscussionAdapter(getActivity());
            recyclerView.setAdapter(adapter);

            APIClient.getClient().create(APIInterface.class).getBlogDiscussionsById("5a9e44016cb538393c3b972e").enqueue(new Callback<BlogDiscussion>() {
                @Override
                public void onResponse(Call<BlogDiscussion> call, Response<BlogDiscussion> response) {
                    adapter.setList(response.body().discussions);
                }

                @Override
                public void onFailure(Call<BlogDiscussion> call, Throwable t) {
                    Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }

        return rootView;
    }

}
