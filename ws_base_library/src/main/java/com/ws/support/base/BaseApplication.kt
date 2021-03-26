package com.ws.support.base

import android.annotation.SuppressLint
import android.content.Context
import androidx.multidex.MultiDexApplication
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.ws.base.BuildConfig
import com.ws.support.http.HttpHelper
import com.ws.support.utils.MyFileUtils
import es.dmoral.toasty.Toasty
import retrofit2.Retrofit

/**
 * BaseApplication.class
 * app应用基础页面
 */
@SuppressLint("NonConstantResourceId")
open class BaseApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        mInstance = this
        mContext = applicationContext
        MyFileUtils.initPath()

        retrofit = HttpHelper.initRetrofit(BuildConfig.baseUrl)
        Toasty.Config.getInstance()
                .tintIcon(true) // optional (apply textColor also to the icon)
                //.setToastTypeface(@NonNull Typeface typeface) // optional
                //.setTextSize(int sizeInSp) // optional
                //.allowQueue(boolean allowQueue) // optional (prevents several Toastys from queuing)
                .apply() // required

        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false) // (Optional) Whether to show thread info or not. Default true
                .methodCount(2) // (Optional) How many method line to show. Default 2
                .methodOffset(0) // (Optional) Hides internal method calls up to offset. Default 5
                //.logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag("base_library") // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return true
            }
        })
    }

    companion object {
        lateinit  var retrofit: Retrofit
        @SuppressLint("StaticFieldLeak")
        private lateinit var mInstance: BaseApplication
        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context
        @JvmStatic fun getInstance(): BaseApplication {
            return mInstance
        }
    }
}