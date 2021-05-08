package com.ws.support.utils

import android.graphics.Bitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import java.security.MessageDigest


class RoundedCorners(
        val topLeftRadius: Int = 0,
        val topRightRadius: Int = 0,
        val bottomLeftRadius: Int = 0,
        val bottomRightRadius: Int = 0
) : BitmapTransformation() {
    constructor(roundingRadius: Int = 0) : this(
            roundingRadius,
            roundingRadius
    )

    constructor(topRadius: Int = 0, bottomRadius: Int = 0) : this(
            topRadius,
            topRadius,
            bottomRadius,
            bottomRadius
    )

    override fun updateDiskCacheKey(messageDigest: MessageDigest) { }

    // TODO 你还可以使用 TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight) 对toTranshform 图片对象进行变换
    override fun transform(pool: BitmapPool,
                           toTransform: Bitmap, outWidth:
                           Int, outHeight: Int
    ): Bitmap = TransformationUtils.roundedCorners(pool, toTransform,
            topLeftRadius.toFloat(),
            topRightRadius.toFloat(),
            bottomRightRadius.toFloat(),
            bottomLeftRadius.toFloat()
    )
}

