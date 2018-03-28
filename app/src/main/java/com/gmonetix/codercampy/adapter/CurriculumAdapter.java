package com.gmonetix.codercampy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.listener.OnLectureClickListener;
import com.gmonetix.codercampy.model.Lecture;
import com.gmonetix.codercampy.util.GlideOptions;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gaurav Bordoloi on 2/15/2018.
 */

public class CurriculumAdapter extends RecyclerView.Adapter<CurriculumAdapter.LectureViewHolder>{

    private Context context;
    private List<Lecture> list = new ArrayList<>();

    private OnLectureClickListener onLectureClickListener;

    public CurriculumAdapter(Context context, OnLectureClickListener onLectureClickListener) {
        this.context = context;
        this.onLectureClickListener = onLectureClickListener;
    }

    public void setList(List<Lecture> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public LectureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View lectureView = LayoutInflater.from(context).inflate(R.layout.row_lecture,parent,false);
        return new LectureViewHolder(lectureView);
    }

    @Override
    public void onBindViewHolder(LectureViewHolder holder, int position) {
        Lecture lecture = list.get(position);
        holder.bind(lecture);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected class LectureViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.lecture_name) TextView lectureName;
        @BindView(R.id.lecture_duration) TextView lectureDuration;
        @BindView(R.id.lecture_details) ImageView lectureDetails;
        @BindView(R.id.lecture_image) ImageView lectureImage;

        public LectureViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void bind(final Lecture lecture) {
            lectureName.setText(lecture.name);
            lectureDuration.setText(lecture.duration);
            Glide.with(context).load(GlideOptions.getThumbnailLink(lecture.video)).apply(GlideOptions.getRequestOptions(R.drawable.course_default,R.drawable.course_default)).into(lectureImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onLectureClickListener.onLectureClick(lecture.id, lecture.video);
                }
            });

            lectureDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialDialog.Builder(context)
                            .title("Description")
                            .content(lecture.description)
                            .icon(context.getResources().getDrawable(R.drawable.ic_info_outline))
                            .show();
                }
            });

        }

    }

}
