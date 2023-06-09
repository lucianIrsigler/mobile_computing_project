package com.example.logintest;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class CenterItemDecoration extends RecyclerView.ItemDecoration {
    private boolean isFirstItem = true;
    private boolean isLastItem = false;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int itemCount = parent.getAdapter().getItemCount();

        if (position == 0) {
            isFirstItem = true;
            isLastItem = false;
        } else if (position == itemCount - 1) {
            isFirstItem = false;
            isLastItem = true;
        } else {
            isFirstItem = false;
            isLastItem = false;
        }

        int totalWidth = parent.getWidth();
        int itemWidth = view.getWidth();

        int margin = (totalWidth - itemWidth) / 2;
        if (isFirstItem) {
            outRect.left = totalWidth / 2 - itemWidth / 2;
            outRect.right = margin;
        } else if (isLastItem) {
            outRect.left = margin;
            outRect.right = totalWidth / 2 - itemWidth / 2;
        } else {
            outRect.left = margin;
            outRect.right = margin;
        }
    }
}