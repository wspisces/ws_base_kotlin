package com.ws.component.dialog

import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.ws.base.R
import com.ws.component.ClearEditText
import com.ws.component.InputFilterFloatMinMax
import com.ws.support.utils.ScreenUtils.Companion.getScreenWidth
import com.ws.support.utils.SharePreferUtil.getScreenOrientation
import com.ws.support.utils.StringUtils.isNotEmptyWithNull
import com.ws.support.utils.ToastUtils.warn

/**
 * 描述信息
 *
 * @author ws
 * @date 12/21/20 7:11 PM
 * 修改人：ws
 */
class HoloDialog(private val context: Context) {
    var listener: OnHoloDialogClickListener? = null
    var listener2: OnHoloDialogClickListener2? = null
    private lateinit var dialog: Dialog
    private lateinit var tvTitle: TextView
    private lateinit var tvMessage: TextView
    private lateinit var tvCancel: TextView
    private lateinit var tvOk: TextView
    private lateinit var etName: ClearEditText
    private lateinit var etAmount: ClearEditText
    fun builder(): HoloDialog {
        // 获取Dialog布局
        val view = LayoutInflater.from(context).inflate(
                R.layout.dialog_holo, null)
        // 定义Dialog布局和参数
        dialog = Dialog(context, R.style.HoloDialogStyle)
        dialog.setContentView(view)
        tvTitle = view.findViewById(R.id.tv_title)
        tvMessage = view.findViewById(R.id.tv_message)
        tvCancel = view.findViewById(R.id.tv_cancel)
        tvOk = view.findViewById(R.id.tv_ok)
        tvCancel.setOnClickListener({ view1: View? ->
            if (listener != null) listener!!.onCancelClick()
            dialog.dismiss()
        })
        tvOk.setOnClickListener({ view12: View? -> if (listener != null) listener!!.onOKClick(dialog) })
        val dialogWindow = dialog.window
        dialogWindow?.setGravity(Gravity.CENTER)
        val lp = dialogWindow?.attributes
        if (getScreenOrientation()) {
            lp?.width = getScreenWidth(context) / 3 * 2
        } else {
            lp?.width = getScreenWidth(context) / 3
        }
        dialogWindow?.attributes = lp
        return this
    }

    fun builderCreateCategory(): HoloDialog {
        // 获取Dialog布局
        val view = LayoutInflater.from(context).inflate(
                R.layout.dialog_holo_create_category, null)
        tvTitle = view.findViewById(R.id.tv_title)
        tvMessage = view.findViewById(R.id.tv_message)
        tvCancel = view.findViewById(R.id.tv_cancel)
        tvOk = view.findViewById(R.id.tv_ok)
        etName = view.findViewById(R.id.et_name)
        etAmount = view.findViewById(R.id.et_amount)
        etAmount.setFilters(arrayOf<InputFilter>(InputFilterFloatMinMax(0.01.toFloat(), 9999f)))
        tvCancel.setOnClickListener(View.OnClickListener { view1: View? ->
            if (listener2 != null) listener2!!.onCancelClick()
            dialog.dismiss()
        })
        tvOk.setOnClickListener({ view12: View? ->
            if (etName.text?.length == 0) {
                warn("请输入菜名")
                return@setOnClickListener
            }
            if (etAmount.text?.length == 0) {
                warn("请输入金额")
                return@setOnClickListener
            }
            if (listener2 != null) listener2!!.onOKClick(dialog, etName.getText().toString(), etAmount.getText().toString())
        })
        // 定义Dialog布局和参数
        dialog = Dialog(context, R.style.HoloDialogStyle)
        dialog.setContentView(view)
        val dialogWindow = dialog.window
        dialogWindow?.setGravity(Gravity.CENTER)
        val lp = dialogWindow?.attributes
        if (getScreenOrientation()) {
            lp?.width = getScreenWidth(context) / 4 * 3
        } else {
            lp?.width = getScreenWidth(context) / 2
        }
        dialogWindow?.attributes = lp
        return this
    }

