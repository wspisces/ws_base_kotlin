package com.ws.support.base.listener

import android.view.View

class SimpleActivityOperationCallback : IActivityOperationCallback {
    override fun hideKeyboard(view: View?) {}
    override fun showKeyboard(view: View?) {}
    override fun hideCoverView(): Boolean {
        return false
    }

    override fun showCoverView() {}
    override fun hideRequestView(): Boolean {
        return false
    }

    override fun showRequestView() {}
    override fun showRequestView(prompt: String?) {}
    override fun hideNetWorkErrorView() {}
    override fun showNetWorkErrorView(action: Runnable?) {}
    override fun showCoverContentView() {}
    override fun hideCoverContentView(): Boolean {
        return false
    }

    override fun initCoverContentView(view: View?) {}
    override fun goBack() {}
    override fun exitApp() {}
}