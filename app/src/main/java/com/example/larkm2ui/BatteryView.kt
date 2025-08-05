package com.example.larkm2ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.text.toInt


class BatteryView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {
    private fun Float.dpToPx(): Float = this * resources.displayMetrics.density

    private var batteryLevel = 75
    private var isCharging = true

    private val batteryPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private var lowLevelColor = Color.RED
    private var normalLevelColor = Color.WHITE
    private var chargingColor = Color.GREEN

    private val batteryWidth = 20f.dpToPx() // Width of the battery view
    private val batteryHeight = 16f.dpToPx() // Height of the battery view
    private val batteryMargin = 2f.dpToPx() // Margin around the battery view
    private val batteryRect: android.graphics.RectF = android.graphics.RectF()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        batteryRect.set(
            batteryMargin + paddingLeft,
            batteryMargin + paddingTop,
            batteryMargin + paddingLeft + batteryWidth,
            batteryMargin + paddingTop + batteryHeight)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth  = (batteryWidth + paddingLeft + paddingRight).toInt()
        val desiredHeight = (batteryHeight + paddingTop + paddingBottom).toInt()
        val measuredW = resolveSize(desiredWidth, widthMeasureSpec)
        val measuredH = resolveSize(desiredHeight, heightMeasureSpec)

        setMeasuredDimension(measuredW, measuredH)
    }

    init {
        initAttributes(context, attrs)
        initView()
    }

    private fun initAttributes(context: Context, attrs: AttributeSet?) {
        // Initialize any custom attributes if needed
        attrs?.let {
            context.withStyledAttributes(it, R.styleable.BatteryView) {
                lowLevelColor = getColor(R.styleable.BatteryView_lowLevelColor, Color.RED)
                normalLevelColor= getColor(R.styleable.BatteryView_normalLevelColor, Color.WHITE)
                chargingColor = getColor(R.styleable.BatteryView_chargingColor, Color.GREEN)
            }
        }
    }

    private fun initView() {
        setBackgroundResource(R.drawable.battery_shape2)
    }

    private fun drawBattery(canvas: Canvas) {
        batteryPaint.color = when {
            isCharging         -> chargingColor
            batteryLevel <= 20 -> lowLevelColor
            else               -> normalLevelColor
        }

        // Calculate the width of the battery level based on the current level
        val levelWidth = (batteryRect.width()) * (batteryLevel / 100f)
        val levelRect = android.graphics.RectF(
            batteryMargin,
            batteryMargin,
            batteryMargin + levelWidth,
            batteryHeight - batteryMargin
        )

        var radii = floatArrayOf(
            6f, 6f,   // top-left
            0f, 0f,   // top-right
            0f, 0f,   // bottom-right
            6f, 6f    // bottom-left
        )
        if (batteryLevel > 95) {
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

        canvas.drawPath(path, batteryPaint)
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