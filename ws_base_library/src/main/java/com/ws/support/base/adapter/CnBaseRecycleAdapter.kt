package com.ws.support.base.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ws.support.widget.recycleview.view.PullLoadMoreRecyclerView
import com.ws.support.widget.recycleview.view.PullLoadMoreRecyclerView.PullLoadMoreListener
import java.util.*

/**
 * CnBaseRecycleAdapter.class
 * Recycle控件适配器
 * @author Johnny.xu
 * time:2019/2/20
 */
abstract class CnBaseRecycleAdapter<T, E>(ctx: Context) : BaseRecyclerViewAdapter<T>(ctx, ArrayList<T>()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(itemId, parent, false)
        return RecyclerViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        initView(getViewHolder(holder), position)
    }

    fun initRecycleConfig(recyclerView: PullLoadMoreRecyclerView, isRefresh: Boolean, isLoadMore: Boolean,
                          listener: PullLoadMoreListener?) {
        recyclerView.setLinearLayout()
        recyclerView.setOnPullLoadMoreListener(listener)
        recyclerView.pushRefreshEnable = isRefresh
        recyclerView.pullRefreshEnable = isLoadMore
    }

    abstract val itemId: Int
    abstract fun getViewHolder(holder: RecyclerViewHolder?): E
    abstract fun initView(e: E, position: Int)
    fun <Y : View?> getViewByViewHolder(holder: RecyclerViewHolder, id: Int): Y {
        return RecyclerViewHolder.Companion.get(holder.itemView, id)!!
    }

    val noneDataView: Int
        get() = if (itemCount == 0) View.VISIBLE else View.GONE
}