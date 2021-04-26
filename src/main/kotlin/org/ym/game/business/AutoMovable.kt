package org.ym.game.business

import org.ym.game.enums.Direction

/**
 * 自动移动
 */
interface AutoMovable {
    //方向
    val currentDirection: Direction
    //速度
    val speed: Int
    fun autoMove()
}