package com.gmonetix.codercampy.callback;

import android.support.v7.util.DiffUtil;

import com.gmonetix.codercampy.model.Course;

import java.util.List;

/**
 * Created by Gaurav Bordoloi on 4/1/2018.
 */
public class CourseDiffCallBack extends DiffUtil.Callback {

    private List<Course> newList;
    private List<Course> oldList;

    public CourseDiffCallBack(List<Course> newList,List<Course> oldList) {
        this.newList = newList;
        this.oldList = oldList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).id.equals(newList.get(newItemPosition).id);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition) == newList.get(newItemPosition);
    }

}
