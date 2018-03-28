package com.gmonetix.codercampy.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.gmonetix.codercampy.ui.activity.CourseDetailsActivity;
import android.widget.Filter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gaurav Bordoloi on 2/14/2018.
 */

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder>{

    private final static String COURSE = "course";
    public static final String ACTION_LIKE_IMAGE_DOUBLE_CLICKED = "action_like_image_button";

    private Context context;
    private List<Course> courseList = new ArrayList<>();
    private List<Course> searchList;

    private RequestManager glide;

    public CourseAdapter(Context context) {
        this.context = context;
        glide = Glide.with(context);
    }

    public void setList(List<Course> courseList) {
        this.courseList = courseList;
        notifyDataSetChanged();
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_course,parent,false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, final int position) {
        final Course course = courseList.get(position);

        Log.e("TAG",courseList.toString());


        glide.load(course.image).into(holder.courseImage);
        holder.courseName.setText(course.name);
        holder.ratingBar.setRating(4);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                notifyItemChanged(position,ACTION_LIKE_IMAGE_DOUBLE_CLICKED);
                return false;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, CourseDetailsActivity.class);
                intent.putExtra(COURSE,(Serializable) course);
                context.startActivity(intent);

            }
        });

    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                final FilterResults oReturn = new FilterResults();
                final List<Course> results = new ArrayList<>();
                if (searchList == null) {
                    searchList = courseList;
                }
                if (charSequence != null) {
                    if (searchList != null && searchList.size() > 0) {
                        for (Course course : searchList) {
                            if (course.name.toLowerCase().contains(charSequence.toString())) {
                                results.add(course);
                            }
                        }
                    }
                    oReturn.values = results;
                    oReturn.count = results.size();
                }
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                courseList = (ArrayList<Course>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {

        public CardView root;
        @BindView(R.id.course_image) ImageView courseImage;
        @BindView(R.id.course_name) TextView courseName;
        @BindView(R.id.course_rating) RatingBar ratingBar;

        public CourseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            root = (CardView) itemView.findViewById(R.id.root);
        }

    }

}
