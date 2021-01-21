package com.ws.support.utils

import android.os.Environment
import android.os.StatFs
import android.util.Log
import java.io.*

/**
 * SD卡相关的辅助类
 *
 * @author zhy
 */
@Suppress("DEPRECATION")
class SDCardUtils private constructor() {
    companion object {
        /**
         * 判断SDCard是否可用
         *
         * @return
         */
        fun isSDCardEnable(): Boolean {
            return Environment.getExternalStorageState() ==
                    Environment.MEDIA_MOUNTED
        }

        /**
         * 获取SD卡路径
         */
        fun getSDCardPath(): String {
            return Environment.getExternalStorageDirectory().absolutePath + File.separator
        }

        /**
         * 获取sd卡路径
         * 双sd卡时，获得的是外置sd卡
         */
        fun getSDCardPath2(): String {
            val cmd = "cat /proc/mounts"
            val run = Runtime.getRuntime() // 返回与当前 Java 应用程序相关的运行时对象
            var `in`: BufferedInputStream? = null
            var inBr: BufferedReader? = null
            try {
                val p = run.exec(cmd) // 启动另一个进程来执行命令
                `in` = BufferedInputStream(p.inputStream)
                inBr = BufferedReader(InputStreamReader(`in`))
                var lineStr: String
                while (inBr.readLine().also { lineStr = it } != null) {
                    // 获得命令执行后在控制台的输出信息
                    Log.i("getSDCardPath", lineStr)
                    if (lineStr.contains("sdcard")
                            && lineStr.contains(".android_secure")) {
                        val strArray = lineStr.split(" ".toRegex()).toTypedArray()
                        if (strArray.size >= 5) {
                            return strArray[1].replace("/.android_secure",
                                    "")
                        }
                    }
                    // 检查命令是否执行失败。
                    if (p.waitFor() != 0 && p.exitValue() == 1) {
                        // p.exitValue()==0表示正常结束，1：非正常结束
                        Log.e("getSDCardPath", "命令执行失败!")
                    }
                }
            } catch (e: Exception) {
                Log.e("getSDCardPath", e.toString())
                //return Environment.getExternalStorageDirectory().getPath();
            } finally {
                try {
                    `in`?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                try {
                    inBr?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return Environment.getExternalStorageDirectory().path
        }

        /**
         * 获取SD卡的剩余容量 单位byte
         */
        fun getSDCardAllSize(): Long {
            if (isSDCardEnable()) {
                val stat = StatFs(getSDCardPath())
                // 获取空闲的数据块的数量
                val availableBlocks = stat.availableBlocks.toLong() - 4
                // 获取单个数据块的大小（byte）
                val freeBlocks = stat.availableBlocks.toLong()
                return freeBlocks * availableBlocks
            }
            return 0
        }

        /**
         * 获取指定路径所在空间的剩余可用容量字节数，单位byte
         *
         * @return 容量字节 SDCard可用空间，内部存储可用空间
         */
        fun getFreeBytes(path: String): Long {
            // 如果是sd卡的下的路径，则获取sd卡可用容量
            var filePath = path
            filePath = if (filePath.startsWith(getSDCardPath())) {
                getSDCardPath()
            } else { // 如果是内部存储的路径，则获取内存存储的可用容量
                Environment.getDataDirectory().absolutePath
            }
            val stat = StatFs(filePath)
            val availableBlocks = stat.availableBlocks.toLong() - 4
            return stat.blockSize * availableBlocks
        }

        /**
         * 获取系统存储路径
         */
        fun getRootDirectoryPath(): String {
            return Environment.getRootDirectory().absolutePath
        }
    }

    init {
        /* cannot be instantiated */
        throw UnsupportedOperationException("cannot be instantiated")
    }
}