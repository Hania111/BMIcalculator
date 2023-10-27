package com.example.myapplication2

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlin.math.round

class MainActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //getString(R.string.height_with_param)
        val button = findViewById<Button>(R.id.buttonCalculate)
        button.setOnClickListener{
            printBMI()
        }
    }

    private fun printBMI (){
        val bmi = calculateBMI()
        val interpretation = interpretBMI(bmi)
        val result = findViewById<TextView>(R.id.resultTV)
        val formattedResult = getString(R.string.bmi_result_format, bmi.toString(), interpretation.first)
        result.text = formattedResult
        result.setBackgroundColor(Color.parseColor(interpretation.second))
    }

    private fun calculateBMI(): Double {
        val height = findViewById<EditText>(R.id.editTextHeight).text.toString().toDoubleOrNull()
        val weight = findViewById<EditText>(R.id.editTextWeight).text.toString().toDoubleOrNull()
        val result = findViewById<TextView>(R.id.resultTV)
        var bmi: Double = 0.0
        if (height != null && weight != null) {
            bmi = String.format("%.2f",weight / (height * height)).toDouble()
        }
        return bmi
    }

    private fun interpretBMI (bmi: Double) : Pair<String, String>{
        return when {
            bmi < 16 -> Pair("Severely underweight", "#CC0000")
            bmi in 16.0..16.99 -> Pair("Underweight", "#FF6666")
            bmi in 17.0..18.49 -> Pair("Mildly underweight", "#FFCCCC")
            bmi in 18.5..24.99 -> Pair("Normal", "#93C572")
            bmi in 25.0..29.99 -> Pair("Overweight", "#FFCCCC")
            bmi in 30.0..34.99 -> Pair("Obese Class I", "#FF6666")
            bmi in 35.0..39.99 -> Pair("Obese Class II", "#CC0000")
            bmi >= 40 -> Pair("Obese Class III", "#FF0000")
            else -> Pair("Invalid BMI", "#FF0000")
        }
    }

}