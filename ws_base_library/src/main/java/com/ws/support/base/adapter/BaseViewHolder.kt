package com.ws.support.base.adapter

import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView

/**
 * BaseViewHolder.class
 * 基础复用对象
 * time:2017/7/28
 */
class BaseViewHolder(var mBaseView: View) {
    protected fun findTextViewById(id: Int): TextView {
        return mBaseView.findViewById<View>(id) as TextView
    }

    protected fun findImageViewById(id: Int): ImageView {
        return mBaseView.findViewById<View>(id) as ImageView
    }

    protected fun findListViewById(id: Int): ListView {
        return mBaseView.findViewById<View>(id) as ListView
    }

    protected fun findGroupViewById(id: Int): View {
        return mBaseView.findViewById(id)
    }
}