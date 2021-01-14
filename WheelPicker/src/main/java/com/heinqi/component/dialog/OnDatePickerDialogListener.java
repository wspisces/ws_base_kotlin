package com.heinqi.component.dialog;

import java.util.Calendar;

/**
 * 时间选择弹窗回调
 *
 * @author ws
 * 2020/6/29 18:33
 * 修改人：ws
 */
public interface OnDatePickerDialogListener {
    void onSelected(Calendar calendar);
}
