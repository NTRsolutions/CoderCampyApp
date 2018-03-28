package com.gmonetix.codercampy.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import com.gmonetix.codercampy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseOverviewFragment extends Fragment {

    @BindView(R.id.courseInstructorName) TextView courseInstructorName;
    @BindView(R.id.courseOverviewWebview) WebView courseOverViewWebview;
    private View rootView;

    private static final String ARG_OVERVIEW = "overview";
    private static final String ARG_INSTRUCTOR_ID = "instructor_id";

    private String overview;
    private String instructorId;

    public CourseOverviewFragment() {  }

    public static CourseOverviewFragment newInstance(String overview, String instructorId) {
        CourseOverviewFragment fragment = new CourseOverviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_OVERVIEW, overview);
        args.putString(ARG_INSTRUCTOR_ID, instructorId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            instructorId = getArguments().getString(ARG_INSTRUCTOR_ID);
            overview = getArguments().getString(ARG_OVERVIEW);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_course_overview, container, false);
            ButterKnife.bind(this,rootView);

            /*HomeActivity.database.collection("instructors").document(instructorId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    courseInstructorName.setText(task.getResult().getString("name"));
                }
            });*/

            courseOverViewWebview.loadData(overview,"text/html","UTF-8");

        }

        return rootView;
    }

}
