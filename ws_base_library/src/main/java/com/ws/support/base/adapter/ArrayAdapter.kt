package com.ws.support.base.adapter

import android.content.Context
import android.view.LayoutInflater
import android.widget.BaseAdapter
import java.util.*

abstract class ArrayAdapter<T>(ctx: Context, l: List<T>?) : BaseAdapter() {
    protected var mObjects: MutableList<T>
    protected var mInflater: LayoutInflater
    protected var mContext: Context
    override fun getCount(): Int {
        return mObjects.size
    }

    override fun getItem(position: Int): T {
        return mObjects[position]
    }

    fun getItemExist(tiem: T): Int {
        return mObjects.indexOf(tiem)
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    fun add(item: T) {
        mObjects.add(item)
    }

    fun replace(newObjects: ArrayList<T>?) {
        var newObjects = newObjects
        if (newObjects == null) newObjects = ArrayList()
        mObjects = newObjects
    }

    fun remove(position: Int) {
        mObjects.removeAt(position)
        notifyDataSetChanged()
    }
    /**
     * Adds the specified items at the end of the array.
     *
     * @param items
     * The items to add at the end of the array.
     */
    // public void addAll(T... items) {
    // List<T> values = this.mObjects;
    // for (T item : items) {
    // values.add(item);
    // }
    // this.mObjects = values;
    // }
    /**
     *
     * @param collection
     */
    fun addAll(collection: Collection<T>?) {
        if (collection != null) {
            mObjects.addAll(collection)
            notifyDataSetChanged()
        }
    }

    /**
     * Remove all elements from the list.
     */
    fun clear() {
        mObjects.clear()
        notifyDataSetChanged()
    }

    val all: List<T>
        get() = mObjects

    init {
        mObjects = (l ?: ArrayList()) as MutableList<T>
        mInflater = ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mContext = ctx
    }
}