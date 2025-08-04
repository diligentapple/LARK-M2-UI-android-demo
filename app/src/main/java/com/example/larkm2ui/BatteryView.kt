package com.example.larkm2ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class BatteryView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {
    private fun Float.dpToPx(): Float = this * resources.displayMetrics.density

    private var batteryLevel = 75
    private var isCharging = true

    private val batteryFullPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.WHITE
    }
    private val batteryLowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.RED
    }
    private val batteryChargingPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.GREEN
    }
    private val batteryPaint: Paint
        get() = if (isCharging) batteryChargingPaint else if (batteryLevel <= 20) batteryLowPaint else batteryFullPaint

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        batteryRect.set(batteryMargin, batteryMargin, w - 2*batteryMargin, h - batteryMargin)
    }

    private val batteryWidth = 2f.dpToPx() // Width of the battery view
    private val batteryHeight = 17f.dpToPx() // Height of the battery view
    private val batteryMargin = 2f.dpToPx() // Margin around the battery view
    private val batteryRect = android.graphics.RectF(
        batteryMargin,
        batteryMargin,
        batteryWidth - batteryMargin,
        batteryHeight - batteryMargin
    )

    private fun drawBattery(canvas: Canvas) {
        // Calculate the width of the battery level based on the current level
        val levelWidth = (batteryRect.width()) * (batteryLevel / 100f)
        val levelRect = android.graphics.RectF(
            batteryMargin,
            batteryMargin,
            batteryMargin + levelWidth,
            batteryHeight - batteryMargin
        )
        // Radii: top-left, top-right, bottom-right, bottom-left (pairs of X, Y)

        var radii = floatArrayOf(
            6f, 6f,   // top-left
            0f, 0f,   // top-right
            0f, 0f,   // bottom-right
            6f, 6f    // bottom-left
        )
        if (batteryLevel > 90) {
            radii = floatArrayOf(
                6f, 6f,   // top-left
                6f, 6f,   // top-right
                6f, 6f,   // bottom-right
                6f, 6f    // bottom-left
            )
        }

        val path = Path().apply {
            addRoundRect(levelRect, radii, Path.Direction.CW)
        }

        // Draw the battery level
        canvas.drawPath(path, batteryPaint)
    }


    init {
        // Initialize any attributes or properties here if needed
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBattery(canvas)
    }

    fun setBatteryLevel(level: Int) {
        batteryLevel = level
        invalidate() // Redraw the view with the new battery level
    }

    fun setChargingState(charging: Boolean) {
        isCharging = charging
        invalidate() // Redraw the view with the new charging state
    }


}