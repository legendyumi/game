package org.ym.game

import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import org.itheima.kotlin.game.core.Window
import org.ym.game.business.*
import org.ym.game.enums.Direction
import org.ym.game.model.*
import java.io.*
import java.util.concurrent.CopyOnWriteArrayList

class GameWindow : Window(
    title = "坦克大战1.0"
    , icon = "img/logo.jpg"
    , width = Config.gameWidth
    , height = Config.gameHeight
) {

    //管理元素的集合
//    private val views = arrayListOf<View>()
    //线程安全的集合
    private val views = CopyOnWriteArrayList<View>()
    // 晚点创建
    private lateinit var tank: Tank
    //游戏是否结束
    private var gameOver: Boolean = false
    //敌方的数量
    private var enemyTotalSize = 20
    //敌方坦克最多显示几个
    private var enemyActiveSize = 6
    //地方的出生点
    private val enemvBornLocation = arrayListOf<Pair<Int, Int>>()
    //出生地点下标
    private var bornIndex = 0

    override fun onCreate() {
        //地图
        //通过读文件的方式创建地图
//        val file = File(javaClass.getResource("/map/1.map").path)
        val resourceAsStream = javaClass.getResourceAsStream("/map/1.map")
        val reader = BufferedReader(InputStreamReader(resourceAsStream, "utf-8") as Reader?)
        //读取文件中的行
        val lines = reader.readLines()
        //循环遍历
        var lineNum = 0
        lines.forEach { line ->
            var columnNum = 0
            // it  [空砖空砖空砖空砖空砖空砖空]
            line.toCharArray().forEach { column ->
                when (column) {
                    '砖' -> views.add(Wall(columnNum * Config.block, lineNum * Config.block))
                    '铁' -> views.add(Steel(columnNum * Config.block, lineNum * Config.block))
                    '草' -> views.add(Grass(columnNum * Config.block, lineNum * Config.block))
                    '水' -> views.add(Water(columnNum * Config.block, lineNum * Config.block))
//                    '敌' -> views.add(Enemy(columnNum * Config.block, lineNum * Config.block))
                    '敌' -> enemvBornLocation.add(Pair(columnNum * Config.block, lineNum * Config.block))
                }
                columnNum++
            }
            lineNum++
        }


        //添加我方的坦克
        tank = Tank(Config.block * 10, Config.block * 12)
        views.add(tank)
        //添加大本营
        views.add(Camp(Config.gameWidth / 2 - Config.block, Config.gameHeight - 96))
    }

    override fun onDisplay() {
        //绘制图像

        //绘制地图中的元素
        views.forEach {
            it.draw()
        }
        println(views.size)
    }

    override fun onKeyPressed(event: KeyEvent) {
        //用户操作时
        if (!gameOver) {
            when (event.code) {
                KeyCode.W -> {
                    tank.move(Direction.UP)
                }
                KeyCode.S -> {
                    tank.move(Direction.DOWN)
                }
                KeyCode.A -> {
                    tank.move(Direction.LEFT)
                }
                KeyCode.D -> {
                    tank.move(Direction.RIGHT)
                }
                KeyCode.ENTER -> {
                    //发射子弹
                    val bullet = tank.shot()
                    //交给views管理
                    views.add(bullet)
                }
            }
        }
    }

    override fun onRefresh() {
        //检测销毁
        views.filter { it is Destoryable }.forEach {
            //判断具备销毁能力的物体，是否被销毁
            if ((it as Destoryable).isDestory()) {
                views.remove(it)

                if (it is Enemy) {
                    enemyTotalSize--
                }
                //老鹰死时爆炸物显示效果
                val destory = it.showDestroy()
                destory?.let {
                    views.addAll(destory)
                }
            }
        }
        if (gameOver) return
        //业务逻辑
        // 判断运动的物体和阻塞物体是否发生碰撞
        //1） 找到运动的物体
        views.filter { it is Movable }.forEach { move ->
            //2) 找到阻塞的物体
            move as Movable
            var badDirection: Direction? = null
            var badBlock: Blockable? = null

            //不要和自己比较
            views.filter { (it is Blockable) and (move != it) }.forEach blockTag@{ block ->
                // 3) 遍历集合，找到是否发生碰撞
//                move 和 block是否碰撞
                block as Blockable
                move.willCollision(block)
                // 获得碰撞的方向
                val direction = move.willCollision(block)
                direction?.let {
                    //移动的发现碰撞。跳出当前循环
                    badDirection = direction
                    badBlock = block
                    return@blockTag
                }
            }
            //找到和move碰撞的block，找到会碰撞的方向
            //通知可以移动的物体，会在哪个方向和哪个物体碰撞
            move.notifyCollision(badDirection, badBlock)
        }
        //检测自动移动能力的物体，让他们自动动起来
        views.filter { it is AutoMovable }.forEach {
            (it as AutoMovable).autoMove()
        }
        //检测 具备攻击能力的和被攻击能力的物体间是否产生碰撞
        //1)过滤 具备攻击能力的
        views.filter { it is Attackable }.forEach { attack ->
            attack as Attackable
            //2）过滤遭受攻击的  自己的子弹不能打自己
            //攻击方也是受攻击方时，是不可以打自己的
            views.filter { (it is Sufferable) and (attack.owner != it) and (attack != it) }
                .forEach sufferTag@{ suffer ->
                    suffer as Sufferable
                    //3）判断是否发生碰撞
                    if (attack.isCollision(suffer)) {
                        //产生碰撞,找到碰撞者，
                        //通知攻击者产生碰撞
                        attack.notifyAttack(suffer)
                        //通知被攻击着，产生碰撞
                        val sufferView = suffer.notifySuffer(attack)
                        sufferView?.let {
                            //显示挨打的效果
                            views.addAll(sufferView)
                        }
                        return@sufferTag
                    }
                }
        }
        //检测自动射击
        views.filter { it is AutoShot }.forEach {
            it as AutoShot
            val shot = it.autoShot()
            shot?.let {
                views.add(shot)
            }
        }

        //游戏是否结束
        if ((views.filter { it is Camp }.isEmpty()) or (enemyTotalSize <= 0) or (views.filter { it is Tank }.isEmpty())) {
            gameOver = true
        }

        //检测敌方出生
        //判断当前页面上地方的数量，小于激活数量
        if ((enemyTotalSize > 0) and (views.filter { it is Enemy }.size < enemyActiveSize)) {
            val index = bornIndex % enemvBornLocation.size
            val pair = enemvBornLocation[index]
            views.add(Enemy(pair.first, pair.second))
            bornIndex++
        }
    }
}