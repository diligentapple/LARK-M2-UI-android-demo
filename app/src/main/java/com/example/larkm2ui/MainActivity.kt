package com.example.larkm2ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt

class MainActivity : AppCompatActivity() {

    private fun create20AudioBars() {
        val audioLevelContainer = findViewById<LinearLayout>(R.id.audio_level)
        audioLevelContainer.removeAllViews()
        for (i in 1..30) {
            val audioBar = View(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    dpToPx(7f),
                    dpToPx(32f)
                ).apply {
                    marginStart = dpToPx(3.5f)
                }
                setBackgroundResource(R.drawable.audio_bar)
                backgroundTintList = if (i < 18) {
                    ColorStateList.valueOf("#00E70C".toColorInt())
                } else if (i < 26) {
                    ColorStateList.valueOf("#FFAC00".toColorInt())
                } else {
                    ColorStateList.valueOf("#FF0000".toColorInt())
                }

            }
            audioLevelContainer.addView(audioBar)
        }
    }

    private fun dpToPx(dp:Float): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()
        create20AudioBars()


    }


}