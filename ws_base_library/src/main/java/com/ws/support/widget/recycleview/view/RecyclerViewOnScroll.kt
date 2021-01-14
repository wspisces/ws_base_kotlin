package com.ws.support.widget.recycleview.view

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * Created by WuXiaolong
 * on 2015/7/7.
 * github:https://github.com/WuXiaolong/PullLoadMoreRecyclerView
 * weibo:http://weibo.com/u/2175011601
 * 微信公众号：吴小龙同学
 * 个人博客：http://wuxiaolong.me/
 */
class RecyclerViewOnScroll(private val mPullLoadMoreRecyclerView: PullLoadMoreRecyclerView) : RecyclerView.OnScrollListener() {
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        var lastItem = 0
        var firstItem = 0
        val layoutManager = recyclerView.layoutManager
        val totalItemCount = layoutManager!!.itemCount
        if (layoutManager is GridLayoutManager) {
            val gridLayoutManager = layoutManager
            firstItem = gridLayoutManager.findFirstCompletelyVisibleItemPosition()
            //Position to find the final item of the current LayoutManager
            lastItem = gridLayoutManager.findLastCompletelyVisibleItemPosition()
            if (lastItem == -1) lastItem = gridLayoutManager.findLastVisibleItemPosition()
        } else if (layoutManager is LinearLayoutManager) {
            val linearLayoutManager = layoutManager
            firstItem = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
            lastItem = linearLayoutManager.findLastCompletelyVisibleItemPosition()
            if (lastItem == -1) lastItem = linearLayoutManager.findLastVisibleItemPosition()
        } else if (layoutManager is StaggeredGridLayoutManager) {
            val staggeredGridLayoutManager = layoutManager
            // since may lead to the final item has more than one StaggeredGridLayoutManager the particularity of the so here that is an array
            // this array into an array of position and then take the maximum value that is the last show the position value
            val lastPositions = IntArray(layoutManager.spanCount)
            staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(lastPositions)
            lastItem = findMax(lastPositions)
            firstItem = staggeredGridLayoutManager.findFirstVisibleItemPositions(lastPositions)[0]
        }
        if (firstItem == 0 || firstItem == RecyclerView.NO_POSITION) {
            if (mPullLoadMoreRecyclerView.pullRefreshEnable)
                mPullLoadMoreRecyclerView.setSwipeRefreshEnable(true)
        } else {
            mPullLoadMoreRecyclerView.setSwipeRefreshEnable(false)
        }
        //        if (mPullLoadMoreRecyclerView.getPushRefreshEnable()
//                && !mPullLoadMoreRecyclerView.isRefresh()
//                && mPullLoadMoreRecyclerView.isHasMore()
//                && (lastItem == totalItemCount - 1)
//                && !mPullLoadMoreRecyclerView.isLoadMore()
//                && (dx > 0 || dy > 0)) {
//            mPullLoadMoreRecyclerView.setIsLoadMore(true);
//            mPullLoadMoreRecyclerView.loadMore();
//        }
    }

    //To find the maximum value in the array
    private fun findMax(lastPositions: IntArray): Int {
        var max = lastPositions[0]
        for (value in lastPositions) {
            if (value > max) {
                max = value
            }
        }
        return max
    }
}