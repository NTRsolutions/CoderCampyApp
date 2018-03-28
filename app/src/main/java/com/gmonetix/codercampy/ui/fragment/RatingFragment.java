package com.gmonetix.codercampy.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.adapter.RatingAdapter;
import com.gmonetix.codercampy.model.Rating;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RatingFragment extends Fragment {

    View rootView;
    @BindView(R.id.rating_recyclerView) RecyclerView recyclerView;
    @BindView(R.id.course_rating_send) MaterialRippleLayout send;
    @BindView(R.id.course_rating_et) AppCompatEditText editText;
    @BindView(R.id.course_rating) AppCompatRatingBar ratingBar;

    private static final String ARG_PARAM1 = "courseId";
    private static final String ARG_PARAM2 = "courseRatings";

    private String courseId;

    private List<Rating> ratingList = new ArrayList<>();
    private RatingAdapter adapter;

    public RatingFragment() { }

    public static RatingFragment newInstance(String course_id, List<Rating> ratingList) {
        RatingFragment fragment = new RatingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, course_id);
        args.putSerializable(ARG_PARAM2, (Serializable) ratingList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            courseId = getArguments().getString(ARG_PARAM1);
            ratingList = (List<Rating>) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_rating, container, false);
            ButterKnife.bind(this,rootView);

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter = new RatingAdapter(getActivity());
            recyclerView.setAdapter(adapter);
            adapter.setList(ratingList);

            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int rating = (int) ratingBar.getRating();
                    String message = editText.getText().toString().trim();

                    if (rating == 0) {
                        Toast.makeText(getActivity(), "Rating should be between 1-5", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (message.isEmpty()) {
                        Toast.makeText(getActivity(), "Message can't be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Toast.makeText(getActivity(), "" + rating + message, Toast.LENGTH_SHORT).show();
                    //TODO

                }
            });

        }

        return rootView;
    }

}
