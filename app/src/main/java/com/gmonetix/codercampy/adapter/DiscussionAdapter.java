package com.gmonetix.codercampy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.model.Discussion;
import com.gmonetix.codercampy.util.CustomLinearLayoutManager;
import com.gmonetix.codercampy.util.GetTimeAgo;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gaurav Bordoloi on 2/15/2018.
 */

public class DiscussionAdapter extends RecyclerView.Adapter<DiscussionAdapter.DiscussionViewHolder>{

    private Context context;
    private List<Discussion> discussionList = new ArrayList<>();

    public DiscussionAdapter(Context context) {
        this.context = context;
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
    public void onBindViewHolder(DiscussionViewHolder holder, int position) {
        Discussion discussion = discussionList.get(position);

        holder.discussionName.setText(discussion.uid);
        holder.discussionTime.setText(GetTimeAgo.getTimeAgo(Long.parseLong(discussion.timestamp),context));
        holder.discussionMessage.setText(discussion.message);

        if (discussion.replies != null && discussion.replies.size() > 0) {
            DiscussionAdapter adapter = new DiscussionAdapter(context);
            holder.recyclerViewReplies.setLayoutManager(new CustomLinearLayoutManager(context));
            holder.recyclerViewReplies.setAdapter(adapter);
            adapter.setList(discussion.replies);
        }

    }

    @Override
    public int getItemCount() {
        return discussionList.size();
    }

    class DiscussionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.discussion_name) TextView discussionName;
        @BindView(R.id.discussion_time) TextView discussionTime;
        @BindView(R.id.discussion_message) TextView discussionMessage;
        @BindView(R.id.recycler_view_replies) RecyclerView recyclerViewReplies;

        public DiscussionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }

}
