package com.gmonetix.codercampy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.model.Name;
import com.gmonetix.codercampy.model.Rating;
import com.gmonetix.codercampy.networking.APIInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gaurav Bordoloi on 2/15/2018.
 */

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.RatingViewHolder>{

    private List<Rating> ratingList = new ArrayList<>();
    private Context context;

    private APIInterface apiInterface;

    public RatingAdapter(Context context, APIInterface apiInterface) {
        this.context = context;
        this.apiInterface = apiInterface;
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
    public void onBindViewHolder(final RatingViewHolder holder, int position) {
        Rating rating = ratingList.get(position);

        holder.ratingName.setText(rating.uid);
        holder.ratingMessage.setText(rating.message);
        holder.ratingBar.setRating(rating.rating);

        apiInterface.getUserNameByUid(rating.uid).enqueue(new Callback<Name>() {
            @Override
            public void onResponse(Call<Name> call, Response<Name> response) {
                holder.ratingName.setText(response.body().name);
            }

            @Override
            public void onFailure(Call<Name> call, Throwable t) {
                Log.e("TAG","error - " + t.getMessage());
            }
        });

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
