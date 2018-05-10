package com.gmonetix.codercampy.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import com.gmonetix.codercampy.App;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.listener.OnLoadMoreListener;
import com.gmonetix.codercampy.model.Course;
import com.gmonetix.codercampy.model.Response;
import com.gmonetix.codercampy.networking.APIInterface;
import com.gmonetix.codercampy.ui.activity.ClassRoomActivity;
import com.gmonetix.codercampy.ui.activity.Home;
import com.sackcentury.shinebuttonlib.ShineButton;
import android.widget.Filter;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;

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
    private APIInterface apiInterface;

    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private OnLoadMoreListener onLoadMoreListener;

    public CourseAdapter(Context context, RecyclerView recyclerView, APIInterface apiInterface) {
        this.context = context;
        glide = Glide.with(context);
        this.apiInterface = apiInterface;

        final GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = gridLayoutManager.getItemCount();
                lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
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
    public void onBindViewHolder(final CourseViewHolder holder, final int position) {
        final Course course = courseList.get(position);

        if (App.getAuth().getCurrentUser() == null) {
            holder.favBtn.setVisibility(View.GONE);
        } else {
            holder.favBtn.setVisibility(View.VISIBLE);
            if (Home.user.favourites != null) {
                if (Home.user.favourites.contains(course.id)) {
                    holder.favBtn.setChecked(true);
                } else {
                    holder.favBtn.setChecked(false);
                }
            }
        }

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

        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(context, ClassRoomActivity.class);
            intent.putExtra(COURSE,(Serializable) course);
            context.startActivity(intent);

        });

        holder.favBtn.setOnCheckStateChangeListener((view, checked) -> {
            if (checked) {
                apiInterface.addFav(course.id, App.getAuth().getCurrentUser().getUid()).enqueue(new Callback<Response>() {
                    @Override
                    public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                        if (response.body().code.equals("success")) {
                            Home.user.favourites.add(course.id);
                            Toasty.success(context,"Added to favourites", Toast.LENGTH_SHORT,true).show();
                        } else {
                            holder.favBtn.setChecked(false);
                            Toasty.error(context,"Error adding to favourite", Toast.LENGTH_SHORT,true).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Response> call, Throwable t) {
                        holder.favBtn.setChecked(false);
                        Toasty.error(context,"Error adding to favourite", Toast.LENGTH_SHORT,true).show();
                    }
                });
            } else {
                apiInterface.removeFav(course.id, App.getAuth().getCurrentUser().getUid()).enqueue(new Callback<Response>() {
                    @Override
                    public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                        if (response.body().code.equals("success")) {
                            Home.user.favourites.remove(course.id);
                            Toasty.success(context,"Removed from favourites", Toast.LENGTH_SHORT,true).show();
                        } else {
                            holder.favBtn.setChecked(true);
                            Toasty.error(context,"Error removing from favourite", Toast.LENGTH_SHORT,true).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Response> call, Throwable t) {
                        holder.favBtn.setChecked(true);
                        Toasty.error(context,"Error removing from favourite", Toast.LENGTH_SHORT,true).show();
                    }
                });
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
        @BindView(R.id.favbtn) ShineButton favBtn;

        public CourseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            root = (CardView) itemView.findViewById(R.id.root);
        }

    }

}
