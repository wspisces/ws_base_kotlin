package com.ws.base

import com.alibaba.android.arouter.launcher.ARouter
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.ws.support.base.BaseApplication
import org.acra.ReportField
import org.acra.ReportingInteractionMode
import org.acra.annotation.ReportsCrashes

/**
 * MyApplication.class
 * 应用App页面
 */
@ReportsCrashes(
        mailTo = "wspisces@126.com",
        mode = ReportingInteractionMode.DIALOG,
        customReportContent = [
            ReportField.APP_VERSION_NAME,
            ReportField.ANDROID_VERSION,
            ReportField.PHONE_MODEL,
            ReportField.CUSTOM_DATA,
            ReportField.BRAND,
            ReportField.STACK_TRACE,
            ReportField.LOGCAT,
            ReportField.USER_COMMENT],
        )
class MyApplication : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        //retrofit = HttpHelper.initRetrofit("");
        /*try {
            mDbManager = DbUtils.create("hengtong_db", 1);
        } catch (Exception e) {
            Logger.e(e, "数据库初始化");
        }*/

        if (BuildConfig.isDebug) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog()     // 打印日志
            ARouter.openDebug()   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this) // 尽可能早，推荐在Application中初始化

        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false) // (Optional) Whether to show thread info or not. Default true
                .methodCount(0) // (Optional) How many method line to show. Default 2
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

}