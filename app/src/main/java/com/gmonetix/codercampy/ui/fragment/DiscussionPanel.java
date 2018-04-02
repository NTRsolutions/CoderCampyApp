package com.gmonetix.codercampy.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.gmonetix.codercampy.App;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.adapter.DiscussionAdapter;
import com.gmonetix.codercampy.model.BlogDiscussion;
import com.gmonetix.codercampy.model.CourseDiscussion;
import com.gmonetix.codercampy.model.Discussion;
import com.gmonetix.codercampy.networking.APIClient;
import com.gmonetix.codercampy.networking.APIInterface;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gaurav Bordoloi on 3/31/2018.
 */
public class DiscussionPanel extends BottomSheetDialogFragment {

    private View rootView;
    @BindView(R.id.discussions_recyclerView) RecyclerView recyclerView;
    @BindView(R.id.discussion_et) AppCompatEditText editText;
    @BindView(R.id.send) MaterialRippleLayout addDiscussion;

    private String id;
    private boolean isCourse;

    private List<Discussion> discussionList;
    private DiscussionAdapter adapter;

    private APIInterface apiInterface;

    public static DiscussionPanel newInstance(String id, boolean isCourse) {
        DiscussionPanel f = new DiscussionPanel();
        Bundle bundle = new Bundle();
        bundle.putString("id",id);
        bundle.putBoolean("is_course",isCourse);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments().getString("id");
        isCourse = getArguments().getBoolean("is_course");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.discussion_panel,container,false);
        ButterKnife.bind(this,rootView);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        discussionList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        adapter = new DiscussionAdapter(getActivity(),apiInterface);
        recyclerView.setAdapter(adapter);

        if (isCourse) {
            apiInterface.getCourseDiscussionsById(id).enqueue(new Callback<CourseDiscussion>() {
                @Override
                public void onResponse(Call<CourseDiscussion> call, Response<CourseDiscussion> response) {
                    if (response.body() != null) {
                        discussionList.addAll(response.body().discussions);
                    }

                    adapter.setList(discussionList);
                    //recyclerView.scrollToPosition(dis.size()-1);
                }

                @Override
                public void onFailure(Call<CourseDiscussion> call, Throwable t) {
                    Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            apiInterface.getBlogDiscussionsById(id).enqueue(new Callback<BlogDiscussion>() {
                @Override
                public void onResponse(Call<BlogDiscussion> call, Response<BlogDiscussion> response) {
                    if (response.body() != null) {
                        discussionList.addAll(response.body().discussions);
                    }

                    adapter.setList(discussionList);
                }

                @Override
                public void onFailure(Call<BlogDiscussion> call, Throwable t) {
                    Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                }
            });
        }

        addDiscussion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String message = editText.getText().toString().trim();
                if (message.isEmpty()) {
                    Toasty.error(getActivity(),"Please fill the provided information",Toast.LENGTH_SHORT,true).show();
                    return;
                }

                if (isCourse) {
                    apiInterface.addCourseDiscussion(id, App.getAuth().getCurrentUser().getUid(),message).enqueue(new Callback<com.gmonetix.codercampy.model.Response>() {
                        @Override
                        public void onResponse(Call<com.gmonetix.codercampy.model.Response> call, Response<com.gmonetix.codercampy.model.Response> response) {
                            if (response.body().code.equals("success")) {

                                Discussion discussion = new Discussion(App.getAuth().getCurrentUser().getUid(),String.valueOf(System.currentTimeMillis()),message);
                                discussionList.add(discussion);
                                adapter.notifyDataSetChanged();

                                editText.setText("");
                                Toasty.success(getActivity(),"Discussion submitted",Toast.LENGTH_SHORT,true).show();
                            } else {
                                Toasty.error(getActivity(),"Some error occurred",Toast.LENGTH_SHORT,true).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<com.gmonetix.codercampy.model.Response> call, Throwable t) {
                            Toasty.error(getActivity(),"Some error occurred",Toast.LENGTH_SHORT,true).show();
                        }
                    });
                } else {
                    apiInterface.addBlogDiscussion(id, App.getAuth().getCurrentUser().getUid(),message).enqueue(new Callback<com.gmonetix.codercampy.model.Response>() {
                        @Override
                        public void onResponse(Call<com.gmonetix.codercampy.model.Response> call, Response<com.gmonetix.codercampy.model.Response> response) {
                            if (response.body().code.equals("success")) {

                                Discussion discussion = new Discussion(App.getAuth().getCurrentUser().getUid(),String.valueOf(System.currentTimeMillis()),message);
                                discussionList.add(discussion);
                                adapter.notifyDataSetChanged();

                                editText.setText("");
                                Toasty.success(getActivity(),"Discussion submitted",Toast.LENGTH_SHORT,true).show();
                            } else {
                                Toasty.error(getActivity(),"Some error occurred",Toast.LENGTH_SHORT,true).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<com.gmonetix.codercampy.model.Response> call, Throwable t) {
                            Toasty.error(getActivity(),"Some error occurred",Toast.LENGTH_SHORT,true).show();
                        }
                    });
                }

            }
        });

        return rootView;
    }

}
