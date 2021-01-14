package com.ws.support.service

/**
 * 描述信息
 *
 * @author ws
 * 2020/8/12 14:09
 * 修改人：ws
 */
class NewVersionEvent {
    var type = 0 //用于区分不同的页面
    private var hasNewVersion = false //有新版本
    private var downloadFinish = false //下载完成
    private var downloadStart = false
    private var downloadError = false

    private constructor() {}
    private constructor(hasNewVersion: Boolean, type: Int) {
        this.hasNewVersion = hasNewVersion
        this.type = type
    }

    fun isHasNewVersion(): Boolean {
        return hasNewVersion
    }

    fun isDownloadStart(): Boolean {
        return downloadStart
    }

    fun isDownloadFinish(): Boolean {
        return downloadFinish
    }

    fun isDownloadError(): Boolean {
        return downloadError
    }

    companion object {
        fun hasNew(hasNewVersion: Boolean, type: Int): NewVersionEvent {
            return NewVersionEvent(hasNewVersion, type)
        }

        fun downLoadStart(type: Int): NewVersionEvent {
            val event = NewVersionEvent(true, type)
            event.downloadStart = true
            return event
        }

        fun downLoadFinish(type: Int): NewVersionEvent {
            val event = NewVersionEvent(true, type)
            event.downloadFinish = true
            return event
        }

        fun downLoadError(type: Int): NewVersionEvent {
            val event = NewVersionEvent(true, type)
            event.downloadError = true
            return event
        }
    }
}