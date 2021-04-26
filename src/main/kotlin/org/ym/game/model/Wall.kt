package org.ym.game.model

import org.itheima.kotlin.game.core.Composer
import org.ym.game.Config
import org.ym.game.business.Blockable
import org.itheima.kotlin.game.core.Painter
import org.ym.game.business.Attackable
import org.ym.game.business.Destoryable
import org.ym.game.business.Sufferable

/**
 * 砖墙
 *
 * 具备阻塞能力
 * 具备挨打能力
 * 具备销毁能力
 */
class Wall(override val x: Int, override val y: Int) : Blockable, Sufferable, Destoryable {
    //生命值
    override var blood: Int = 3

    //位置
//    override val x: Int = 100
//    override val y: Int = 100
    //宽高
    override val width: Int = Config.block
    override val height: Int = Config.block

    //显示行为
    override fun draw() {
        Painter.drawImage("img/wall.gif", x, y)
    }

    override fun notifySuffer(attackable: Attackable): Array<View> {
        println("砖墙接收到挨打了")
        //砖墙生命值降低
        blood -= attackable.attackPowner
        Composer.play("snd/hit.wav")
        return arrayOf(Blast(x,y))
    }

    override fun isDestory(): Boolean = blood <= 0
}