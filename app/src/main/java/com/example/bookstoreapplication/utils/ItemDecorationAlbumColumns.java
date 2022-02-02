package com.example.bookstoreapplication.utils;

import androidx.recyclerview.widget.RecyclerView;

public class ItemDecorationAlbumColumns extends RecyclerView.ItemDecoration {
    private int mSizeGridSpacingPx;
    private int mGridSize;
    private boolean mNeedLeftSpacing = false;

    public ItemDecorationAlbumColumns(int gridSpacingPx, int gridSize) {
        mSizeGridSpacingPx = gridSpacingPx;
        mGridSize = gridSize;
    }


}
