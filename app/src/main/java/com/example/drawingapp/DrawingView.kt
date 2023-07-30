package com.example.drawingapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView: View {
    private var imageWidth : Int=0
    private var imageHeight : Int=0
    private var brushStrokeWidth: Float = 5f
    private var brushStrokeColor:Int = Color.BLACK
    private var path:Path = Path()
    private var pathsArray:ArrayList<Path> = arrayListOf()
    private var paint:Paint = Paint(Paint.DITHER_FLAG)
    private lateinit var bitmap:Bitmap
    private lateinit var tempCanvas:Canvas
    constructor(context:Context):this(context,null){}
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs,0){}
    constructor(context: Context, attrs: AttributeSet?,defStyle:Int) : super(context, attrs,defStyle){}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = View.resolveSizeAndState(minw, widthMeasureSpec, 1)

        val minh: Int = View.MeasureSpec.getSize(w) + paddingBottom + paddingTop
        val h: Int = View.resolveSizeAndState(minh, heightMeasureSpec, 0)

        setMeasuredDimension(w, h)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        imageWidth = w
        imageHeight = h
        initView(imageWidth,imageHeight)
    }

    private fun initView(width:Int,height:Int){
        bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888)
        tempCanvas = Canvas(bitmap)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x=event.x
        val y=event.y
        when(event.action){
            MotionEvent.ACTION_DOWN->{
                path.moveTo(x,y)
                invalidate()
                return true
            }
            MotionEvent.ACTION_MOVE->{
                path.lineTo(x,y)
                pathsArray.add(path)
                invalidate()
                return true
            }
            MotionEvent.ACTION_UP->{
                invalidate()
                return true
            }
        }
        return false
    }

    override fun onDraw(canvas: Canvas) {
        tempCanvas.drawColor(Color.WHITE)
        for(i in pathsArray){
            paint.color = brushStrokeColor
            paint.strokeWidth = brushStrokeWidth
            paint.strokeJoin = Paint.Join.ROUND
            paint.style = Paint.Style.STROKE
            tempCanvas.drawPath(i,paint)
        }
        canvas.drawBitmap(bitmap,0f,0f,paint)

    }

    fun getBitmap()=bitmap

    fun clearAll() {
        if(pathsArray.size>0){
            pathsArray.clear()
            path.reset()
            invalidate()
        }
    }
}