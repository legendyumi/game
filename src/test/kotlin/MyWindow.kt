import javafx.application.Application
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.paint.Color
import org.itheima.kotlin.game.core.Composer
import org.itheima.kotlin.game.core.Painter
import org.itheima.kotlin.game.core.Window

/**
 * 窗体
 * 继承游戏引擎总的窗体
 */
class MyWindow : Window() {
    override fun onCreate() {
        println("窗体创建")
    }

    /**
     * 窗体渲染的回调
     * 不停的执行
     */
    override fun onDisplay() {
        println("onDisplay")
        //绘制图片
        Painter.drawImage("3.png",0,0)
        //绘制颜色
        Painter.drawColor(Color.WHITE,10,20,100,100)
        //绘制文字
        Painter.drawText("你好",30,30)


    }

    /**
     * 按键响应
     */
    override fun onKeyPressed(event: KeyEvent) {
        //按键响应
        when(event.code){
            KeyCode.ENTER-> println("点击的enter键")
            //播放声音
            KeyCode.L->Composer.play("blast.mav")
        }
    }

    /**
     * 刷新，做业务逻辑，做耗时操作
     */
    override fun onRefresh() {
//        println("窗体创建")
    }


}
fun main(args :Array<String>){
    //启动游戏
    Application.launch(MyWindow::class.java)
}