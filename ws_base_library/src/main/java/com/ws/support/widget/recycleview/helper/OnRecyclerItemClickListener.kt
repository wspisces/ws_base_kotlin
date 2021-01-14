package com.ws.support.widget.recycleview.helper

import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener

/**
 * Created by Administrator on 2016/4/14.
 */
class OnRecyclerItemClickListener(private val recyclerView: RecyclerView) : OnItemTouchListener {
    private val mGestureDetector: GestureDetectorCompat
    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        mGestureDetector.onTouchEvent(e)
        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        mGestureDetector.onTouchEvent(e)
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
    private inner class ItemTouchHelperGestureListener : SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            val child = recyclerView.findChildViewUnder(e.x, e.y)
            if (child != null) {
                val vh = recyclerView.getChildViewHolder(child)
                onItemClick(vh)
            }
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            val child = recyclerView.findChildViewUnder(e.x, e.y)
            if (child != null) {
                val vh = recyclerView.getChildViewHolder(child)
                onLongClick(vh)
            }
        }
    }

    fun onLongClick(vh: RecyclerView.ViewHolder?) {}
    fun onItemClick(vh: RecyclerView.ViewHolder?) {}

    init {
        mGestureDetector = GestureDetectorCompat(recyclerView.context, ItemTouchHelperGestureListener())
    }
}