package com.gustavovenegas.moviebrowser;

import android.widget.AbsListView;

/**
 * Created by gustavovenegas on 08.12.16.
 * Abstract class for implementing infinite scroll
 */

public abstract class InfiniteScrollListener implements AbsListView.OnScrollListener {
    private int currentVisibleItemCount;
    private int currentScrollState;
    private int currentFirstVisibleItem;
    private int totalItem;

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        this.currentScrollState = scrollState;
        this.isScrollCompleted();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.currentFirstVisibleItem = firstVisibleItem;
        this.currentVisibleItemCount = visibleItemCount;
        this.totalItem = totalItemCount;
    }

    private void isScrollCompleted() {
        if (totalItem - currentFirstVisibleItem == currentVisibleItemCount && this.currentScrollState == SCROLL_STATE_IDLE) {
            loadMore();
        }
    }

    public abstract void loadMore();
}
