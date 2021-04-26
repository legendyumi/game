package org.ym.game.model

import org.itheima.kotlin.game.core.Painter
import org.ym.game.Config
import org.ym.game.business.*
import org.ym.game.enums.Direction
import java.util.*

/**
 * 敌方坦克
 *
 * 敌方坦克是可以移动的(避开阻挡物)
 * 敌方坦克是可以自动移动的(自己动起来)
 * 敌方坦克是可以阻塞移动
 * 敌方坦克是可以自动射击
 * 敌方坦克是可以被打
 * 敌方坦克是可以销毁
 */
class Enemy(override var x: Int, override var y: Int) : View, Movable, AutoMovable, Blockable,
    AutoShot, Sufferable, Destoryable {
    override var blood: Int = 2

    override var currentDirection: Direction = Direction.DOWN
    override val speed: Int = 8
    override val width: Int = Config.block
    override val height: Int = Config.block
    //坦克不可以走的方向
    private var badDirection: Direction? = null

    //上次射击的时间
    private var lastShotTime = 0L
    //射击的频率
    private var lastShotFrequency = 800

    //上次移动的时间
    private var lastMoveTime = 0L
    //移动的频率
    private var lastMoveFrequency = 200

    override fun draw() {
        val imagePath = when (currentDirection) {
            Direction.UP -> "img/enemy_1_u.gif"
            Direction.DOWN -> "img/enemy_1_d.gif"
            Direction.LEFT -> "img/enemy_1_l.gif"
            Direction.RIGHT -> "img/enemy_1_r.gif"
        }
        Painter.drawImage(imagePath, x, y)
    }

    override fun notifyCollision(direction: Direction?, block: Blockable?) {
        badDirection = direction
    }

    /**
     * 自动
     */
    override fun autoMove() {
        //移动频率检测
        val current = System.currentTimeMillis()
        if (current - lastMoveTime < lastMoveFrequency) return
        lastMoveTime = current
        if (currentDirection == badDirection) {
            currentDirection = rdmDirection(badDirection)
            return
        }
        //坦克的坐标需要发生变化
        //根据不同的方向，改变对应的坐标
        when (currentDirection) {
            Direction.UP -> y -= speed
            Direction.DOWN -> y += speed
            Direction.LEFT -> x -= speed
            Direction.RIGHT -> x += speed
        }

        //越界判断
        if (x < 0) x = 0
        if (x > Config.gameWidth - width) x = Config.gameWidth - width
        if (y < 0) y = 0
        if (y > Config.gameHeight - height) y = Config.gameHeight - height
    }

    /**
     * 随机产生方向
     */
    private fun rdmDirection(bad: Direction?): Direction {
        val i = Random().nextInt(4)
        val direction = when (i) {
            0 -> Direction.UP
            1 -> Direction.DOWN
            2 -> Direction.LEFT
            3 -> Direction.RIGHT
            else -> Direction.UP
        }
        //判断 不能要错误的反向   伪递归调用
        if (direction == bad) {
            return rdmDirection(bad)
        }
        return direction
    }

    override fun autoShot(): View? {
        //设置射击时间
        val current = System.currentTimeMillis()
        if (current - lastShotTime < lastShotFrequency) return null
        lastShotTime = current
        return Bullet(this, currentDirection) { bulletWidth, bulletHeight ->
            //计算子弹真实的坐标
            val tankX = x
            val tankY = y
            val tankWidth = width
            val tankHeight = height

            // 如果坦克是向上的，计算子弹的位置
            // bulletX = tankX + (tankWidth - bulletWidth) / 2
            // bulletY = tankY - bulletHeight / 2
            var bulletX = 0
            var bulletY = 0

            when (currentDirection) {
                Direction.UP -> {
                    bulletX = tankX + (tankWidth - bulletWidth) / 2
                    bulletY = tankY - bulletHeight / 2
                }
                Direction.DOWN -> {
                    bulletX = tankX + (tankWidth - bulletWidth) / 2
                    bulletY = tankY + tankHeight - bulletHeight / 2
                }
                Direction.LEFT -> {
                    bulletX = tankX - bulletWidth / 2
                    bulletY = tankY + (tankHeight - bulletHeight) / 2
                }
                Direction.RIGHT -> {
                    bulletX = tankX + tankWidth - bulletWidth / 2
                    bulletY = tankY + (tankHeight - bulletHeight) / 2
                }
            }

            Pair(bulletX, bulletY)
        }
    }

    /**
     * 受到打击
     */
    override fun notifySuffer(attackable: Attackable): Array<View>? {
        //攻击方是敌方，不掉血
        if (attackable.owner is Enemy){
            return null
        }
        blood -= attackable.attackPowner
        return arrayOf(Blast(x, y))
    }

    /**
     * 是否销毁
     */
    override fun isDestory() = blood <= 0
}