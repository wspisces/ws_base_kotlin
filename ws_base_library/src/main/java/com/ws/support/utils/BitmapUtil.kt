@file:Suppress("DEPRECATION")

package com.ws.support.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.media.MediaScannerConnection
import android.media.MediaScannerConnection.MediaScannerConnectionClient
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.View.MeasureSpec
import com.orhanobut.logger.Logger
import java.io.*
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * 图片工具类
 *
 * @author Johnny.xu
 * date 2017/7/5
 */
object BitmapUtil {
    fun bmpToByteArray(bmp: Bitmap, needRecycle: Boolean): ByteArray {
        val output = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output)
        if (needRecycle) {
            bmp.recycle()
        }
        val result = output.toByteArray()
        try {
            output.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    /**
     * 截图
     * @author ws
     * 2020/7/8 15:52
     */
    fun getBitmapOfView(v: View?): Bitmap? {
        if (null == v) {
            return null
        }
        v.isDrawingCacheEnabled = true
        v.buildDrawingCache()
        if (Build.VERSION.SDK_INT >= 11) {
            v.measure(MeasureSpec.makeMeasureSpec(v.width,
                    MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
                    v.height, MeasureSpec.EXACTLY))
            v.layout(v.x.toInt(), v.y.toInt(),
                    v.x.toInt() + v.measuredWidth,
                    v.y.toInt() + v.measuredHeight)
        } else {
            v.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
            v.layout(0, 0, v.measuredWidth, v.measuredHeight)
        }
        val b = Bitmap.createBitmap(v.drawingCache, 0, 0, v.measuredWidth, v.measuredHeight)
        v.isDrawingCacheEnabled = false
        v.destroyDrawingCache()
        return b
    }

    /**
     * 根据View(主要是ImageView)的宽和高来获取图片的缩略图
     * @param path 图片路径
     * @param viewWidth 目标宽度
     * @param viewHeight 目标高度
     * @return Bitmap 目标图片
     */
    private fun getThumbBitmapForFile(path: String, viewWidth: Int, viewHeight: Int): Bitmap {
        val options = BitmapFactory.Options()
        //设置为true,表示解析Bitmap对象，该对象不占内存
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)
        //设置缩放比例
        options.inSampleSize = computeScale(options, viewWidth, viewHeight)

        //设置为false,解析Bitmap对象加入到内存中
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(path, options)
    }

    /**
     * 根据View(主要是ImageView)的宽和高来计算Bitmap缩放比例。默认不缩放
     * @param options 图片属性
     * @param viewWidth  目标宽度
     * @param viewHeight 目标高度
     */
    private fun computeScale(options: BitmapFactory.Options, viewWidth: Int, viewHeight: Int): Int {
        var inSampleSize = 1
        if (viewWidth == 0 || viewHeight == 0) {
            return inSampleSize
        }
        val bitmapWidth = options.outWidth
        val bitmapHeight = options.outHeight

        //假如Bitmap的宽度或高度大于我们设定图片的View的宽高，则计算缩放比例
        if (bitmapWidth > viewWidth || bitmapHeight > viewWidth) {
            val widthScale = Math.round(bitmapWidth.toFloat() / viewWidth.toFloat())
            val heightScale = Math.round(bitmapHeight.toFloat() / viewWidth.toFloat())

            //为了保证图片不缩放变形，我们取宽高比例最小的那个
            inSampleSize = if (widthScale < heightScale) widthScale else heightScale
        }
        return inSampleSize
    }

