package com.ws.component

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.ws.base.R

/**
 * 空布局界面
 *
 * @author ws
 * 2020/9/4 16:08
 * 修改人：ws
 */
class EmptyView : ConstraintLayout {
    var listener:OnClickListener? = null
    private lateinit var btn: Button
    private lateinit var iv: ImageView
    private lateinit var tv: TextView

    constructor(context: Context) : super(context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate(R.layout.empty_view, this, true)
        iv = v.findViewById(R.id.empty_iv)
        tv = v.findViewById(R.id.empty_tv)
        btn = v.findViewById(R.id.empty_btn)
        btn.setOnClickListener(OnClickListener { view: View? ->
                listener?.onClick()
        })
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    @JvmName("setClickListener")
    fun setListener(listener: OnClickListener?) {
        this.listener = listener
        btn.visibility = VISIBLE
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate(R.layout.empty_view, this, true)
        iv = v.findViewById(R.id.empty_iv)
        tv = v.findViewById(R.id.empty_tv)
        btn = v.findViewById(R.id.empty_btn)
        btn.setOnClickListener(OnClickListener { view: View? ->
            if (listener != null) {
                listener!!.onClick()
            }
        })
        val a = context.obtainStyledAttributes(attrs, R.styleable.EmptyView)
        val msg = a.getString(R.styleable.EmptyView_msg)
        if (TextUtils.isEmpty(msg)) {
            tv.setVisibility(GONE)
        } else {
            tv.setText(msg)
        }
        val image = a.getResourceId(R.styleable.MenuRow_icon, R.mipmap.no_data)
        iv.setImageResource(image)
        a.recycle()
    }

    fun setMessage(msg: String?) {
        if (TextUtils.isEmpty(msg)) {
            tv.visibility = GONE
        } else {
            tv.text = msg
            tv.visibility = VISIBLE
        }
    }

    @SuppressLint("ResourceType")
    fun setImage(@IdRes resource: Int) {
        iv.setImageResource(resource)
    }

    interface OnClickListener {
        fun onClick()
    }
}