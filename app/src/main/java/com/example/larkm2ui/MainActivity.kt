package com.example.larkm2ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.content.Intent
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import com.example.larkm2ui.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private fun setToolbar() {
        val toolbar = binding.toolbar
        toolbar.setNavigationOnClickListener {
            finish()
        }
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_settings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.action_recordings -> {
                    val intent = Intent(this, RecordingsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun setAudioBars() {
        binding.audioLevel.removeAllViews()
        for (i in 1..30) {
            val audioBar = View(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    7f.dpToPx(),
                    32f.dpToPx()
                ).apply {
                    marginStart = 3.5f.dpToPx()
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
            binding.audioLevel.addView(audioBar)
        }
    }

    private fun Float.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }

    private fun setSpeakerSwitch() {
        binding.speakerSwitch.setOnCheckedChangeListener { _, isChecked ->
            val message = if (isChecked) "手机外放：开启" else "手机外放：关闭"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setNoiseCancel() {
        binding.ncSwitch.isChecked = true
        var isStrongEnabled = true
        //启动app时默认强降噪
        binding.ncStrong.apply { isSelected = true; refreshDrawableState() }
        binding.ncSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (isStrongEnabled) {
                    binding.ncStrong.apply {isSelected = true; refreshDrawableState()}
                } else {
                    binding.ncWeak.apply {isSelected = true; refreshDrawableState()}
                }
            } else {
                binding.ncStrong.apply {isSelected = false; refreshDrawableState()}
                binding.ncWeak.apply {isSelected = false; refreshDrawableState()}
            }
        }

        binding.ncStrong.setOnClickListener {
            if (binding.ncSwitch.isChecked) {
                if(!isStrongEnabled) {
                    binding.ncStrong.apply {isSelected = true; refreshDrawableState()}
                    binding.ncWeak.apply {isSelected = false; refreshDrawableState()}
                    isStrongEnabled = true
                }
            } else {
                Toast.makeText(this, "请先开启降噪", Toast.LENGTH_SHORT).show()
            }
        }

        binding.ncWeak.setOnClickListener {
            if (binding.ncSwitch.isChecked) {
                if (isStrongEnabled) {
                    binding.ncStrong.apply {isSelected = false; refreshDrawableState()}
                    binding.ncWeak.apply {isSelected = true; refreshDrawableState()}
                    isStrongEnabled = false
                }
            } else {
                Toast.makeText(this, "请先开启降噪", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setVolume() {
        var currentSelectedButton: Button? = null
        val Button1 = binding.volumeButtons.getChildAt(0)
        if (Button1 is Button) {
            Button1.apply {isSelected = true; refreshDrawableState()}
            currentSelectedButton = Button1
        }
        for (i in 0 until binding.volumeButtons.childCount) {
            val button = binding.volumeButtons.getChildAt(i)
            if (button is Button) {
                if (button != Button1) {
                    button.apply {isSelected = false; refreshDrawableState()}
                }
                button.setOnClickListener{
                    if (!getLockState()) {
                        currentSelectedButton?.let { prevButton ->
                            prevButton.apply {isSelected = false; refreshDrawableState()}
                        }
                        button.apply {isSelected = true; refreshDrawableState()}
                        currentSelectedButton = button
                    } else {
                        Toast.makeText(this, "音量已锁定", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            if (button is ImageButton) {
                button.setOnClickListener {
                    val buttonText = if (!getLockState()) "锁定音量" else "解锁音量"
                    Toast.makeText(this, buttonText, Toast.LENGTH_SHORT).show()
                    button.setTag(R.id.tag, !getLockState())
                }
            }
        }
    }
    private fun getLockState(): Boolean {
        return binding.btnLock.getTag(R.id.tag) as? Boolean ?: false
    }

    private fun setChannel() {
        var singleChannelEnabled = true
        //启动app时默认单声道
        binding.singleChannel.apply { isSelected = true; refreshDrawableState() }
        binding.singleChannel.setOnClickListener {
            if (!singleChannelEnabled) {
                binding.singleChannel.apply { isSelected = true; refreshDrawableState() }
                binding.stereoChannel.apply { isSelected = false; refreshDrawableState() }
                singleChannelEnabled = true
            }
        }
        binding.stereoChannel.setOnClickListener {
            if (singleChannelEnabled) {
                binding.stereoChannel.apply { isSelected = true; refreshDrawableState() }
                binding.singleChannel.apply { isSelected = false; refreshDrawableState() }
                singleChannelEnabled = false
            }
        }
    }

    private fun setAutoShutdown() {
        var fifteenMinEnabled = true
        //启动app时默认15分钟自动关机
        binding.btn15min.apply { isSelected = true; refreshDrawableState() }
        binding.btn15min.setOnClickListener {
            if (!fifteenMinEnabled) {
                binding.btn15min.apply { isSelected = true; refreshDrawableState() }
                binding.btnNever.apply { isSelected = false; refreshDrawableState() }
                fifteenMinEnabled = true
            }
        }
        binding.btnNever.setOnClickListener {
            if (fifteenMinEnabled) {
                binding.btnNever.apply { isSelected = true; refreshDrawableState() }
                binding.btn15min.apply { isSelected = false; refreshDrawableState() }
                fifteenMinEnabled = false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        setToolbar()
        setAudioBars()
        setSpeakerSwitch()
        setNoiseCancel()
        setVolume()
        setChannel()
        setAutoShutdown()
    }

}

