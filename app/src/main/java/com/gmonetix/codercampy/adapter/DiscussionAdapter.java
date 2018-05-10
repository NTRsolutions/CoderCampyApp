package com.gmonetix.codercampy.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.listener.OnLoadMoreListener;
import com.gmonetix.codercampy.model.Discussion;
import com.gmonetix.codercampy.util.GetTimeAgo;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Gaurav Bordoloi on 2/15/2018.
 */

public class DiscussionAdapter extends RecyclerView.Adapter<DiscussionAdapter.DiscussionViewHolder>{

    private Context context;
    private List<Discussion> discussionList = new ArrayList<>();

    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private OnLoadMoreListener onLoadMoreListener;

    private RequestManager glide;

    public DiscussionAdapter(Context context, RecyclerView recyclerView) {
        this.context = context;
        glide = Glide.with(context);

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });

    }

    public void setLoaded() {
        isLoading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
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

        holder.discussionName.setText(discussion.user.name);
        holder.discussionTime.setText(GetTimeAgo.getTimeAgo(discussion.timestamp,context));
        holder.discussionMessage.setText(discussion.message);
        glide.load(discussion.user.image).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return discussionList.size();
    }

    class DiscussionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.discussion_name) TextView discussionName;
        @BindView(R.id.discussion_time) TextView discussionTime;
        @BindView(R.id.discussion_message) TextView discussionMessage;
        @BindView(R.id.profile_image) CircleImageView imageView;

        public DiscussionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }

}
