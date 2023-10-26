package com.example.myapplication2

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //getString(R.string.height_with_param)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener{
            val textView = findViewById<TextView>(R.id.heighTextView)
            textView.text = getString(R.string.android)
        }
    }
}