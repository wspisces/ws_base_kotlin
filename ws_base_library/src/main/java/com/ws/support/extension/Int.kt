package com.ws.support.extension

import android.widget.Toast
import com.ws.support.base.BaseApplication


/**
 * Int扩展类
 * @author ws
 * @date 1/19/21 3:19 PM
 * 修改人：ws
 */
fun Int.showToast(duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(BaseApplication.mContext, this, duration).show()
}

