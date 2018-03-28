package com.gmonetix.codercampy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.model.Rating;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gaurav Bordoloi on 2/15/2018.
 */

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.RatingViewHolder>{

    private List<Rating> ratingList = new ArrayList<>();
    private Context context;

    public RatingAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<Rating> ratingList) {
        this.ratingList = ratingList;
        notifyDataSetChanged();
    }

    @Override
    public RatingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_rating,parent,false);
        return new RatingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RatingViewHolder holder, int position) {
        Rating rating = ratingList.get(position);

        holder.ratingName.setText(rating.id);
        holder.ratingMessage.setText(rating.message);
        holder.ratingBar.setRating(rating.rating);

    }

    @Override
    public int getItemCount() {
        return ratingList.size();
    }

    class RatingViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rating_name) TextView ratingName;
        @BindView(R.id.rating_no) RatingBar ratingBar;
        @BindView(R.id.rating_message) TextView ratingMessage;

        public RatingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }

}
