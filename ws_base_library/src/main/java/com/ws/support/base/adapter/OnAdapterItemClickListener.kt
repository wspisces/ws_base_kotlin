package com.ws.support.base.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * RecycleView Adapter 单击事件监听
 *
 * @author ws
 * @date 2020/8/20 16:28
 * 修改人：ws
 */
interface OnAdapterItemClickListener {
    fun onItemClick(holder: RecyclerView.ViewHolder?, position: Int)
    fun onItemChildClick(holder: RecyclerView.ViewHolder?, view: View?, position: Int)
}