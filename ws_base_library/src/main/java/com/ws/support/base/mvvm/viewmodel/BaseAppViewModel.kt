package com.ws.support.base.mvvm.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ws.support.base.mvvm.TUtil
import com.ws.support.base.mvvm.repository.BaseRepository


/**
 * 描述信息
 * @author ws
 * @date 3/24/21 3:54 PM
 * 修改人：ws
 */
abstract class BaseAppViewModel<T : BaseRepository?>(@NonNull application: Application) : AndroidViewModel(application) {
    @SuppressLint("StaticFieldLeak")
    protected var context: Context = application
    protected var repository: T?

    val isLoading: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().apply { value = false }
    }

    init {
        repository = TUtil.getInstance(this, 0)
    }
}