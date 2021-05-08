package com.ws.support.utils.web

import android.content.Context
import android.os.Environment
import android.os.StatFs
import android.text.TextUtils
import android.util.Base64
import java.io.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * @desc
 * @auth 方毅超
 * @time 2017/12/8 14:41
 */
class FileManager {
    // 取得SD卡文件路径
    // 获取单个数据块的大小(Byte)
    // 空闲的数据块的数量
    // 返回SD卡空闲大小
    // return freeBlocks * blockSize; //单位Byte
    // return (freeBlocks * blockSize)/1024; //单位KB
    // 单位MB
    // SD卡剩余空间
    val sDFreeSize: Long
        get() {
            // 取得SD卡文件路径
            val path = Environment.getExternalStorageDirectory()
            val sf = StatFs(path.path)
            // 获取单个数据块的大小(Byte)
            val blockSize = sf.blockSize.toLong()
            // 空闲的数据块的数量
            val freeBlocks = sf.availableBlocks.toLong()
            // 返回SD卡空闲大小
            // return freeBlocks * blockSize; //单位Byte
            // return (freeBlocks * blockSize)/1024; //单位KB
            return freeBlocks * blockSize / 1024 / 1024 // 单位MB
        }

    companion object {
        const val ROOT_NAME = "RongYifu"
        const val LOG_NAME = "UserLog"
        const val CACHE_NAME = "Cache"
        const val IMAGE_NAME = "Image"
        const val RECORD_NAME = "Voice"
        val ROOT_PATH = (File.separator + ROOT_NAME
                + File.separator)
        val LOG_PATH_NAME = (File.separator + LOG_NAME
                + File.separator)
        val CACHE_PATH_NAME = (File.separator + CACHE_NAME
                + File.separator)
        val IMAGE_PATH_NAME = (File.separator + IMAGE_NAME
                + File.separator)
        val RECORD_PATH_NAME = (File.separator + RECORD_NAME
                + File.separator)
        const val ACTION_DEL_ALL_IMAGE_CACHE = "com.citic21.user_delImageCache"
        const val CODE_ENCODING = "utf-8"
        fun getRootPath(appContext: Context): String? {
            var rootPath: String? = null
            rootPath = if (checkMounted()) {
                rootPathOnSdcard
            } else {
                getRootPathOnPhone(appContext)
            }
            return rootPath
        }

        val rootPathOnSdcard: String
            get() {
                val sdcard = Environment.getExternalStorageDirectory()
                return sdcard.absolutePath + ROOT_PATH
            }

        fun getRootPathOnPhone(appContext: Context): String {
            val phoneFiles = appContext.filesDir
            return phoneFiles.absolutePath + ROOT_PATH
        }// 获取跟目录

        // 判断sd卡是否存在
        val sdcardPath: String
            get() {
                var sdDir: File? = null
                val sdCardExist = checkMounted() // 判断sd卡是否存在
                if (sdCardExist) {
                    sdDir = Environment.getExternalStorageDirectory() // 获取跟目录
                    return sdDir.path
                }
                return "/"
            }

        fun checkMounted(): Boolean {
            return Environment.MEDIA_MOUNTED == Environment
                    .getExternalStorageState()
        }

        fun getUserLogDirPath(appContext: Context): String {
            return getRootPath(appContext) + LOG_PATH_NAME
        }

        // 缓存整体路径
        fun getCacheDirPath(appContext: Context): String {
            return getRootPath(appContext) + CACHE_PATH_NAME
        }

        // 图片缓存路径
        fun getImageCacheDirPath(appContext: Context): String {
            return getCacheDirPath(appContext) + IMAGE_PATH_NAME
        }

        // 创建一个图片文件
        @JvmStatic
        fun getImgFile(context: Context): File {
            val file = File(getImageCacheDirPath(context))
            if (!file.exists()) {
                file.mkdirs()
            }
            val imgName = SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(Date())
            return File(file.absolutePath + File.separator
                    + "IMG_" + imgName + ".jpg")
        }

        // 创建拍照处方单路径
        fun initCreatImageCacheDir(appContext: Context): File {
            val rootPath = getImageCacheDirPath(appContext)
            val dir = File(rootPath)
            if (!dir.exists()) {
                dir.mkdirs()
            }
            return dir
        }

        fun getFileSize(file: File): String {
            var fileSize = "0.00K"
            if (file.exists()) {
                fileSize = FormetFileSize(file.length())
                return fileSize
            }
            return fileSize
        }

        fun FormetFileSize(fileS: Long): String { // 转换文件大小
            val df = DecimalFormat("0.00")
            var fileSizeString = ""
            fileSizeString = if (fileS < 1024) {
                df.format(fileS.toDouble()) + "B"
            } else if (fileS < 1048576) {
                df.format(fileS.toDouble() / 1024) + "K"
            } else if (fileS < 1073741824) {
                df.format(fileS.toDouble() / 1048576) + "M"
            } else {
                df.format(fileS.toDouble() / 1073741824) + "G"
            }
            return fileSizeString
        }

        fun writeStringToFile(text: String, file: File?): Boolean {
            return try {
                writeStringToFile(text.toByteArray(charset(CODE_ENCODING)), file)
            } catch (e1: UnsupportedEncodingException) {
                e1.printStackTrace()
                false
            }
        }

        fun close(c: Closeable?) {
            if (c != null) {
                try {
                    c.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        fun writeStringToFile(datas: ByteArray?, file: File?): Boolean {
            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(file)
                fos.write(datas)
                fos.flush()
                return true
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                close(fos)
            }
            return false
        }

        /**
         * @param oldpath URL 的 md5+"_tmp"
         * @param newpath URL 的 md5+
         * @return
         */
        fun renameFileName(oldpath: String?, newpath: String?): Boolean {
            try {
                val file = File(oldpath)
                if (file.exists()) {
                    file.renameTo(File(newpath))
                }
                return true
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }

        fun isFileExists(file: File): Boolean {
            return if (file.exists()) {
                true
            } else false
        }

        fun getFileSizeByByte(file: File): Long {
            var fileSize = 0L
            if (file.exists()) {
                fileSize = file.length()
                return fileSize
            }
            return fileSize
        }

        fun checkCachePath(appContext: Context): Boolean {
            val path = getCacheDirPath(appContext)
            val file = File(path)
            return if (!file.exists()) {
                false
            } else true
        }

        fun getUrlFileName(resurlt: String): String {
            return if (!TextUtils.isEmpty(resurlt)) {
                val nameIndex = resurlt.lastIndexOf("/")
                var loacalname = ""
                if (nameIndex != -1) {
                    loacalname = resurlt.substring(nameIndex + 1)
                }
                val index = loacalname.indexOf("?")
                if (index != -1) {
                    loacalname = loacalname.substring(0, index)
                }
                loacalname
            } else {
                resurlt
            }
        }

        // 存储map类型数据 转换为Base64进行存储
        @Throws(IOException::class)
        fun SceneList2String(SceneList: Map<String?, String?>?): String {
            val toByte = ByteArrayOutputStream()
            var oos: ObjectOutputStream? = null
            try {
                oos = ObjectOutputStream(toByte)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                oos!!.writeObject(SceneList)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            // 对byte[]进行Base64编码
            return String(Base64.encode(toByte.toByteArray(),
                    Base64.DEFAULT))
        }
    }
}