package org.ym.game.model

import org.itheima.kotlin.game.core.Painter
import org.ym.game.Config
import org.ym.game.business.Attackable
import org.ym.game.business.AutoMovable
import org.ym.game.business.Destoryable
import org.ym.game.business.Sufferable
import org.ym.game.enums.Direction
import org.ym.game.ext.checkCollision

/**
 * 子弹
 * 具备攻击能力
 */
class Bullet(override val owner: View,override val currentDirection: Direction, create: (width: Int, height: Int) -> Pair<Int, Int>

) : View,
    AutoMovable, Destoryable, Attackable,Sufferable {
    override val blood: Int = 1

    //攻击力
    override val attackPowner: Int =1
    //速度
    override val speed: Int = 8
    //create: () -> Pair<Int, Int>  函数返回两个值x,y  Pair

    //给子弹方向，方向由坦克决定，计算子弹的宽高，进而计算子弹坐标点
    override val width: Int
    override val height: Int
    //子弹坐标点
    override var x: Int = 0
    override var y: Int = 0

    //是否被销毁
    private var isDestoryed = false

    private val imagePath: String = when (currentDirection) {
        Direction.UP -> "img/shot_top.gif"
        Direction.DOWN -> "img/shot_bottom.gif"
        Direction.LEFT -> "img/shot_left.gif"
        Direction.RIGHT -> "img/shot_right.gif"
    }

    /**
     * 初始化
     */
    init {
        //先计算宽度和高度
        val size = Painter.size(imagePath)
        width = size[0]
        height = size[1]
        val invoke = create.invoke(width, height)
        x = invoke.first
        y = invoke.second
    }

    override fun draw() {
//        val imagePath = when (direction) {
//            Direction.UP -> "img/shot_top.gif"
//            Direction.DOWN -> "img/shot_down.gif"
//            Direction.LEFT -> "img/shot_left.gif"
//            Direction.RIGHT -> "img/shot_right.gif"
//        }
        Painter.drawImage(imagePath, x, y)
    }

    override fun autoMove() {
        //根据自己的方向，来改变自己的坐标
        when (currentDirection) {
            Direction.UP -> y -= speed
            Direction.DOWN -> y += speed
            Direction.LEFT -> x -= speed
            Direction.RIGHT -> x += speed
        }
    }

    override fun isDestory(): Boolean {
        if (isDestoryed) return true
        if (x < -width) return true
        if (x > Config.gameWidth) return true
        if (y < -height) return true
        if (y > Config.gameHeight) return true
        return false
    }

    /**
     * 是否产生碰撞
     */
    override fun isCollision(sufferable: Sufferable): Boolean {
        return checkCollision(sufferable)
    }

    override fun notifyAttack(sufferable: Sufferable) {
        println("子弹接收到碰撞了")
        //子弹打到墙后，要销毁
        isDestoryed = true
    }


    override fun notifySuffer(attackable: Attackable): Array<View>? {
        return arrayOf(Blast(x, y))
    }
}