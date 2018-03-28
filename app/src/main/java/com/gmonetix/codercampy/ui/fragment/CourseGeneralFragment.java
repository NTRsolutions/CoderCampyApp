package com.gmonetix.codercampy.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.listener.OnButtonClickListener;
import com.gmonetix.codercampy.listener.OnLectureClickListener;
import com.gmonetix.codercampy.model.Category;
import com.gmonetix.codercampy.model.Instructor;
import com.gmonetix.codercampy.model.Language;
import com.gmonetix.codercampy.networking.APIClient;
import com.gmonetix.codercampy.networking.APIInterface;
import com.gmonetix.codercampy.ui.activity.ClassRoomActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseGeneralFragment extends Fragment {

    @BindView(R.id.course_language) TextView courseLanguage;
    @BindView(R.id.course_name) TextView courseName;
    @BindView(R.id.course_category) TextView courseCategory;
    @BindView(R.id.course_description) TextView courseDescription;
    @BindView(R.id.learnNow) Button learnNow;
    private View rootView;

    private static final String ARG_COURSE_ID = "course_id";
    private static final String ARG_COURSE_NAME = "course_name";
    private static final String ARG_COURSE_DESCRIPTION = "course_description";
    private static final String ARG_COURSE_CATEGORY_ID = "course_categoryId";
    private static final String ARG_COURSE_LANGUAGE = "course_language";

    private final static String COURSE_ID = "course_id";

    private APIInterface apiInterface;

    private String name, description;
    private String courseId, category_id;
    private List<String> languageList;

    private OnButtonClickListener onButtonClickListener;

    public CourseGeneralFragment() { }

    public static CourseGeneralFragment newInstance(String id, String name, String description, String category_id, ArrayList<String> languages) {
        CourseGeneralFragment fragment = new CourseGeneralFragment();
        Bundle args = new Bundle();
        args.putString(ARG_COURSE_ID, id);
        args.putString(ARG_COURSE_NAME, name);
        args.putString(ARG_COURSE_DESCRIPTION, description);
        args.putString(ARG_COURSE_CATEGORY_ID, category_id);
        args.putStringArrayList(ARG_COURSE_LANGUAGE, languages);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            courseId = getArguments().getString(ARG_COURSE_ID);
            name = getArguments().getString(ARG_COURSE_NAME);
            description = getArguments().getString(ARG_COURSE_DESCRIPTION);
            languageList = getArguments().getStringArrayList(ARG_COURSE_LANGUAGE);
            category_id = getArguments().getString(ARG_COURSE_CATEGORY_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_course_general, container, false);
            ButterKnife.bind(this,rootView);

            apiInterface = APIClient.getClient().create(APIInterface.class);

            courseName.setText(name);
            courseDescription.setText(description);

            Call<Category> c1 = apiInterface.getCategory(category_id);
            c1.enqueue(new Callback<Category>() {
                @Override
                public void onResponse(Call<Category> call, Response<Category> response) {
                    if (response.body() != null) {
                        courseCategory.setText(response.body().name);
                    }
                }

                @Override
                public void onFailure(Call<Category> call, Throwable t) {
                    Log.e("Error",t.getMessage());
                }
            });

            Call<List<Language>> c2 = apiInterface.getAllLanguages();
            c2.enqueue(new Callback<List<Language>>() {
                @Override
                public void onResponse(Call<List<Language>> call, Response<List<Language>> response) {

                    StringBuilder s = new StringBuilder();

                    for (Language language : response.body()) {

                        if (languageList.contains(language.id)) {
                            if (!s.toString().isEmpty())
                                s.append(" , ");
                            s.append(language.name);
                        }

                    }

                    courseLanguage.setText(s.toString());

                }

                @Override
                public void onFailure(Call<List<Language>> call, Throwable t) {
                    Log.e("Error",t.getMessage());
                }
            });

            learnNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonClickListener.onButtonClicked();
                }
            });

        }

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnButtonClickListener) {
            onButtonClickListener = (OnButtonClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnButtonClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onButtonClickListener = null;
    }

}
