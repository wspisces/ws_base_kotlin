package com.ws.support.utils

import android.content.Context
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.GridView
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.core.content.ContextCompat

/**
 *
 * Title:ViewMeasureUtils.java
 *
 * Description:控件测量工具类
 *
 * @author Johnny.xu
 * @date 2016年12月13日
 */
object ViewMeasureUtils {
    /**
     * 测量控件
     *
     * @param view
     */
    fun measureView(view: View, callback: MeasureViewCallback?) {
        val vto = view.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val width = view.width
                val height = view.height
                callback?.callback(view, width, height)
                view.viewTreeObserver.removeGlobalOnLayoutListener(this)
            }
        })
    }

    /**
     * listview 全部显示
     */
    fun setListViewMatchHeight(noScrollListView1: ListView, spaceHeight: Int) {
        // 获取ListView对应的Adapter
        val listAdapter = noScrollListView1.adapter ?: return
        var totalHeight = 0
        var i = 0
        val len = listAdapter.count
        while (i < len) {

            // listAdapter.getCount()返回数据项的数目
            val listItem = listAdapter.getView(i, null, noScrollListView1)
            // 计算子项View 的宽高
            listItem.measure(0, 0)
            // 统计所有子项的总高度
            totalHeight += listItem.measuredHeight
            i++
        }
        val params = noScrollListView1.layoutParams
        params.height = totalHeight + spaceHeight * (listAdapter.count - 1)
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        noScrollListView1.layoutParams = params
    }

    fun measureListViewHeight(lv: ListView) {
        val la = lv.adapter ?: return
        var h = 0
        val cnt = la.count
        for (i in 0 until cnt) {
            val item = la.getView(i, null, lv)
            item.measure(0, 0)
            h += item.measuredHeight
        }
        val lp = lv.layoutParams
        lp.height = h + lv.dividerHeight * (cnt - 1)
        lv.layoutParams = lp
    }

    /**
     * gridview 全部显示
     */
    fun setGridViewMatchHeight(noScrollListView1: GridView, Column: Int, spaceHeight: Int) {
        // 获取ListView对应的Adapter
        val listAdapter = noScrollListView1.adapter ?: return
        var totalHeight = 0
        var i = 0
        val len = if (listAdapter.count % Column == 0) listAdapter.count / Column else listAdapter.count / Column + 1
        while (i < len) {

            // listAdapter.getCount()返回数据项的数目
            val listItem = listAdapter.getView(i, null, noScrollListView1)
            // 计算子项View 的宽高
            listItem.measure(0, 0)
            // 统计所有子项的总高度
            totalHeight += listItem.measuredHeight
            i++
        }
        val params = noScrollListView1.layoutParams
        params.height = totalHeight + spaceHeight * (listAdapter.count - 1)
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        noScrollListView1.layoutParams = params
    }

    fun initViewVisibilityWithGone(view: View, isShow: Boolean) {
        view.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    fun initViewVisibilityWithInvisible(view: View, isShow: Boolean) {
        view.visibility = if (isShow) View.VISIBLE else View.INVISIBLE
    }

    fun initImageViewResource(view: ImageView, isSelect: Boolean, selectImageResource: Int, defaultImageResource: Int) {
        view.setImageResource(if (isSelect) selectImageResource else defaultImageResource)
    }

    fun initTextViewColorResource(context: Context, view: TextView, isSelect: Boolean, defaultColorId: Int, selectColorId: Int) {
        view.setTextColor(getResourceColorSelector(context, isSelect, defaultColorId, selectColorId))
    }

    fun getResourceColorSelector(context: Context, isSelect: Boolean, defaultColorId: Int, selectColorId: Int): Int {
        return getResourceColor(context, if (isSelect) selectColorId else defaultColorId)
    }

    fun getResourceColor(context: Context, colorId: Int): Int {
        return ContextCompat.getColor(context,colorId)
        //context.resources.getColor(colorId)
    }
}