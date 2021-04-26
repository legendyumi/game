package org.ym.game.business

import org.ym.game.model.View


/**
 * 具备攻击的能力
 */
interface Attackable {

    /**
     * 所有者
     */
    val owner: View
    /**
     * 攻击力
     */
    val attackPowner: Int

    //判断是否碰撞
    fun isCollision(sufferable: Sufferable): Boolean

    fun notifyAttack(sufferable: Sufferable)
}