package com.ws.support.utils

import android.widget.ImageView
import com.ws.base.R
import org.xutils.common.util.DensityUtil
import org.xutils.image.ImageOptions

/**
 * Created by wg on 2017/9/27.
 */
object XutilImageOption {
    var roundConnerOption = ImageOptions.Builder().setRadius(DensityUtil.dip2px(5f)).setSquare(true).setImageScaleType(ImageView.ScaleType.FIT_XY).setLoadingDrawableId(R.mipmap.ic_icon_none).setFailureDrawableId(R.mipmap.ic_icon_none).setUseMemCache(true).build()
    var fitcenterOption = ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_CENTER).setLoadingDrawableId(R.mipmap.ic_icon_none).setFailureDrawableId(R.mipmap.ic_icon_none).setUseMemCache(true).build()
    var normalOption = ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setLoadingDrawableId(R.mipmap.ic_icon_none).setFailureDrawableId(R.mipmap.ic_icon_none).setUseMemCache(true).build()
    var roundOption = ImageOptions.Builder().setCircular(true).setImageScaleType(ImageView.ScaleType.CENTER_CROP).setLoadingDrawableId(R.mipmap.ic_icon_none).setFailureDrawableId(R.mipmap.ic_icon_none).setUseMemCache(true).build()
}