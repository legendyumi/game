package org.ym.game.model

import org.itheima.kotlin.game.core.Painter
import org.ym.game.Config
import org.ym.game.business.Destoryable

/**
 * 爆炸物
 */
class Blast(override val x: Int, override val y: Int) : Destoryable {
    override val width: Int = Config.block
    override val height: Int = Config.block
    private val imagePaths = arrayListOf<String>()
    private var index = 0

    init {
        (1..32).forEach {
            imagePaths.add("img/blast_${it}.png")
        }
    }

    override fun draw() {
        var i = index % imagePaths.size//不会越界
        Painter.drawImage(imagePaths[i], x, y)
        index++
    }

    override fun isDestory(): Boolean {
        return index >= imagePaths.size
    }
}