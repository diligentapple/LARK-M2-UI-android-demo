package com.example.larkm2ui

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
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
        binding.ncStrong.isSelected = true
        binding.ncSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (isStrongEnabled) {
                    binding.ncStrong.isSelected = true
                } else {
                    binding.ncWeak.isSelected = true
                }
            } else {
                binding.ncStrong.isSelected = false
                binding.ncWeak.isSelected = false
            }
        }

        binding.ncStrong.setOnClickListener {
            if (binding.ncSwitch.isChecked) {
                if(!isStrongEnabled) {
                    binding.ncStrong.isSelected = true
                    binding.ncWeak.isSelected = false
                    isStrongEnabled = true
                }
            } else {
                Toast.makeText(this, "请先开启降噪", Toast.LENGTH_SHORT).show()
            }
        }

        binding.ncWeak.setOnClickListener {
            if (binding.ncSwitch.isChecked) {
                if (isStrongEnabled) {
                    binding.ncStrong.isSelected = false
                    binding.ncWeak.isSelected = true
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
            Button1.isSelected = true
            currentSelectedButton = Button1
        }
        for (i in 0 until binding.volumeButtons.childCount) {
            val button = binding.volumeButtons.getChildAt(i)
            if (button is Button) {
                if (button != Button1) {
                    button.isSelected = false
                }
                button.setOnClickListener{
                    if (!getLockState()) {
                        currentSelectedButton?.let { prevButton ->
                            prevButton.isSelected = false
                        }
                        button.isSelected = true
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
        binding.singleChannel.isSelected = true
        binding.singleChannel.setOnClickListener {
            if (!singleChannelEnabled) {
                binding.singleChannel.isSelected = true
                binding.stereoChannel.isSelected = false
                singleChannelEnabled = true
            }
        }
        binding.stereoChannel.setOnClickListener {
            if (singleChannelEnabled) {
                binding.stereoChannel.isSelected = true
                binding.singleChannel.isSelected = false
                singleChannelEnabled = false
            }
        }
    }

    private fun setAutoShutdown() {
        var fifteenMinEnabled = true
        //启动app时默认15分钟自动关机
        binding.btn15min.isSelected = true
        binding.btn15min.setOnClickListener {
            if (!fifteenMinEnabled) {
                binding.btn15min.isSelected = true
                binding.btnNever.isSelected = false
                fifteenMinEnabled = true
            }
        }
        binding.btnNever.setOnClickListener {
            if (fifteenMinEnabled) {
                binding.btnNever.isSelected = true
                binding.btn15min.isSelected = false
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

