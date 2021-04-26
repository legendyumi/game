package org.ym.game.business

import org.ym.game.model.View


/**
 * 销毁
 */
interface Destoryable : View {
    /**
     * 判断是否销毁了
     */
    fun isDestory():Boolean

    /**
     * 死给你看的功能
     */
    fun showDestroy(): Array<View>? {
        return null
    }
}