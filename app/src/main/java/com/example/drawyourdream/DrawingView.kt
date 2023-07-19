package com.example.drawyourdream

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import java.util.jar.Attributes

class DrawingView(context: Context,attrs:AttributeSet) : View(context,attrs){

    private var mDrawPath: CustomPath? =null //An variable of CustomPath inner class to use it further
    private var mCanvasBitmap:Bitmap?=null //An instance of the Bitmap
    private var mDrawPaint:Paint?=null //The Paint class holds the style and color information about how to draw
    private var mCanvasPaint:Paint?=null // Instance of canvas paint view
    private var mBrushSize:Float= 0.toFloat() //
    private var color=Color.BLACK //A variable to hold the color of the stroke
    private var canvas:Canvas?=null
    private val mPaths=ArrayList<CustomPath>()

    init {
        setUpDrawing()
    }

    private fun setUpDrawing(){
        mDrawPaint=Paint()
        mDrawPath=CustomPath(color,mBrushSize)
        mDrawPaint!!.color=color
        mDrawPaint!!.style=Paint.Style.STROKE // This is to draw a STROKE style
        mDrawPaint!!.strokeJoin=Paint.Join.ROUND // This is for store join
        mDrawPaint!!.strokeCap=Paint.Cap.ROUND // This is for stroke cap
        mCanvasPaint= Paint(Paint.DITHER_FLAG)
        //mBrushSize=20.toFloat()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasBitmap= Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888)
        canvas=Canvas(mCanvasBitmap!!)
    }
    //change Canvas to canvas? if fails
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(mCanvasBitmap!!,0f,0f,mCanvasPaint)

        for (path in mPaths){
            mDrawPaint!!.strokeWidth=path.brushThickness
            mDrawPaint!!.color=path.color
            canvas.drawPath(path,mDrawPaint!!)
        }


        if (!mDrawPath!!.isEmpty){
            mDrawPaint!!.strokeWidth=mDrawPath!!.brushThickness
            mDrawPaint!!.color=mDrawPath!!.color
            canvas.drawPath(mDrawPath!!,mDrawPaint!!)

        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        val touchX=event?.x
        val touchY=event?.y


        when(event?.action){
            // -> is a lambda expression
            MotionEvent.ACTION_DOWN ->{
                mDrawPath!!.color=color
                mDrawPath!!.brushThickness=mBrushSize

                mDrawPath!!.reset()
                if (touchX != null) {
                    if (touchY != null) {
                        mDrawPath!!.moveTo(touchX,touchY)
                    }
                }
            }
            MotionEvent.ACTION_MOVE ->{
                if (touchY != null) {
                    if (touchX != null) {
                        mDrawPath!!.lineTo(touchX,touchY)
                    }
                }
            }
            MotionEvent.ACTION_UP ->{
                mPaths.add(mDrawPath!!)
                mDrawPath =CustomPath(color,mBrushSize)
            }
            else -> return false
        }

        invalidate()
        return true

    }

     fun setSizeForBrush(newSize : Float){
        mBrushSize= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,newSize,resources.displayMetrics)
        mDrawPaint!!.strokeWidth==mBrushSize
    }

    fun setColor(newColor: String){
        color=Color.parseColor(newColor)
        mDrawPaint!!.color
    }
    // An inner class for custom path with two params as color and stroke size.
    internal inner class CustomPath(var color:Int,var brushThickness:Float):Path() {



    }

}