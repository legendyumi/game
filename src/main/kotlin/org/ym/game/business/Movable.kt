package org.ym.game.business

import org.ym.game.Config
import org.ym.game.enums.Direction
import org.ym.game.model.View

/**
 * 移动运动的能力
 */
interface Movable : View {

    /**
     * 可移动的物体存在方向
     */
    val currentDirection: Direction

    /**
     * 可移动的物体需要右移动的速度
     */
    val speed: Int

    /**
     * 判断移动的物体是否和阻塞物体发生碰撞
     *
     * @return 要碰撞的方向,如果为null，说明没有碰撞的
     */
    fun willCollision(block: Blockable): Direction?{
        //未来的坐标
        var x = this.x
        var y = this.y
        //将要碰撞时做判断
        when (currentDirection) {
            Direction.UP -> y -= speed
            Direction.DOWN -> y += speed
            Direction.LEFT -> x -= speed
            Direction.RIGHT -> x += speed
        }
        //越界判断
        if (x < 0)return Direction.LEFT
        if (x > Config.gameWidth - width) return Direction.RIGHT
        if (y < 0) return Direction.UP
        if (y > Config.gameHeight - height)return Direction.DOWN

        /*//检测碰撞 下一步是否碰撞
        val collision = when {
            block.y + block.height <= y -> //如果 阻挡物 在运动物的上方时 ，不碰撞
                false
            y + height <= block.y -> //如果 阻挡物 在运动物的下方时 ，不碰撞
                false
            block.x + block.width <= x -> //如果 阻挡物 在运动物的左方时 ，不碰撞
                false
            else -> x + width > block.x
        }
        return if (collision) currentDirection else null*/
        return if (checkCollision(
                block.x,
                block.y,
                block.width,
                block.height,
                x,
                y,
                width,
                height
            )
        ) currentDirection else null
    }
    /**
     * 通知碰撞
     */
    fun notifyCollision(direction: Direction?, block: Blockable?)
}