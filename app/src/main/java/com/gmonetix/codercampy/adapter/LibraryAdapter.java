package com.gmonetix.codercampy.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.model.Library;
import com.gmonetix.codercampy.util.IntentUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gaurav Bordoloi on 4/1/2018.
 */
public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.LibraryViewHolder> {

    private Context context;
    private List<Library> libraryList = new ArrayList<>();

    public LibraryAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<Library> libraryList) {
        this.libraryList = libraryList;
    }

    @NonNull
    @Override
    public LibraryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_library,parent,false);
        return new LibraryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LibraryViewHolder holder, int position) {
        final Library library = libraryList.get(position);

        holder.textView.setText(library.name);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.openCustomChromeTab(context, Uri.parse(library.link));
            }
        });

    }

    @Override
    public int getItemCount() {
        return libraryList.size();
    }

    class LibraryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.library_name) TextView textView;

        public LibraryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }

}
