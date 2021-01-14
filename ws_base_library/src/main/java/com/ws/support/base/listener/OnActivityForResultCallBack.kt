package com.ws.support.base.listener

import android.content.Intent

/**
 * OnActivityForResultCallBack.class
 * 页面跳转回调监听类
 * @author Johnny.xu
 * time:2018/11/30
 */
abstract class OnActivityForResultCallBack {
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}
    fun onActivityResultOk(data: Intent?) {}
}