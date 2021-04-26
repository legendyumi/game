package org.ym.game.ext

import org.ym.game.business.Sufferable
import org.ym.game.model.View

/**
 * 拓展方法
 */
fun View.checkCollision(view: View): Boolean {
    return checkCollision(x, y, width, height, view.x, view.y, view.width, view.height)
}