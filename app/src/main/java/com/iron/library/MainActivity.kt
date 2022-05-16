package com.iron.library

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import com.iron.library.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.button1.setOnClickListener {
//            moveProgress(binding.arcProgressBar1 ,0, 5000)
//        }
//
//        binding.button2.setOnClickListener {
//            moveProgress(binding.arcProgressBar1, 5000, 10000)
//        }
//
//        binding.button3.setOnClickListener {
//            moveProgress(binding.arcProgressBar1, 10000, 5000)
//        }
//
//        binding.button4.setOnClickListener {
//            moveProgress(binding.arcProgressBar1,5000, 0)
//        }
//
//        binding.button5.setOnClickListener {
//            moveProgress(binding.arcProgressBar2, 0, 5000)
//        }
//
//        binding.button6.setOnClickListener {
//            moveProgress(binding.arcProgressBar2, 5000, 10000)
//        }
//
//        binding.button7.setOnClickListener {
//            moveProgress(binding.arcProgressBar2, 10000, 5000)
//        }
//
//        binding.button8.setOnClickListener {
//            moveProgress(binding.arcProgressBar2, 5000, 0)
//        }
    }

    private fun moveProgress(
        progressBar: ProgressBar,
        nowProgress: Int,
        nextProgress: Int
    ) {
        val propertyName = "progress"
        progressBar.run {
            val animationTest = ObjectAnimator.ofInt(
                progressBar,
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