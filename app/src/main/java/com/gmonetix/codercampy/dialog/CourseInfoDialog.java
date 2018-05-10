package com.gmonetix.codercampy.dialog;

import android.app.DialogFragment;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.model.Language;
import com.gmonetix.codercampy.viewmodel.CourseViewModel;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Gaurav Bordoloi on 3/31/2018.
 */
public class CourseInfoDialog extends DialogFragment {

    private View rootView;
    @BindView(R.id.course_image) ImageView courseImage;
    @BindView(R.id.course_name) TextView courseName;
    @BindView(R.id.course_category) TextView courseCategory;
    @BindView(R.id.course_language) TextView courseLanguage;
    @BindView(R.id.course_description) TextView courseDescription;

    private String image, name, description, categoryId;
    private List<String> languages;

    private CourseViewModel courseViewModel;

    public CourseInfoDialog() {}

    public static CourseInfoDialog newInstance(String image, String courseName, String categoryId, List<String> languages, String description) {
        CourseInfoDialog f = new CourseInfoDialog();
        Bundle bundle = new Bundle();
        bundle.putString("image",image);
        bundle.putString("name",courseName);
        bundle.putString("description",description);
        bundle.putString("categoryId",categoryId);
        bundle.putStringArrayList("languages", (ArrayList<String>) languages);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        image = getArguments().getString("image");
        name = getArguments().getString("name");
        description = getArguments().getString("description");
        categoryId = getArguments().getString("categoryId");
        languages = getArguments().getStringArrayList("languages");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {

            rootView = inflater.inflate(R.layout.dialog_course_info,container,false);
            ButterKnife.bind(this,rootView);

            courseViewModel = ViewModelProviders.of((FragmentActivity) getActivity()).get(CourseViewModel.class);

            courseName.setText(name);
            courseDescription.setText(description);
            Glide.with(getActivity()).load(image).into(courseImage);

            courseViewModel.getCategory(categoryId).observe((LifecycleOwner) getActivity(), category -> {

                courseCategory.setText(category.name);

            });

            courseViewModel.getAllLanguages().observe((LifecycleOwner) getActivity(), response -> {

                StringBuilder s = new StringBuilder();

                for (Language language : response) {

                    if (languages.contains(language.id)) {
                        if (!s.toString().isEmpty())
                            s.append(" , ");
                        s.append(language.name);
                    }

                }

                courseLanguage.setText(s.toString());

            });

        }
        return rootView;
    }

    @OnClick(R.id.close_dialog)
    void closeDialog() {
        dismiss();
    }

}
