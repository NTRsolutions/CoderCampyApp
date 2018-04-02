package com.gmonetix.codercampy.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.model.Blog;
import com.gmonetix.codercampy.ui.activity.BlogDetailsActivity;
import com.gmonetix.codercampy.util.GetTimeAgo;
import com.gmonetix.codercampy.util.GlideOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gaurav Bordoloi on 3/7/2018.
 */

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.MyViewHolder> {

    private Context context;
    private List<Blog> blogs = new ArrayList<>();
    private List<Blog> searchList;

    private RequestManager glide;

    public BlogAdapter(Context context) {
        this.context = context;
        glide = Glide.with(context);
    }

    public void setList(List<Blog> blogs) {
        this.blogs = blogs;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_blog,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Blog blog = blogs.get(position);

        holder.blogName.setText(blog.name);
        holder.blogTime.setText(GetTimeAgo.getTimeAgo(Long.parseLong(blog.time),context));
        glide.load(blog.image).apply(GlideOptions.getRequestOptions(R.drawable.course_default,R.drawable.course_default)).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BlogDetailsActivity.class);
                intent.putExtra("blog",(Serializable) blog);
                context.startActivity(intent);
            }
        });

    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                final FilterResults oReturn = new FilterResults();
                final List<Blog> results = new ArrayList<>();
                if (searchList == null) {
                    searchList = blogs;
                }
                if (charSequence != null) {
                    if (searchList != null && searchList.size() > 0) {
                        for (Blog blog : searchList) {
                            if (blog.name.toLowerCase().contains(charSequence.toString())) {
                                results.add(blog);
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
                blogs = (ArrayList<Blog>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return blogs.size();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.blog_image) ImageView imageView;
        @BindView(R.id.blog_name) TextView blogName;
        @BindView(R.id.blog_time) TextView blogTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }

}
