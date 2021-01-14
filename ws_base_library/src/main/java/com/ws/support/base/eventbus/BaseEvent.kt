package com.ws.support.base.eventbus

/**
 * 基础EventBus
 *
 * @author wg
 * @time 2017/5/25 10:12
 */
class BaseEvent<T> {
    var mType = 0
    var name: String? = null

    constructor(name: String?) {
        this.name = name
    }

    constructor(mType: Int) {
        this.mType = mType
    }

    constructor(name: String?, mType: Int) {
        this.name = name
        this.mType = mType
    }

    constructor() {}
    constructor(name: String?, mContent: T) {
        this.name = name
        this.mContent = mContent
    }

    constructor(name: String?, mType: Int, mContent: T) {
        this.name = name
        this.mType = mType
        this.mContent = mContent
    }

    var mContent: T? = null
}