package com.ws.support.base.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.ws.support.utils.ViewMeasureUtils
import java.util.*

/**
 *
 * Title:BaseAdapter.java
 *
 * Description:适配器基础类
 * @author Johnny.xu
 * @date 2016年11月9日
 */
abstract class CnBaseAdapter<T, E>(context: Context) : BaseAdapter() {
    private val mEmptyView: View? = null
    val data: MutableList<T>?
    protected var mContext: Context
    /**
     * 添加数据
     * @param list
     * @param isAdd true则添加数据并不清除之前数据，否则清除旧数据再次添加新数据
     */
    /**
     * 添加数据，默认重新添加，清除之前缓存
     * @param list
     */
    @JvmOverloads
    fun addAllData(list: List<T>?, isAdd: Boolean = false) {
        if (!isAdd) {
            data!!.clear()
        }
        if (list != null && list.size > 0) {
            data!!.addAll(list)
        }
        notifyDataSetChanged()
    }

    fun addDate(i: Int, d: T) {
        data!!.add(i, d)
        notifyDataSetChanged()
    }

    fun reSetItem(d: T) {
        val position = data!!.indexOf(d)
        if (position < 0 || position > count) {
            addDate(d)
        } else {
            data[position] = d
        }
    }

    /**
     * 插入数据
     */
    fun addDate(t: T) {
        if (data!!.contains(t)) {
            data.remove(t)
        }
        data.add(t)
        notifyDataSetChanged()
    }

    fun initEmptyViewLayout() {
        if (mEmptyView != null) {
            ViewMeasureUtils.initViewVisibilityWithGone(mEmptyView, count == 0)
        }
    }

    override fun getCount(): Int {
        return data?.size ?: 0
    }

    override fun getItem(position: Int): T? {
        return if (data != null && position < data.size && position >= 0) data[position] else null
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        var convertView = convertView
        val vb: E
        if (convertView == null) {
            convertView = View.inflate(mContext, layout, null)
            vb = getViewHolder(convertView)
            convertView.tag = vb
        } else {
            vb = convertView.tag as E
        }
        initData(position, vb)
        return convertView
    }

    abstract val layout: Int
    abstract fun getViewHolder(convertView: View): E
    abstract fun initData(position: Int, vh: E)
    fun getResColorById(id: Int): Int {
        return mContext.resources.getColor(id)
    }

    override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()
        initEmptyViewLayout()
    }

    fun removeData(t: T) {
        data!!.remove(t)
        notifyDataSetChanged()
    }

    fun clearData() {
        data!!.clear()
        notifyDataSetChanged()
    }

    init {
        data = ArrayList()
        mContext = context
    }
}