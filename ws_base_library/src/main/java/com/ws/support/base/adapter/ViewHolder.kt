package com.ws.support.base.adapter

import android.util.SparseArray
import android.view.View

/**
 * @author allen
 * @email jaylong1302@163.com
 * @date 2014-1-15 上午10:31:28
 * @company 富媒科技
 * @version 1.0
 * @description
 */
object ViewHolder {
    // I added a generic return type to reduce the casting noise in client
    // code
    operator fun <T : View?> get(view: View, id: Int): T? {
        var viewHolder = view.tag as SparseArray<View?>
        if (viewHolder == null) {
            viewHolder = SparseArray()
            view.tag = viewHolder
        }
        var childView = viewHolder[id]
        if (childView == null) {
            childView = view.findViewById(id)
            viewHolder.put(id, childView)
        }
        return childView as T?
    }
}