package com.example.myapplication2

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class BmiDescriptionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi_description)

        val interpretation = intent.getStringExtra("BMI_INTERPRETATION_KEY")
        val color : Int = intent.getIntExtra("BMI_COLOR_KEY", Color.parseColor("#00000000"))
        val bmi : String? = intent.getStringExtra("BMI")


        if (interpretation != null && bmi != null) {
            printBMI(bmi, interpretation, color)
        }
        else printError()

        if (interpretation != null) {
            printBmiInfo(interpretation)
        }

    }

fun getBmiInfo(category: String): String {
    return when (category) {
        getString(R.string.bmi_category_severely_underweight) -> getString(R.string.bmi_severely_underweight)
        getString(R.string.bmi_category_underweight)-> getString(R.string.bmi_underweight)
        getString(R.string.bmi_category_mildly_underweight) -> getString(R.string.bmi_mildly_underweight)
        getString(R.string.bmi_category_normal) -> getString(R.string.bmi_normal)
        getString(R.string.bmi_category_overweight) -> getString(R.string.bmi_overweight)
        getString(R.string.bmi_category_obese_class_i)-> getString(R.string.bmi_obese_class_i)
        getString(R.string.bmi_category_obese_class_ii) -> getString(R.string.bmi_obese_class_ii)
        getString(R.string.bmi_category_obese_class_iii) -> getString(R.string.bmi_obese_class_iii)
        else -> getString(R.string.bmi_invalid_category)
    }
}


    fun printBmiInfo(category: String){
        val description = findViewById<TextView>(R.id.descriptionTV)
        description.text = getBmiInfo(category)
    }

    fun printBMI (Bmi: String, cateogry: String, color: Int){
        val result = findViewById<TextView>(R.id.resultTV)
        val formattedResult = getString(R.string.bmi_result_format, Bmi, cateogry)
        result.text = formattedResult
        result.setBackgroundColor(color)
    }

    fun printError (){
        val result = findViewById<TextView>(R.id.resultTV)
        result.text = getString(R.string.ErrorDescription)
    }



}


