package com.goodapps.simplewhiteboard;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class GalleryItemDecoration extends RecyclerView.ItemDecoration {
    private final int space;

    public GalleryItemDecoration(int space) {
        this.space = space;


    }

    @Override
    public void getItemOffsets(@NonNull @NotNull Rect outRect, @NonNull @NotNull View view, @NonNull @NotNull RecyclerView parent, @NonNull @NotNull RecyclerView.State state) {
        int pos = parent.getChildLayoutPosition(view);

        outRect.bottom = space;
        outRect.left = space;

        if (pos == 0 || pos == 1 || pos == 2 || pos == 3) {
            outRect.top = space;
        }

        //adding right margin to last column
        if (pos % 4 == 3) {
            outRect.right = space;
        }


    }
}
