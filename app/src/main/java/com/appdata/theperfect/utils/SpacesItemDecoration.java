package com.appdata.theperfect.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
  private int space;

  public SpacesItemDecoration(int space) {
    this.space = space;
  }

  @Override
  public void getItemOffsets(Rect outRect, View view,
                             RecyclerView parent, RecyclerView.State state) {

    outRect.bottom = space;

    // Add top margin only for the first item to avoid double space between items
    if (parent.getChildLayoutPosition(view)%2 == 0) {
      outRect.left = space;
      outRect.right = space;
    } else {
      outRect.right = space;
    }
  }
}