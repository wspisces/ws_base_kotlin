package com.ws.support.base

import androidx.multidex.MultiDexApplication
import com.ws.support.utils.MyFileUtils
import es.dmoral.toasty.Toasty
import org.xutils.DbManager
import org.xutils.x
import retrofit2.Retrofit

/**
 * BaseApplication.class
 * app应用基础页面
 *
 * @author Johnny.xu
 * time:2018/11/26
 */
open class BaseApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        mInstance = this
        x.Ext.init(this)
        MyFileUtils.initPath()
        //监听App状态
        registerActivityLifecycleCallbacks(AppLifecycleCallback())
        Toasty.Config.getInstance()
                .tintIcon(true) // optional (apply textColor also to the icon)
                //.setToastTypeface(@NonNull Typeface typeface) // optional
                //.setTextSize(int sizeInSp) // optional
                //.allowQueue(boolean allowQueue) // optional (prevents several Toastys from queuing)
                .apply() // required
    }

    companion object {
        var mDbManager: DbManager? = null
        var retrofit: Retrofit? = null
        private var mInstance: BaseApplication? = null
        fun getInstance(): BaseApplication? {
            return mInstance
        }
    }
}