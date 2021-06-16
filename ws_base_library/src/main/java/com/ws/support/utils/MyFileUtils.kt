@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")

package com.ws.support.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.os.StatFs
import android.util.Base64
import com.orhanobut.logger.Logger
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*

/**
 *
 * Title:FileUtils.java
 *
 * Description:文件管理类
 */
object MyFileUtils {
    var AppCode = "heniqi"
    private const val Log = "log"
    private const val File = "file"
    private const val Img = "img"
    private const val Video = "video"
    private var mEnvironmentDirectory = Environment.getExternalStorageDirectory().path + "/" + AppCode
    fun initPath() {
        mEnvironmentDirectory = Environment.getExternalStorageDirectory().path + "/" + AppCode
    }

    fun getRootDirectory(): String {
        try {
            val file = File("/")
            if (file.isDirectory) {
                val files = file.listFiles()
                for (i in files.indices) {
                    if (null != files[i]) {
                        Logger.i("path=" + files[i]!!.path)
                    }
                }
            } else {
                Logger.i("path=" + "/")
            }
        } catch (e: Exception) {
            Logger.i("file is null")
        }
        return ""
    }

    /**
     * 获取图片Base64字节
     * @param imagePath
     * @return
     */
    fun getImageBase64Str(imagePath: String?): String {
        var str = ""
        try {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            str = Base64.encodeToString(Bitmap2Bytes(bitmap), Base64.DEFAULT)
            if (!bitmap.isRecycled) bitmap.recycle()
        } catch (e: Exception) {
        }
        return str
    }

    fun Bitmap2Bytes(bm: Bitmap): ByteArray {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 80, baos)
        return baos.toByteArray()
    }

    /**
     * 通过文件路径获取名称
     */
    fun getFileNameByPath(path: String?): String {
        try {
            if (!StringUtils.isEmpty(path)) {
                val file = File(path)
                if (file.exists()) {
                    return file.name
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
    //	/**
    //	 * 获取当前时间日期
    //	 *
    //	 * @param format
    //	 *            自定义格式,例：yyyy-MM-dd hh:mm:ss
    //	 * @return
    //	 */
    //	public static String getFormatTime(String format) {
    //		Date date = new Date();
    //		SimpleDateFormat df = new SimpleDateFormat(format);
    //		String time = df.format(date);
    //		return time;
    //	}
    /**
     * 获取日志文件地址
     */
    fun getLogDir(): String {
        return getDir(Log)
    }

    /**
     * 获取文件地址
     */
    fun getFileDir(): String {
        return getDir(File)
    }

    /**
     * 获取视频文件地址
     */
    fun getVideoDir(): String {
        return getDir(Video)
    }

    /**
     * 获取图片文件地址
     */
    fun getImgDir(): String {
        return getDir(Img)
    }

    fun getDir(vararg dir: String): String {
        val sb = StringBuilder(mEnvironmentDirectory)
        for (str in dir) {
            sb.append("/$str")
        }
        mkDir(sb.toString())
        return sb.toString()
    }

    /**
     * 创建文件目录
     * @param dir 如：/sdcard/log
     */
    fun mkDir(dir: String?): File? {
        if (isSDCARDMounted()) {
            val file: File?
            try {
                file = File(dir)
                if (!file.exists()) {
                    file.mkdirs()
                }
                return file
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }

    /**
     * 判断SDCARD是否有效
     */
    fun isSDCARDMounted(): Boolean {
        val status = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == status
    }

    /**
     * 创建文件路径
     */
    fun createDirectoryPath(path: String?) {
        val file = File(path)
        if (!file.exists()) {
            file.mkdirs()
        }
    }

    /**
     * 新建文件
     */
    fun createNewDirectory(path: String?): File {
        val file = File(path)
        createDirectoryPath(file.parent)
        if (file.exists()) {
            if (file.delete()) try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return file
    }

    fun getFileTime(path: String?): Long {
        val file = File(path)
        return if (file.exists()) {
            Date(file.lastModified()).time
        } else {
            0
        }
    }

    /**
     * 获取应用数据目录
     *
     * @param context
     * @return
     */
    fun getDiskFileDir(context: Context): String? {
        var filePath: String?
        filePath = if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()) {
            context.getExternalFilesDir(null)!!.path
        } else {
            context.filesDir.path
        }
        return filePath
    }

    /**
     * 应用内部目录
     *
     * @param context
     * @return
     */
    fun getInnerFileDir(context: Context): String {
        return context.filesDir.path
    }

    /**
     * 获取SDCard剩下的大小
     *
     * @return SDCard剩下的大小
     * @since V1.0
     */
    fun getSDCardRemainSize(): Long {
        val statfs = StatFs(Environment.getExternalStorageDirectory().path)
        val blockSize = statfs.blockSize.toLong()
        val availableBlocks = statfs.availableBlocks.toLong()
        return availableBlocks * blockSize
    }

    /**
     * 检查文件名后缀
     * @param fileName 文件全名称
     * @param fileSuffix 所检查后缀
     * @return 该文件是否是包含所检查的后缀
     */
    fun checkSuffix(fileName: String?, fileSuffix: Array<String?>): Boolean {
        for (suffix in fileSuffix) {
            if (fileName != null) {
                if (fileName.toLowerCase(Locale.getDefault()).endsWith(suffix!!)) {
                    return true
                }
            }
        }
        return false
    }
}