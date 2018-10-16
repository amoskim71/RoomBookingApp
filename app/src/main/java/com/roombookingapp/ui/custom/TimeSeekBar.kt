package com.roombookingapp.ui.custom

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.SeekBar;
import com.roombookingapp.data.model.TimeSlots
import com.roombookingapp.R
class TimeSeekBar : SeekBar {

    private var mProgressItemsList: ArrayList<TimeSlots>? = null

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    fun initData(progressItemsList: ArrayList<TimeSlots>) {
        this.mProgressItemsList = progressItemsList
    }

    @Synchronized
    override fun onMeasure(widthMeasureSpec: Int,
                           heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

     override fun onDraw(canvas: Canvas) {
        if (mProgressItemsList!!.size > 0) {
            val progressBarWidth = width
            val progressBarHeight = height
            val thumboffset = thumbOffset
            var lastProgressX = 0
            var progressItemWidth: Int
            var progressItemRight: Int
            for (i in 0 until mProgressItemsList!!.size) {
                val progressItem = mProgressItemsList!![i]
                val progressPaint = Paint()
                if (progressItem.available) {
                    progressPaint.setColor(context.resources.getColor(R.color.green))
                } else {
                    progressPaint.setColor(context.resources.getColor(R.color.red))
                }


                progressItemWidth = (progressBarWidth / 48).toInt()

                progressItemRight = lastProgressX + progressItemWidth

                // for last item give right to progress item to the width
                if (i == mProgressItemsList!!.size - 1 && progressItemRight != progressBarWidth) {
                    progressItemRight = progressBarWidth
                }
                val progressRect = Rect()
                progressRect.set(lastProgressX, thumboffset / 2,
                        progressItemRight, progressBarHeight - thumboffset / 2)
                canvas.drawRect(progressRect, progressPaint)
                lastProgressX = progressItemRight
            }
            super.onDraw(canvas)
        }

    }





}