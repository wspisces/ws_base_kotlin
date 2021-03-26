package com.ws.support.base.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ws.support.base.mvvm.TUtil
import com.ws.support.base.mvvm.repository.BaseRepository
import com.ws.support.extension.LoadState


/**
 * 描述信息
 * @author ws
 * @date 3/24/21 3:54 PM
 * 修改人：ws
 */
abstract class BaseViewModel<T : BaseRepository?> : ViewModel() {
    //    val isLoading: MutableLiveData<Boolean> by lazy {
//        MutableLiveData<Boolean>().apply { value = false }
//    }
    val loadState: MutableLiveData<LoadState> by lazy {
        MutableLiveData()
    }
    val error: MutableLiveData<String> by lazy {
        MutableLiveData()
    }
    protected var repository: T? = TUtil.getInstance(this, 0)

}