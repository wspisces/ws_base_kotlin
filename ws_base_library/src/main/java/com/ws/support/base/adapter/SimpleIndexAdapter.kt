package com.ws.support.base.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import com.ws.base.R
import com.ws.support.base.adapter.SimpleIndexAdapter.SimpleIndexViewHolder
import com.ws.support.base.entity.CnSimpleIndexInfoImp

/**
 * SimpleIndexAdapter.class
 * 基础标记适配器
 * @author Johnny.xu
 * time:2018/12/3
 */
class SimpleIndexAdapter(context: Context) : CnBaseAdapter<CnSimpleIndexInfoImp?, SimpleIndexViewHolder>(context) {
    override val layout: Int
        get() = R.layout.adapter_base_m_simple_index_item

    override fun getViewHolder(convertView: View): SimpleIndexViewHolder {
        return SimpleIndexViewHolder(convertView)
    }

    override fun initData(position: Int, vh: SimpleIndexViewHolder) {
        val infoImp = getItem(position)
        vh.tv_index.text = (position + 1).toString()
        vh.tv_content.text = infoImp?.simpleContent
    }

    inner class SimpleIndexViewHolder(view: View) {
        var tv_index: TextView
        var tv_content: TextView

        init {
            tv_index = view.findViewById(R.id.tv_index)
            tv_content = view.findViewById(R.id.tv_content)
        }
    }
}