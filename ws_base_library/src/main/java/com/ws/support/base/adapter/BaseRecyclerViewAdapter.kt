package com.ws.support.base.adapter

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * @anthor wg
 * @time 2017/9/6 14:31
 * @describe 基础类
 */
abstract class BaseRecyclerViewAdapter<T>(ctx: Context, l: List<T>?) : RecyclerView.Adapter<RecyclerViewHolder>() {
    var dataList: MutableList<T>
    var mInflater: LayoutInflater
    var mContext: Context
    fun addAllData(dataList: List<T>?) {
        this.dataList.addAll(dataList!!)
        notifyDataSetChanged()
    }

    fun removeData(position: Int) {
        dataList.removeAt(position)
        notifyDataSetChanged()
    }

    fun clearData() {
        dataList.clear()
        notifyDataSetChanged()
    }

    fun removeData(t: T?) {
        if (t != null) {
            dataList.remove(t)
        }
        notifyDataSetChanged()
    }

    fun replaceData(dataList: List<T>?) {
        this.dataList.clear()
        if (dataList != null) {
            this.dataList.addAll(dataList)
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            mOnItemClickListen?.onClick(position, dataList[position])
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    var mOnItemClickListen: OnItemClickListen<T>? = null

    interface OnItemClickListen<T> {
        fun onClick(position: Int, item: T)
    }

    fun setOnItemClickListener(mOnItemClickListen: OnItemClickListen<T>?) {
        this.mOnItemClickListen = mOnItemClickListen
    }

    init {
        dataList = (l ?: ArrayList()) as MutableList<T>
        mInflater = ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mContext = ctx
    }
}