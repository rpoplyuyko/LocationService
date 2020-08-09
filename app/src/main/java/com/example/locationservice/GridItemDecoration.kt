package com.example.locationservice

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridItemDecoration(gridSpacingPx: Int, gridSize: Int) : RecyclerView.ItemDecoration() {
    private var mSizeGridSpacingPx: Int = gridSpacingPx
    private var mGridSize: Int = gridSize

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val frameWidth = ((parent.width - mSizeGridSpacingPx.toFloat() * (mGridSize - 1)) / mGridSize).toInt()
        val padding = parent.width / mGridSize - frameWidth
        val itemPosition = (view.getLayoutParams() as RecyclerView.LayoutParams).viewAdapterPosition

        outRect.top = mSizeGridSpacingPx
        outRect.bottom = mSizeGridSpacingPx

        if (mGridSize > 1) {
            if (itemPosition % mGridSize == 0 && outRect.left != mSizeGridSpacingPx) {
                outRect.left = mSizeGridSpacingPx
                outRect.right = padding
            } else {
                outRect.left = padding
                outRect.right = mSizeGridSpacingPx
            }
        } else {
            if (outRect.left != mSizeGridSpacingPx) {
                outRect.left = mSizeGridSpacingPx
                outRect.right = mSizeGridSpacingPx
            }

        }
    }
}
