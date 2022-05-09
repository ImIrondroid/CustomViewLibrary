package com.iron.libary

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.iron.libary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button1.setOnClickListener {
            moveProgress(0, 5000)
        }

        binding.button2.setOnClickListener {
            moveProgress(5000, 10000)
        }

        binding.button3.setOnClickListener {
            moveProgress(10000, 5000)
        }

        binding.button4.setOnClickListener {
            moveProgress(5000, 0)
        }
    }

    private fun moveProgress(
        nowProgress: Int,
        nextProgress: Int
    ) {
        val progressBar = binding.arcProgressBar
        val propertyName = "progress"
        progressBar.run {
            val animationTest = ObjectAnimator.ofInt(
                binding.arcProgressBar,
                propertyName,
                nowProgress,
                nextProgress
            )

            animationTest.apply {
                duration = 500
            }.start()
        }
    }
}