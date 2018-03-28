package com.gmonetix.codercampy.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.model.Instructor;
import com.gmonetix.codercampy.ui.activity.InstructorDetailsActivity;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gaurav Bordoloi on 2/21/2018.
 */

public class InstructorAdapter extends RecyclerView.Adapter<InstructorAdapter.InstructorViewHolder>{

    public static final String ACTION_LIKE_IMAGE_DOUBLE_CLICKED = "action_like_image_button";

    private List<Instructor> instructorList = new ArrayList<>();
    private Context context;
    private List<Instructor> searchList;


    public InstructorAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<Instructor> instructorList) {
        this.instructorList = instructorList;
        notifyDataSetChanged();
    }

    @Override
    public InstructorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_instructor,parent,false);
        return new InstructorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final InstructorViewHolder holder, final int position) {
        final Instructor instructor = instructorList.get(position);

        holder.name.setText(instructor.name);

        Glide.with(context).load(instructor.image).into(holder.image);

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

                Intent intent = new Intent(context, InstructorDetailsActivity.class);
                intent.putExtra("instructor",instructor);

                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View,String>(holder.image,"transition_image");
                pairs[1] = new Pair<View,String>(holder.name,"transition_name");

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, pairs);

                context.startActivity(intent,options.toBundle());

            }
        });

    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                final FilterResults oReturn = new FilterResults();
                final List<Instructor> results = new ArrayList<>();
                if (searchList == null) {
                    searchList = instructorList;
                }
                if (charSequence != null) {
                    if (searchList != null && searchList.size() > 0) {
                        for (Instructor instructor : searchList) {
                            if (instructor.name.toLowerCase().contains(charSequence.toString())) {
                                results.add(instructor);
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
                instructorList = (ArrayList<Instructor>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return instructorList.size();
    }

    public class InstructorViewHolder extends RecyclerView.ViewHolder {

        public CardView root;
        @BindView(R.id.instructor_name) TextView name;
        @BindView(R.id.instructor_image) ImageView image;

        public InstructorViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            root = (CardView) itemView.findViewById(R.id.root);
        }
    }

}
