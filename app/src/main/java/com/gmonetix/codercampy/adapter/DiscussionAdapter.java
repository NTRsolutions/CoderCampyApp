package com.gmonetix.codercampy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.model.Discussion;
import com.gmonetix.codercampy.model.Name;
import com.gmonetix.codercampy.networking.APIInterface;
import com.gmonetix.codercampy.util.GetTimeAgo;
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

public class DiscussionAdapter extends RecyclerView.Adapter<DiscussionAdapter.DiscussionViewHolder>{

    private Context context;
    private List<Discussion> discussionList = new ArrayList<>();

    private APIInterface apiInterface;

    public DiscussionAdapter(Context context, APIInterface apiInterface) {
        this.context = context;
        this.apiInterface = apiInterface;
    }

    public void setList(List<Discussion> discussionList) {
        this.discussionList = discussionList;
        notifyDataSetChanged();
    }

    @Override
    public DiscussionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_discussion, parent, false);
        return new DiscussionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DiscussionViewHolder holder, int position) {
        Discussion discussion = discussionList.get(position);

        holder.discussionName.setText(discussion.uid);
        holder.discussionTime.setText(GetTimeAgo.getTimeAgo(Long.parseLong(discussion.timestamp),context));
        holder.discussionMessage.setText(discussion.message);

        apiInterface.getUserNameByUid(discussion.uid).enqueue(new Callback<Name>() {
            @Override
            public void onResponse(Call<Name> call, Response<Name> response) {
                holder.discussionName.setText(response.body().name);
            }

            @Override
            public void onFailure(Call<Name> call, Throwable t) {
                Log.e("TAG","error - " + t.getMessage());
            }
        });

    }

    @Override
    public int getItemCount() {
        return discussionList.size();
    }

    class DiscussionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.discussion_name) TextView discussionName;
        @BindView(R.id.discussion_time) TextView discussionTime;
        @BindView(R.id.discussion_message) TextView discussionMessage;

        public DiscussionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }

}