    fun saveImage(bitmap: Bitmap?): Boolean {
        var isOk = false
        val fname = MyFileUtils.getImgDir() + "/" + UUIDGenerator.getUUID() + ".png"
        if (bitmap != null) {
            println("bitmap got!")
            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(fname)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                isOk = true
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (fos != null) {
                    try {
                        fos.flush()
                        fos.close()
                    } catch (e: IOException) {
                    }
                }
            }
        } else {
            println("bitmap is NULL!")
        }
        return isOk
    }

    fun saveImage(fileName: String, bitmap: Bitmap?): Boolean {
        var isOk = false
        val fname = MyFileUtils.getImgDir() + "/" + fileName
        if (bitmap != null) {
            println("bitmap got!")
            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(fname)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                isOk = true
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (fos != null) {
                    try {
                        fos.flush()
                        fos.close()
                    } catch (e: IOException) {
                    }
                }
            }
        } else {
            println("bitmap is NULL!")
        }
        return isOk
    }

    fun saveImage70(context: Context, filepath: String?): String {
        val bitmap = scaleImage(filepath, 640, 480)
        // 为了防止重复 这里用uuid作为名字
        val uuid = UUIDGenerator.getUUID()
        val picPath = MyFileUtils.getImgDir() + "/" + uuid + ".jpg"
        val file = File(picPath)
        try {
            val outStream = FileOutputStream(file)
            if (bitmap!!.compress(Bitmap.CompressFormat.JPEG, 70, outStream)) {
                outStream.flush()
                outStream.close()
            }
            val cr = context.contentResolver
            MediaStore.Images.Media.insertImage(cr, bitmap, uuid, "")
            updateGallery(context, "file://" + Environment.getExternalStorageDirectory())
            bitmap.recycle()
            return picPath
        } catch (e: FileNotFoundException) {
        } catch (e: IOException) {
        }
        return ""
    }

    fun saveImage(context: Context, filepath: String?): String {
        val bitmap = scaleImage(filepath, 720, 1280)
        // 为了防止重复 这里用uuid作为名字
        val uuid = UUIDGenerator.getUUID()
        val picPath = MyFileUtils.getImgDir() + "/" + uuid + ".jpg"
        val file = File(picPath)
        try {
            val outStream = FileOutputStream(file)
            if (bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, outStream)) {
                outStream.flush()
                outStream.close()
            }
            val cr = context.contentResolver
            MediaStore.Images.Media.insertImage(cr, bitmap, uuid, "")
            updateGallery(context, "file://" + Environment.getExternalStorageDirectory())
            bitmap.recycle()
            return picPath
        } catch (e: FileNotFoundException) {
        } catch (e: IOException) {
        }
        return ""
    }

    fun saveImage_480_720(context: Context, filepath: String?): String {
        val bitmap = scaleImage(filepath, 480, 720)
        // 为了防止重复 这里用uuid作为名字
        val uuid = UUIDGenerator.getUUID()
        val picPath = MyFileUtils.getImgDir() + "/" + uuid + ".jpg"
        val file = File(picPath)
        try {
            val outStream = FileOutputStream(file)
            if (bitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, outStream)) {
                outStream.flush()
                outStream.close()
            }
            //val cr = context.contentResolver
            //			MediaStore.Images.Media.insertImage(cr, bitmap, uuid, "");
//			updateGallery(context, "file://" + Environment.getExternalStorageDirectory());
            bitmap.recycle()
            //			updateGalleryNew(context, picPath);
            return picPath
        } catch (e: FileNotFoundException) {
        } catch (e: IOException) {
        }
        return ""
    }

    fun saveImage_1280_1920(context: Context, filepath: String?): String {
        val bitmap = scaleImage(filepath, 1280, 1920)
        // 为了防止重复 这里用uuid作为名字
        val uuid = UUIDGenerator.getUUID()
        val picPath = MyFileUtils.getImgDir() + "/" + uuid + ".jpg"
        val file = File(picPath)
        try {
            val outStream = FileOutputStream(file)
            if (bitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, outStream)) {
                outStream.flush()
                outStream.close()
            }
            val cr = context.contentResolver
            //			MediaStore.Images.Media.insertImage(cr, bitmap, uuid, "");
//			updateGallery(context, "file://" + Environment.getExternalStorageDirectory());
            bitmap.recycle()
            //			updateGalleryNew(context, picPath);
            return picPath
        } catch (e: FileNotFoundException) {
        } catch (e: IOException) {
        }
        return ""
    }
    /**
     * 不改变图片大小压缩图片
     * @param context 上下文
     * @param filepath 图片地址
     * @param size 图片大小
     */
    /**
     * 不改变图片大小压缩图片 默认压缩到1024kb
     * @param context 上下文
     * @param filepath 图片地址
     */
    @JvmOverloads
    fun saveImageBySize(context: Context, filepath: String?, size: Int = 1024): String {
        val bitmap = scaleImage(filepath, 1280, 1920)
        // 为了防止重复 这里用uuid作为名字
        val uuid = UUIDGenerator.getUUID()
        val picPath = MyFileUtils.getImgDir() + "/" + uuid + ".jpg"
        val file = File(picPath)
        try {
            val outStream = FileOutputStream(file)
            if (bitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, outStream)) {
                outStream.flush()
                outStream.close()
            }
            val cr = context.contentResolver
            //			MediaStore.Images.Media.insertImage(cr, bitmap, uuid, "");
//			updateGallery(context, "file://" + Environment.getExternalStorageDirectory());
            bitmap.recycle()
            //			updateGalleryNew(context, picPath);
            return picPath
        } catch (e: FileNotFoundException) {
        } catch (e: IOException) {
        }
        return ""
    }

    private var msc: MediaScannerConnection? = null
    @Synchronized
    fun updateGalleryNew(context: Context, imagePath: String?) {
        try {
            val path = MediaStore.Images.Media.insertImage(context.contentResolver, imagePath, "", "")
            if (StringUtils.isNotEmptyWithNull(path)) {
                msc = MediaScannerConnection(context, object : MediaScannerConnectionClient {
                    override fun onMediaScannerConnected() {
                        if (msc != null) msc!!.scanFile(path, "image/jpeg")
                    }

                    override fun onScanCompleted(path: String, uri: Uri) {
                        if (msc != null) msc!!.disconnect()
                    }
                })
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    fun saveImage(data: ByteArray?): Boolean {
        var isOk = false
        val fileName = MyFileUtils.getImgDir() + "/" + UUIDGenerator.getUUID() + ".png"
        if (data != null) {
            println("bitmap got!")
            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(fileName)
                fos.write(data)
                isOk = true
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (fos != null) {
                    try {
                        fos.flush()
                        fos.close()
                    } catch (e: IOException) {
                    }
                }
            }
        } else {
            println("bitmap is NULL!")
        }
        return isOk
    }

    fun scaleImage(imagePath: String?, requestWidth: Int, requestHeight: Int): Bitmap? {
        var newBitmap: Bitmap? = null
        try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(imagePath, options)
            options.inSampleSize = calculateInSampleSize(options, requestWidth, requestHeight)
            options.inJustDecodeBounds = false
            options.inPreferredConfig = Bitmap.Config.RGB_565
            options.inPurgeable = true
            options.inInputShareable = true
            val bitmap = BitmapFactory.decodeFile(imagePath, options)
            val orientation = getExifOrientation(imagePath, "0")
            val matrix = Matrix()
            matrix.postRotate(java.lang.Float.valueOf(orientation))
            newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, false)
        } catch (e: Exception) {
            Logger.v("ImageUtils", "scaleImage error")
        }
        return newBitmap
    }

    fun getExifOrientation(path: String?, orientation: String): String {
        var orientation = orientation
        val exif_getAttribute: Method
        val exif_construct: Constructor<ExifInterface>
        var exifOrientation = ""
        var sdk_int = 0
        sdk_int = try {
            Integer.valueOf(Build.VERSION.SDK)
        } catch (e1: Exception) {
            3 // assume they are on cupcake
        }
        if (sdk_int >= 5) {
            try {
                exif_construct = ExifInterface::class.java
                        .getConstructor(*arrayOf<Class<*>>(String::class.java))
                val exif: Any = exif_construct.newInstance(path)
                exif_getAttribute = ExifInterface::class.java
                        .getMethod("getAttribute", *arrayOf<Class<*>>(String::class.java))
                try {
                    exifOrientation = exif_getAttribute.invoke(exif,
                            ExifInterface.TAG_ORIENTATION) as String
                    if (exifOrientation != null) {
                        if (exifOrientation == "1") {
                            orientation = "0"
                        } else if (exifOrientation == "3") {
                            orientation = "180"
                        } else if (exifOrientation == "6") {
                            orientation = "90"
                        } else if (exifOrientation == "8") {
                            orientation = "270"
                        }
                    } else {
                        orientation = "0"
                    }
                } catch (ite: InvocationTargetException) {
                    orientation = "0"
                } catch (ie: IllegalAccessException) {
                    System.err.println("unexpected $ie")
                    orientation = "0"
                }
            } catch (nsme: NoSuchMethodException) {
                orientation = "0"
            } catch (e: IllegalArgumentException) {
                orientation = "0"
            } catch (e: InstantiationException) {
                orientation = "0"
            } catch (e: IllegalAccessException) {
                orientation = "0"
            } catch (e: InvocationTargetException) {
                orientation = "0"
            }
        }
        return orientation
    }

    fun calculateInSampleSize(options: BitmapFactory.Options,
                              reqW: Int, reqH: Int): Int {
        val h = options.outHeight
        val w = options.outWidth
        var inSampleSize = 1
        if (h > reqH || w > reqW) {
            val heightRatio = Math.round(h.toFloat() / reqH.toFloat())
            val widthRatio = Math.round(w.toFloat() / reqW.toFloat())
            inSampleSize = Math.min(heightRatio, widthRatio)
        }
        return inSampleSize
    }

    fun updateGallery(context: Context?, filename: String) {
        MediaScannerConnection.scanFile(context, arrayOf(filename), null
        ) { path, uri -> }
    }
}