    /*   public HoloDialog builderInputAmount()
    {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_holo_create_category, null);
        tvTitle = view.findViewById(R.id.tv_title);
        tvMessage = view.findViewById(R.id.tv_message);
        tvCancel = view.findViewById(R.id.tv_cancel);
        tvOk = view.findViewById(R.id.tv_ok);
        etName = view.findViewById(R.id.et_name);
        etAmount = view.findViewById(R.id.et_amount);
        etAmount.setFilters(new InputFilter[]{new InputFilterFloatMinMax((float) 0.01, 9999)});

        tvCancel.setOnClickListener(view1 ->
        {
            if (listener2 != null)
                listener2.onCancelClick();
            dialog.dismiss();
        });
        tvOk.setOnClickListener(view12 ->
        {
            if (etName.getText().length() == 0)
            {
                MyToastUtil.show("请输入菜名");
                return;
            }

            if (etAmount.getText().length() == 0)
            {
                MyToastUtil.show("请输入金额");
                return;
            }
            if (listener2 != null)
                listener2.onOKClick(dialog, etName.getText().toString(), etAmount.getText().toString());
        });
        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.HoloDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        if (SharePreferUtil.getScreenOrientation())
        {
            lp.width = ScreenUtils.getScreenWidth(context) / 2;
        } else
        {
            lp.width = ScreenUtils.getScreenWidth(context) / 2;
        }
        dialogWindow.setAttributes(lp);

        return this;
    }*/
    fun setCategoryInfo(name: String, price: String?): HoloDialog {
        if (isNotEmptyWithNull(name)) {
            etName.setText(name)
            etName.setSelection(name.length)
        }
        if (isNotEmptyWithNull(price)) {
            etAmount.setText(price)
        }
        return this
    }

    fun setCancelable(cancel: Boolean): HoloDialog {
        dialog.setCancelable(cancel)
        return this
    }

    fun setCanceledOnTouchOutside(cancel: Boolean): HoloDialog {
        dialog.setCanceledOnTouchOutside(cancel)
        return this
    }

    fun setTitle(title: String?): HoloDialog {
        if (isNotEmptyWithNull(title)) {
            tvTitle.text = title
        }
        return this
    }

    fun setMessage(message: String?): HoloDialog {
        if (isNotEmptyWithNull(message)) {
            tvMessage.text = message
        }
        return this
    }

    fun setOneButton(btnStr: String?): HoloDialog {
        if (isNotEmptyWithNull(btnStr)) {
            tvOk.text = btnStr
        }
        tvCancel.visibility = View.GONE
        return this
    }

    fun setOneButton(): HoloDialog {
        tvCancel.visibility = View.GONE
        return this
    }

    fun setOnClickListener(l: OnHoloDialogClickListener?): HoloDialog {
        listener = l
        return this
    }

    fun setOnClickListener2(l: OnHoloDialogClickListener2?): HoloDialog {
        listener2 = l
        return this
    }

    fun show() {
        // setSheetItems();
        dialog.show()
    }

    /**
     * edittext只能输入数值的时候做最大最小的限制
     */
    private fun setRegion(edit: EditText, MIN_MARK: Float, MAX_MARK: Float) {
        edit.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                var s = s
                if (start > 1) {
                    if (MIN_MARK != -1f && MAX_MARK != -1f) {
                        val num = s.toString().toDouble()
                        if (num > MAX_MARK) {
                            s = MAX_MARK.toString()
                            edit.setText(s)
                        } else if (num < MIN_MARK) {
                            s = MIN_MARK.toString()
                            edit.setText(s)
                        }
                        edit.setSelection(s.length)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s != null && s.length > 0) {
                    if (MIN_MARK != -1f && MAX_MARK != -1f) {
                        var markVal = 0.0
                        markVal = try {
                            s.toString().toDouble()
                        } catch (e: NumberFormatException) {
                            0.0
                        }
                        if (markVal > MAX_MARK) {
                            edit.setText(MAX_MARK.toString())
                            edit.setSelection(MAX_MARK.toString().length)
                        }
                        return
                    }
                }
            }
        })
    }

    interface OnHoloDialogClickListener {
        fun onOKClick(dialog: Dialog?)
        fun onCancelClick()
    }

    interface OnHoloDialogClickListener2 {
        fun onOKClick(dialog: Dialog?, name: String?, amount: String?)
        fun onCancelClick()
    }
}