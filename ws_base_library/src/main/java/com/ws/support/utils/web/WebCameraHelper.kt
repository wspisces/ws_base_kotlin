package com.ws.support.utils.web

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ws.support.utils.web.FileManager.Companion.getImgFile

/**
 * @desc web页面调用本地照相机、图库的相关助手
 * @auth 方毅超
 * @time 2017/12/8 16:25
 * url https://www.jianshu.com/p/0647a0bb885e
 */
//<input type="file" accept=".jpeg, .jpg, .png" name="upload_file" id="js-title-img-input">
class WebCameraHelper {

    /**
     * 图片选择回调
     */
    var mUploadMessage: ValueCallback<Uri?>? = null
    var mUploadCallbackAboveL: ValueCallback<Array<Uri?>?>? = null
    var fileUri: Uri? = null

    /**
     * 包含拍照和相册选择
     */
    fun showOptions(act: Activity) {
        val alertDialog = AlertDialog.Builder(act)
        alertDialog.setOnCancelListener(ReOnCancelListener())
        alertDialog.setTitle("选择")
        alertDialog.setItems(arrayOf<CharSequence>("相机", "相册")
        ) { dialog, which ->
            if (which == 0) {
                if (ContextCompat.checkSelfPermission(act,
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // 申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat
                            .requestPermissions(
                                    act, arrayOf(Manifest.permission.CAMERA),
                                    TYPE_REQUEST_PERMISSION)
                } else {
                    toCamera(act)
                }
            } else {
                val i = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI) // 调用android的图库
                act.startActivityForResult(i,
                        TYPE_GALLERY)
            }
        }
        alertDialog.show()
    }

    /**
     * 请求拍照
     *
     * @param act
     */
    fun toCamera(act: Activity) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE) // 调用android的相机
        // 创建一个文件保存图片
        fileUri = Uri.fromFile(getImgFile(act.applicationContext))
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
        act.startActivityForResult(intent, TYPE_CAMERA)
    }

    /**
     * startActivityForResult之后要做的处理
     *
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode == TYPE_CAMERA) { // 相册选择
            if (resultCode == -1) { //RESULT_OK = -1，拍照成功
                if (mUploadCallbackAboveL != null) { //高版本SDK处理方法
                    val uris = arrayOf(fileUri)
                    mUploadCallbackAboveL!!.onReceiveValue(uris)
                    mUploadCallbackAboveL = null
                } else if (mUploadMessage != null) { //低版本SDK 处理方法
                    mUploadMessage!!.onReceiveValue(fileUri)
                    mUploadMessage = null
                } else {
//                    Toast.makeText(CubeAndroid.this, "无法获取数据", Toast.LENGTH_LONG).show();
                }
            } else { //拍照不成功，或者什么也不做就返回了，以下的处理非常有必要，不然web页面不会有任何响应
                if (mUploadCallbackAboveL != null) {
                    mUploadCallbackAboveL!!.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent))
                    mUploadCallbackAboveL = null
                } else if (mUploadMessage != null) {
                    mUploadMessage!!.onReceiveValue(fileUri)
                    mUploadMessage = null
                } else {
//                    Toast.makeText(CubeAndroid.this, "无法获取数据", Toast.LENGTH_LONG).show();
                }
            }
        } else if (requestCode == TYPE_GALLERY) { // 相册选择
            if (mUploadCallbackAboveL != null) {
                mUploadCallbackAboveL!!.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent))
                mUploadCallbackAboveL = null
            } else if (mUploadMessage != null) {
                val result = if (intent == null || resultCode != Activity.RESULT_OK) null else intent.data
                mUploadMessage!!.onReceiveValue(result)
                mUploadMessage = null
            } else {
//                Toast.makeText(CubeAndroid.this, "无法获取数据", Toast.LENGTH_LONG).show();
            }
        }
    }


    /**
     * 点击取消的回调
     */
    private inner class ReOnCancelListener : DialogInterface.OnCancelListener {
        override fun onCancel(dialogInterface: DialogInterface) {
            if (mUploadMessage != null) {
                mUploadMessage!!.onReceiveValue(null)
                mUploadMessage = null
            }
            if (mUploadCallbackAboveL != null) {
                mUploadCallbackAboveL!!.onReceiveValue(null)
                mUploadCallbackAboveL = null
            }
        }
    }

    companion object {
        const val TYPE_REQUEST_PERMISSION = 3
        const val TYPE_CAMERA = 1
        const val TYPE_GALLERY = 2

        private var instance: WebCameraHelper? = null
            get() {
                if (field == null) {
                    field = WebCameraHelper()
                }
                return field
            }
        @Synchronized
        fun get(): WebCameraHelper {
            //细心的小伙伴肯定发现了，这里不用getInstance作为为方法名，是因为在伴生对象声明时，内部已有getInstance方法，所以只能取其他名字
            return instance!!
        }
    }
}