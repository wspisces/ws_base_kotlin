package com.ws.component.flowlayout

import android.util.Log
import android.view.View
import java.util.*

abstract class TagAdapter<T> {
    private val mTagDatas: List<T>?
    private var mOnDataChangedListener: OnDataChangedListener? = null
    val preCheckedList = HashSet<Int>()

    constructor(datas: List<T>?) {
        mTagDatas = datas
    }

    constructor(datas: Array<T>) {
        mTagDatas = ArrayList(Arrays.asList(*datas))
    }

    fun setOnDataChangedListener(listener: OnDataChangedListener?) {
        mOnDataChangedListener = listener
    }

    fun setSelectedList(vararg poses: Int) {
        val set: MutableSet<Int> = HashSet()
        for (pos in poses) {
            set.add(pos)
        }
        setSelectedList(set)
    }

    fun setSelectedList(set: Set<Int>?) {
        preCheckedList.clear()
        if (set != null) {
            preCheckedList.addAll(set)
        }
        notifyDataChanged()
    }

    val count: Int
        get() = mTagDatas?.size ?: 0

    fun notifyDataChanged() {
        if (mOnDataChangedListener != null) mOnDataChangedListener!!.onChanged()
    }

    fun getItem(position: Int): T {
        return mTagDatas!![position]
    }

    abstract fun getView(parent: FlowLayout?, position: Int, t: T): View?
    fun onSelected(position: Int, view: View?) {
        Log.d("zhy", "onSelected $position")
    }

    fun unSelected(position: Int, view: View?) {
        Log.d("zhy", "unSelected $position")
    }

    fun setSelected(position: Int, t: T): Boolean {
        return false
    }

    interface OnDataChangedListener {
        fun onChanged()
    }
}