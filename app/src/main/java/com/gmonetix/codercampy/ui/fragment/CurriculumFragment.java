package com.gmonetix.codercampy.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.adapter.CurriculumAdapter;
import com.gmonetix.codercampy.listener.OnLectureClickListener;
import com.gmonetix.codercampy.model.Course;
import com.gmonetix.codercampy.model.Lecture;
import com.gmonetix.codercampy.networking.APIClient;
import com.gmonetix.codercampy.networking.APIInterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurriculumFragment extends Fragment {

    View rootView;
    @BindView(R.id.curriculum_recyclerView) RecyclerView recyclerView;

    private static final String ARG_PARAM1 = "lecture_list";

    private List<Lecture> lectureList;
    private CurriculumAdapter adapter;

    private OnLectureClickListener onLectureClickListener;

    public CurriculumFragment() { }

    public static CurriculumFragment newInstance(List<Lecture> lectureList) {
        CurriculumFragment fragment = new CurriculumFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, (Serializable) lectureList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lectureList = (List<Lecture>) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_curriculum, container, false);
            ButterKnife.bind(this,rootView);

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
            adapter = new CurriculumAdapter(getActivity(), new OnLectureClickListener() {
                @Override
                public void onLectureClick(String lectureId, String videoId) {
                    onLectureClickListener.onLectureClick(lectureId,videoId);
                }
            });
            recyclerView.setAdapter(adapter);

            adapter.setList(lectureList);

        }

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLectureClickListener) {
            onLectureClickListener = (OnLectureClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLectureClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onLectureClickListener = null;
    }

}
