package com.iron.library

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.iron.library.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomSheet = ColorPickerBottomSheetDialogFragment().apply {
            setOnSelectListener {
                //doSomething
            }
        }
        bottomSheet.show(supportFragmentManager, ColorPickerBottomSheetDialogFragment.TAG)
    }
}