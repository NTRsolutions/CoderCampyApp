package com.gmonetix.codercampy.util;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Gaurav Bordoloi on 3/28/2018.
 */

public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private int offset;

    public GridItemDecoration(int offset) {
        this.offset = offset;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view.getLayoutParams();

        if (layoutParams.getSpanIndex() % 2 == 0) {

            outRect.top = offset;
            outRect.left = offset;
            outRect.right = offset/2;

        } else {

            outRect.top = offset;
            outRect.right = offset;
            outRect.left = offset/2;

        }

    }

}
