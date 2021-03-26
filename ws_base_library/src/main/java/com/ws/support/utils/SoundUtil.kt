@file:Suppress("DEPRECATION")

package com.ws.support.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.RingtoneManager
import android.media.SoundPool
import android.os.Build
import java.util.*

/**
 * 声音播放
 * 修改人：ws
 */
class SoundUtil private constructor() {
    var soundPool: SoundPool? = null
    var soundIds: MutableList<Int> = ArrayList()
    var isInit = false
    fun init(maxStreams: Int) {
        _init(maxStreams)
        soundIds = ArrayList()
        isInit = true
    }

    private fun _init(maxStreams: Int) {
        soundPool = if (Build.VERSION.SDK_INT > 21) {
            val builder = SoundPool.Builder()
            //传入音频数量
            builder.setMaxStreams(maxStreams)
            //AudioAttributes是一个封装音频各种属性的方法
            val attrBuilder = AudioAttributes.Builder()
            //设置音频流的合适的属性
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC)
            //加载一个AudioAttributes
            builder.setAudioAttributes(attrBuilder.build())
            builder.build()
        } else {
//        第一个参数为soundPool可以支持的声音数量，这决定了Android为其开设多大的缓冲区，第二个参数为声音类型，
//        在这里标识为系统声音，除此之外还有AudioManager.STREAM_RING以及AudioManager.STREAM_MUSIC等
//        系统会根据不同的声音为其标志不同的优先级和缓冲区，最后参数为声音品质，品质越高，声音效果越好，但耗费更多的系统资源
            SoundPool(maxStreams, AudioManager.STREAM_SYSTEM, 5)
        }
    }

    fun addSound(context: Context?, resouceId: Int) {
        if (!isInit) {
            throw NullPointerException("please init first")
        }
        val soundId = soundPool!!.load(context, resouceId, 1000)
        soundPool!!.setOnLoadCompleteListener { _: SoundPool?, _: Int, _: Int -> soundIds.add(soundId) }
        //return soundId;
    }

    //播放已加载的声音
    fun playSound(soundId: Int) {
        if (!isInit) {
            throw NullPointerException("please init first")
        }
        soundPool!!.play(soundId, 1f, 1f, 1000, 0, 1f)
    }

    /**
     * 只播放一次
     */
    fun playSoundOnce(context: Context?, resouceId: Int) {
        _init(1)
        val soundId = soundPool!!.load(context, resouceId, 1000)
        soundPool!!.play(soundId, 1f, 1f, 1000, 0, 1f)
    }

    fun release() {
        if (soundPool != null) soundPool!!.release()
        isInit = false
    }

    companion object {
        val instance = SoundUtil()

        /**
         * 播放系统提示音
         */
        fun playSystemNotification(context: Context) {
            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val rt = RingtoneManager.getRingtone(context, uri)
            rt.play()
        }
    }
}