package com.ws.support.service

import android.annotation.SuppressLint
import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.os.Environment
import com.orhanobut.logger.Logger

/**
 * 版本验证服务,升级
 *
 * @author ws
 */
@Suppress("DEPRECATION")
class VersionCheckService : IntentService("VersionCheckService") {
    var type = 0
    @SuppressLint("WrongConstant")
    override fun onHandleIntent(intent: Intent?) {
        /*测试安装网易新闻*/
        //downloadAPK("http://3g.163.com/links/4636");
        if (intent != null) {
            Logger.i("启动版本检查")
            val action = intent.action
            type = intent.getIntExtra("type", -1)
            if (ACTION == action && type > 0) {
                update()
            }
        }
    }

    private fun update() {
//        HttpHelper.subscribe(createService(_ApiUrl::class.java).GetCurrentVersion(), object : BaseObserver<ResultTO>(this, false) {
//            override fun onSuccess(o: ResultTO) {
//                if (o.isSucc()) {
//                    try {
//                        val result = o.toJsonObject()
//                        val versionCode = result!!.optInt("verNo")
//                        if (BuildConfig.VERSION_CODE < versionCode) {
//                            val downloadUrl = result.optString("downloadUrl")
//                            EventBus.getDefault().postSticky(NewVersionEvent.Companion.hasNew(true, type))
//                            downloadAPK(downloadUrl)
//                        } else {
//                            val file = File(path_apk)
//                            if (file.exists()) {
//                                file.delete() //删除旧的APK
//                            }
//                            EventBus.getDefault().postSticky(NewVersionEvent.Companion.hasNew(false, type))
//                        }
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//                }
//            }
//
//            override fun onFailed(e: Throwable?) {
//                Logger.e("获取版本", e)
//            }
//        })
    }

    private fun downloadAPK(downloadUrl: String) {
//        Thread {
//            EventBus.getDefault().postSticky(NewVersionEvent.Companion.downLoadStart(type))
//            //OK设置请求超时时间，读取超时时间
//            val client = OkHttpClient.Builder()
//                    .connectTimeout(15, TimeUnit.SECONDS)
//                    .readTimeout(15, TimeUnit.SECONDS)
//                    .build()
//            val retrofit = Retrofit.Builder().baseUrl("")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .client(client)
//                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                    .build()
//            val apiService = retrofit.create(_ApiUrl::class.java)
//            //String url = "http://oa.tjpc.com.cn:8099/Content/App/android/heinqi_OA-release.apk";
//            val observable = apiService.download(downloadUrl)
//            observable!!.subscribeOn(Schedulers.io())
//                    .subscribe(object : Observer<ResponseBody> {
//                        override fun onSubscribe(d: Disposable) {}
//                        override fun onNext(responseBody: ResponseBody) {
//                            var inputStream: InputStream? = null
//                            var total: Long = 0
//                            val responseLength: Long
//                            var fos: FileOutputStream? = null
//                            try {
//                                val buf = ByteArray(2048)
//                                var len: Int
//                                responseLength = responseBody.contentLength()
//                                inputStream = responseBody.byteStream()
//                                val dir = File(mEnvironmentDirectory)
//                                if (!dir.exists()) {
//                                    dir.mkdirs()
//                                }
//                                val file = File(path_apk)
//                                if (file.exists()) {
//                                    file.delete()
//                                } else {
//                                    file.createNewFile()
//                                }
//                                fos = FileOutputStream(file)
//                                var progress = 0
//                                var lastProgress: Int
//                                val startTime = System.currentTimeMillis() // 开始下载时获取开始时间
//                                while (inputStream.read(buf).also { len = it } != -1) {
//                                    fos.write(buf, 0, len)
//                                    total += len.toLong()
//                                    lastProgress = progress
//                                    progress = (total * 100 / responseLength).toInt()
//                                    val curTime = System.currentTimeMillis()
//                                    var usedTime = (curTime - startTime) / 1000
//                                    if (usedTime == 0L) {
//                                        usedTime = 1
//                                    }
//                                    val speed = total / usedTime // 平均每秒下载速度
//                                    // 如果进度与之前进度相等，则不更新，如果更新太频繁，则会造成界面卡顿
//                                    if (progress > 0 && progress != lastProgress) {
//                                        Logger.e("VersionCheckService process=$progress")
//                                    }
//                                }
//                                fos.flush()
//                                /*发送到主页*/EventBus.getDefault().postSticky(NewVersionEvent.Companion.downLoadFinish(type))
//                            } catch (e: Exception) {
//                                Logger.e("下载新版本", e)
//                                EventBus.getDefault().postSticky(NewVersionEvent.Companion.downLoadError(type))
//                            } finally {
//                                try {
//                                    fos?.close()
//                                    inputStream?.close()
//                                } catch (e: Exception) {
//                                    EventBus.getDefault().postSticky(NewVersionEvent.Companion.downLoadError(type))
//                                    e.printStackTrace()
//                                }
//                            }
//                        }
//
//                        override fun onError(e: Throwable) {
//                            Logger.e("下载新版本", e)
//                            EventBus.getDefault().postSticky(NewVersionEvent.Companion.downLoadError(type))
//                        }
//
//                        override fun onComplete() {}
//                    })
//        }.start()
    }

    companion object {
        private const val ACTION = "com.heinqi.im.service.action.version"
        var mEnvironmentDirectory = Environment.getExternalStorageDirectory().path
        var path_apk = mEnvironmentDirectory + "/htIM.apk"

        //上报roomId
        fun startAction(context: Context, type: Int) {
            val intent = Intent(context, VersionCheckService::class.java)
            intent.action = ACTION
            intent.putExtra("type", type)
            context.startService(intent)
        }
    }
}