package com.example.myapplication2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView

class AboutAuthorActivity : AppCompatActivity() {

    private lateinit var mountainIB : ImageButton
    private lateinit var dogIB : ImageButton
    private lateinit var mottoIB : ImageButton
    private lateinit var meIB : ImageButton
    private lateinit var authorDescriptionTV : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_author)
        initializeViews()
        printAuthorInfo()

        mountainIB.setOnClickListener{ printMountainInfo() }
        dogIB.setOnClickListener{ printDogInfo() }
        mottoIB.setOnClickListener{ printMottoInfo() }
        meIB.setOnClickListener{ printAuthorInfo() }


    }

    fun initializeViews(){
        mountainIB = findViewById<ImageButton>(R.id.mountainsIB)
        dogIB = findViewById<ImageButton>(R.id.dogIB)
        mottoIB = findViewById<ImageButton>(R.id.mottoIB)
        meIB = findViewById<ImageButton>(R.id.meIB)
        authorDescriptionTV = findViewById<TextView>(R.id.authorDescriptionTV)
        printAuthorInfo()
    }
    fun printAuthorInfo() {
    authorDescriptionTV.text = getString(R.string.author_info)
}

    fun printDogInfo() {
        authorDescriptionTV.text = getString(R.string.dog_info)
    }

    fun printMountainInfo() {
        authorDescriptionTV.text = getString(R.string.mountain_info)
    }

    fun printMottoInfo() {
        authorDescriptionTV.text = getString(R.string.motto_info)
    }



}