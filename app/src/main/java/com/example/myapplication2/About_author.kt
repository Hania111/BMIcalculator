package com.example.myapplication2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class About_author : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_author)
        printAuthorInfo()

        val returnButton = findViewById<Button>(R.id.buttonReturn)
        returnButton.setOnClickListener {
            openMainActivity()
        }
    }

    fun printAuthorInfo(){
        val authorDescriptionTV = findViewById<TextView>(R.id.authorDescriptionTV)
        authorDescriptionTV.text = "I am a student in my second year of Applied Computer Science at Wrocław University of \n" +
                "Science and Technology. I am also a scout leader at 23th Wrocławska Drużyna Harcerska \n" +
                "\"Wilki\"."
    }

    public fun openMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}