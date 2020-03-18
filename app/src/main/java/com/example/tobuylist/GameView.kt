package com.example.tobuylist

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View

class GameView(context: Context?) : View(context) {
    private val greenPaint : Paint = Paint()
    private val blackPaint : Paint = Paint()

    var cX : Float = 0f
    var cY : Float = 0f


    init{
        greenPaint.color = Color.GREEN
        blackPaint.color= Color.BLACK
    }
    override fun onDraw(canvas: Canvas?) {
        canvas?.drawCircle(0f, 0f, 100f, blackPaint)
        canvas?.drawCircle(Companion.maxX, Companion.maxY, 100f, greenPaint)
        canvas?.drawCircle(cX, cY, 100f, greenPaint)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        cX = event!!.getX()
        cY = event!!.getY()
        invalidate()
        return true
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        Companion.maxX = w.toFloat()
        Companion.maxY = h.toFloat()
        cX = Companion.maxX /2
        cY = Companion.maxY -200
    }

    companion object {
        var maxX: Float = 0f
        var maxY: Float = 0f
    }
    inner class gameThread : Thread(){
        override fun run() {
            while (true){
                SystemClock.sleep(10)

            }
        }
    }
}
open class CircleObject(var x:Float, var y:Float, var r:Float, var angle: Float, var vel:Float, var paint : Paint){
    var vector : Vector = Vector(angle)
    open fun update(){
        x += vector.x * vel;
        y += -(vector.y) * vel;
    }
}
class Bullet(x: Float, y: Float, r: Float, angle: Float, vel: Float, paint: Paint) : CircleObject(x, y, r, angle, vel, paint){
    override fun update(){
        if(x < 0 || x > GameView.maxX- r * 2) {
            vector.turnX()
        }
        if(y <  GameView.maxY || y > GameView.maxY - r * 2){
            vector.turnY()
        }
    }
}
class Vector(angle: Float) {
    var x: Float
    var y: Float
    var anglePI: Float
    fun turnX() {
        anglePI =
            (3.0f * Math.PI.toFloat() - anglePI) % (2.0f * Math.PI.toFloat())
        x = Math.cos(anglePI.toDouble()) as Float
        y = Math.sin(anglePI.toDouble()) as Float
    }

    fun turnY() {
        anglePI = 2.0f * Math.PI.toFloat() - anglePI
        x = Math.cos(anglePI.toDouble()) as Float
        y = Math.sin(anglePI.toDouble()) as Float
    }

    init {
        anglePI = Math.PI.toFloat() * angle / 180.0f
        x = Math.cos(anglePI.toDouble()) as Float
        y = Math.sin(anglePI.toDouble()) as Float
    }
}