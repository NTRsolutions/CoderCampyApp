package com.gmonetix.codercampy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.listener.OnAdapterItemClickListener;
import com.gmonetix.codercampy.util.GlideOptions;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gaurav Bordoloi on 3/6/2018.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

    public static final String ACTION_LIKE_IMAGE_DOUBLE_CLICKED = "action_like_image_button";

    private List<String> list = new ArrayList<>();
    private Context context;

    private OnAdapterItemClickListener onClickListener;

    private RequestManager glide;

    public ImageAdapter(Context context, OnAdapterItemClickListener onClickListener) {
        this.context = context;
        this.onClickListener = onClickListener;
        glide = Glide.with(context);
    }

    public void setList(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_imageview,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        glide.load(list.get(position)).apply(GlideOptions.getRequestOptions(R.drawable.course_default,R.drawable.course_default)).into(holder.imageView);

        holder.itemView.setOnClickListener(v -> {
            notifyItemChanged(position,ACTION_LIKE_IMAGE_DOUBLE_CLICKED);
            onClickListener.onClick(position);
        });

        holder.itemView.setOnLongClickListener(v -> {
            notifyItemChanged(position,ACTION_LIKE_IMAGE_DOUBLE_CLICKED);
            return false;
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView) ImageView imageView;
        public LinearLayout root;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            root = (LinearLayout) itemView.findViewById(R.id.root);
        }
    }

}