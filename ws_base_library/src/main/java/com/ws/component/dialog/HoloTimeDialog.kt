package com.ws.component.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.TimePicker
import com.ws.base.R
import com.ws.support.utils.ScreenUtils.Companion.getScreenWidth
import com.ws.support.utils.SharePreferUtil.getScreenOrientation
import com.ws.support.utils.StringUtils.isNotEmptyWithNull

/**
 * 描述信息
 *
 * @author ws
 * @date 12/21/20 7:11 PM
 * 修改人：ws
 */
class HoloTimeDialog(private val context: Context) {
    var listener: OnHoloDialogClickListener? = null
    private lateinit var dialog: Dialog
    private lateinit var tvTitle: TextView
    private lateinit var tvMessage: TextView
    private lateinit var tvCancel: TextView
    private lateinit var tvOk: TextView
    private lateinit var timePicker: TimePicker
    @SuppressLint("NewApi", "InflateParams")
    fun builder(): HoloTimeDialog {
        // 获取Dialog布局
        val view = LayoutInflater.from(context).inflate(
                R.layout.dialog_holo_time, null)
        tvTitle = view.findViewById(R.id.tv_title)
        tvMessage = view.findViewById(R.id.tv_message)
        tvCancel = view.findViewById(R.id.tv_cancel)
        tvOk = view.findViewById(R.id.tv_ok)
        timePicker = view.findViewById(R.id.picker)
        timePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS) //设置点击事件不弹键盘
        timePicker.setIs24HourView(true) //设置时间显示为24小时
        tvCancel.setOnClickListener(View.OnClickListener { view1: View? ->
            if (listener != null) listener!!.onCancelClick()
            dialog.dismiss()
        })
        tvOk.setOnClickListener({ view12: View? -> if (listener != null) listener!!.onOKClick(dialog, timePicker.getCurrentHour(), timePicker.getCurrentMinute()) })
        // 定义Dialog布局和参数
        dialog = Dialog(context, R.style.HoloDialogStyle)
        dialog.setContentView(view)
        val dialogWindow = dialog.window
        dialogWindow!!.setGravity(Gravity.CENTER)
        val lp = dialogWindow.attributes
        if (getScreenOrientation()) {
            lp.width = getScreenWidth(context) / 3 * 2
        } else {
            lp.width = getScreenWidth(context) / 3
        }
        dialogWindow.attributes = lp
        return this
    }

    @SuppressLint("NewApi")
    fun setTime(hour: Int, min: Int): HoloTimeDialog {
        if (hour == -1 || min == -1) return this
        timePicker.currentHour = hour
        timePicker.currentMinute = min
        return this
    }

    @SuppressLint("NewApi")
    fun setMaxTime(hour: Int, min: Int): HoloTimeDialog {
        if (hour == -1 || min == -1) return this
        val systemResources = Resources.getSystem()
        val hourNumberPickerId = systemResources.getIdentifier("hour", "id", "android")
        val minuteNumberPickerId = systemResources.getIdentifier("minute", "id", "android")
        val hourNumberPicker = timePicker!!.findViewById<View>(hourNumberPickerId) as NumberPicker
        val minuteNumberPicker = timePicker!!.findViewById<View>(minuteNumberPickerId) as NumberPicker
        hourNumberPicker.maxValue = hour
        minuteNumberPicker.maxValue = min
        return this
    }

    @SuppressLint("NewApi")
    fun setMinTime(hour: Int, min: Int): HoloTimeDialog {
        if (hour == -1 || min == -1) return this
        val systemResources = Resources.getSystem()
        val hourNumberPickerId = systemResources.getIdentifier("hour", "id", "android")
        val minuteNumberPickerId = systemResources.getIdentifier("minute", "id", "android")
        val hourNumberPicker = timePicker.findViewById<View>(hourNumberPickerId) as NumberPicker
        val minuteNumberPicker = timePicker.findViewById<View>(minuteNumberPickerId) as NumberPicker
        hourNumberPicker.minValue = hour
        minuteNumberPicker.minValue = min
        return this
    }

    fun setCancelable(cancel: Boolean): HoloTimeDialog {
        dialog.setCancelable(cancel)
        return this
    }

    fun setCanceledOnTouchOutside(cancel: Boolean): HoloTimeDialog {
        dialog.setCanceledOnTouchOutside(cancel)
        return this
    }

    fun setTitle(title: String?): HoloTimeDialog {
        if (isNotEmptyWithNull(title)) {
            tvTitle.text = title
        }
        return this
    }

    /* public HoloTimeDialog setMessage(String message)
    {
        if (StringUtils.isNotEmptyWithNull(message))
        {
            tvMessage.setText(message);
        }
        return this;
    }*/
    fun setOneButton(btnStr: String?): HoloTimeDialog {
        if (isNotEmptyWithNull(btnStr)) {
            tvOk.text = btnStr
        }
        tvCancel.visibility = View.GONE
        return this
    }

    fun setOneButton(): HoloTimeDialog {
        tvCancel.visibility = View.GONE
        return this
    }

    fun setOnClickListener(l: OnHoloDialogClickListener?): HoloTimeDialog {
        listener = l
        return this
    }

    fun show() {
        dialog.show()
    }

    interface OnHoloDialogClickListener {
        fun onOKClick(dialog: Dialog?, hour: Int, min: Int)
        fun onCancelClick()
    }
}