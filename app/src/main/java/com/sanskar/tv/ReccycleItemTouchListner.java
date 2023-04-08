package com.sanskar.tv;

import androidx.recyclerview.widget.RecyclerView;

public interface ReccycleItemTouchListner {

    void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);

}
