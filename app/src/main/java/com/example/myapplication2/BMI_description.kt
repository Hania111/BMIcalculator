package com.example.myapplication2

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class BMI_description : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi_description)

        val interpretation = intent.getStringExtra("BMI_INTERPRETATION_KEY")
        val color : Int = intent.getIntExtra("BMI_COLOR_KEY", Color.parseColor("#00000000"))
        val bmi : String? = intent.getStringExtra("BMI")
        val returnButton = findViewById<Button>(R.id.buttonReturn)


        if (interpretation != null && bmi != null && color != null) {
            printBMI(bmi, interpretation, color)
        }
        else printError()

        if (interpretation != null) {
            printBmiInfo(interpretation)
        }

        returnButton.setOnClickListener {
            openMainActivity()
        }

    }

    public fun openMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun getBmiInfo(category: String): String {
        return when (category) {
            "Severely underweight" -> "This is classified as severely underweight. It means that the body weight is significantly below what is recommended for one's height, which may lead to health issues."
            "Underweight" -> "This is classified as underweight. It means that the body weight is below what is recommended for one's height. Individuals in this range might experience a lack of energy or health problems related to malnutrition."
            "Mildly underweight" -> "This is classified as mildly underweight. The body weight is slightly below the norm, but the risk of health issues is lesser than that of being severely underweight."
            "Normal" -> "This is classified as normal weight. Individuals with a BMI in this range generally have the lowest risk of health problems related to their body weight."
            "Overweight" -> "This is classified as overweight. Having a body weight above the recommended range may increase the risk of various health issues."
            "Obese Class I" -> "This is classified as Obese Class I. Individuals in this BMI range face an elevated risk of health problems associated with excessive body weight."
            "Obese Class II" -> "This is classified as Obese Class II. This condition poses an even higher risk of health issues than Class I obesity."
            "Obese Class III" -> "This is classified as Obese Class III, also known as extreme obesity. Individuals with such a BMI have a very high risk of severe health problems related to excessive body weight."
            else -> "Invalid BMI category which doesn't fit into any of the above classifications."
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
        result.text = "you have to calculate your bmi first"
    }



}


