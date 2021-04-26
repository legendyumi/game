package org.ym.game.model

import org.ym.game.Config
import org.ym.game.business.Blockable
import org.itheima.kotlin.game.core.Painter
import org.ym.game.business.Attackable
import org.ym.game.business.Sufferable

/**
 * 铁墙
 *
 * 具有阻塞能力
 * 具有接受攻击的能力
 */
class Steel(override val x: Int, override val y: Int) : Blockable,Sufferable {
    override val blood: Int = 1

    //位置
//    override var x: Int = 200
//    override var y: Int = 200
    //宽高
    override var width: Int = Config.block
    override var height: Int = Config.block

    //显示行为
    override fun draw() {
        Painter.drawImage("img/steel.gif", x, y)
    }

    override fun notifySuffer(attackable: Attackable): Array<View>? {
       return null
    }
}