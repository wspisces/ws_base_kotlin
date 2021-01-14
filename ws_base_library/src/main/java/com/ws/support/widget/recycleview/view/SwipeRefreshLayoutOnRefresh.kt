package com.ws.support.widget.recycleview.view

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

/**
 * Created by WuXiaolong
 * on 2015/7/7.
 * github:https://github.com/WuXiaolong/PullLoadMoreRecyclerView
 * weibo:http://weibo.com/u/2175011601
 * 微信公众号：吴小龙同学
 * 个人博客：http://wuxiaolong.me/
 */
class SwipeRefreshLayoutOnRefresh(private val mPullLoadMoreRecyclerView: PullLoadMoreRecyclerView) : SwipeRefreshLayout.OnRefreshListener {
    override fun onRefresh() {
        if (!mPullLoadMoreRecyclerView.isRefresh) {
            mPullLoadMoreRecyclerView.setIsRefresh(true)
            mPullLoadMoreRecyclerView.refresh()
        }
    }
}