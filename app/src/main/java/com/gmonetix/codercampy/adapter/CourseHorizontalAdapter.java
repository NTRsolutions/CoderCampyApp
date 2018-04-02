package com.gmonetix.codercampy.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.model.Course;
import com.gmonetix.codercampy.ui.activity.ClassRoomActivity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gaurav Bordoloi on 3/30/2018.
 */
public class CourseHorizontalAdapter extends RecyclerView.Adapter<CourseHorizontalAdapter.CourseViewHolder>{

    private final static String COURSE = "course";

    private Context context;
    private List<Course> courseList = new ArrayList<>();

    private RequestManager glide;

    public CourseHorizontalAdapter(Context context) {
        this.context = context;
        glide = Glide.with(context);
    }

    public void setList(List<Course> courseList) {
        this.courseList = courseList;
        notifyDataSetChanged();
    }

    @Override
    public CourseHorizontalAdapter.CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_course_tile,parent,false);
        return new CourseHorizontalAdapter.CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CourseHorizontalAdapter.CourseViewHolder holder, final int position) {
        final Course course = courseList.get(position);

        glide.load(course.image).into(holder.courseImage);
        holder.courseName.setText(course.name);
        holder.ratingBar.setRating(4);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ClassRoomActivity.class);
                intent.putExtra(COURSE,(Serializable) course);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.course_image) ImageView courseImage;
        @BindView(R.id.course_name) TextView courseName;
        @BindView(R.id.course_rating) RatingBar ratingBar;

        public CourseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }

}
