package com.ws.base

import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.ws.support.base.BaseApplication

/**
 * MyApplication.class
 * 应用App页面
 */
class  MyApplication : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        //retrofit = HttpHelper.initRetrofit("");
        /*try {
            mDbManager = DbUtils.create("hengtong_db", 1);
        } catch (Exception e) {
            Logger.e(e, "数据库初始化");
        }*/
        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false) // (Optional) Whether to show thread info or not. Default true
                .methodCount(0) // (Optional) How many method line to show. Default 2
                .methodOffset(0) // (Optional) Hides internal method calls up to offset. Default 5
                //.logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag("wstag") // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return true
            }
        })
        /* FormatStrategy formatStrategy = CsvFormatStrategy.newBuilder()
                .tag("custom")
                .build();
        Logger.addLogAdapter(new DiskLogAdapter(formatStrategy));*/
        //CrashReport.initCrashReport(getApplicationContext(), "f61fdd454e", isdebug);
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}