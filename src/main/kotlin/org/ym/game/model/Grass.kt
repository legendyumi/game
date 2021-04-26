package org.ym.game.model

import org.ym.game.Config
import org.itheima.kotlin.game.core.Painter

/**
 * 草坪
 *
 */
class Grass(override val x: Int, override val y: Int) : View {

    //位置
//    override var x: Int = 200
//    override var y: Int = 200
    //宽高
    override var width: Int = Config.block
    override var height: Int = Config.block

    //显示行为
    override fun draw() {
        Painter.drawImage("img/grass.gif", x, y)
    }
}