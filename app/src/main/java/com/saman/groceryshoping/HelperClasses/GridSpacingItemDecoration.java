package com.saman.groceryshopping.HelperClasses;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;


import androidx.annotation.Keep;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

@Keep
public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = "GridSpacingItemDecorati";
    private final int spacing;

    public GridSpacingItemDecoration(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(@NotNull Rect outRect, View view, RecyclerView parent, @NotNull RecyclerView.State state) {

        GridLayoutManager gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();
        assert gridLayoutManager != null;
        int spanCount = gridLayoutManager.getSpanCount();

        GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) view.getLayoutParams();

        int spanIndex = params.getSpanIndex();
        int spanSize = params.getSpanSize();

        Log.d(TAG, "getItemOffsets: " + spanIndex);

        // If it is in column 0 you apply the full offset on the start side, else only half
        if (spanIndex == 0) {
            outRect.left = spacing;
        } else {
            outRect.left = spacing / 2;
        }

        // If spanIndex + spanSize equals spanCount (it occupies the last column) you apply the full offset on the end, else only half.
        if (spanIndex + spanSize == spanCount) {
            outRect.right = spacing;
        } else {
            outRect.right = spacing / 2;
        }

        // just add some vertical padding as well
        outRect.top = spacing / 2;
        outRect.bottom = spacing / 2;
    }
}
