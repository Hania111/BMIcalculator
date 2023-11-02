package com.example.myapplication2

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import kotlin.math.round

class MainActivity : AppCompatActivity() {

    private var currentInterpretation: Pair<String, String>? = null
    private var currentBMI : Double = 0.0
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val result = findViewById<TextView>(R.id.resultTV)
        val button_menu = findViewById<ImageButton>(R.id.ButtonMenu)

        val button_calculate = findViewById<Button>(R.id.buttonCalculate)
        button_calculate.setOnClickListener{
            printBMI(result)
        }

        result.setOnClickListener {
            openBMIdescriptionActivity()
        }

        button_menu.setOnClickListener(){
            menu(button_menu)
        }
    }

    private fun printBMI (result : TextView){
        calculateBMI()
        currentInterpretation = interpretBMI(currentBMI)
        val formattedResult = getString(R.string.bmi_result_format, currentBMI.toString(), currentInterpretation!!.first)
        result.text = formattedResult
        result.setBackgroundColor(Color.parseColor(currentInterpretation!!.second))
    }

    private fun calculateBMI() {
        val height = findViewById<EditText>(R.id.editTextHeight).text.toString().toDoubleOrNull()
        val weight = findViewById<EditText>(R.id.editTextWeight).text.toString().toDoubleOrNull()
        currentBMI = 0.0
        if (height != null && weight != null) {
            currentBMI = String.format("%.2f", weight / (height * height)).toDouble()
        }
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

    public fun openBMIdescriptionActivity(){
        val intent = Intent(this, BMI_description::class.java)
        intent.putExtra("BMI_INTERPRETATION_KEY", currentInterpretation?.first)
        intent.putExtra("BMI_COLOR_KEY", currentInterpretation?.second)
        intent.putExtra("BMI", currentBMI.toString())
        startActivity(intent)
    }

    fun openHistoryActivity(){
        val intent = Intent(this, history::class.java)
        startActivity(intent)
    }

    fun openAboutAuthorActivity(){
        val intent = Intent(this, About_author::class.java)
        startActivity(intent)
    }
    fun menu(button_menu : ImageButton){
        val popup = PopupMenu(this, button_menu)
        popup.menuInflater.inflate(R.menu.popup_menu, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.history_option -> {
                    openHistoryActivity()
                    true
                }
                R.id.switch_metrics -> {
                    true
                }
                R.id.about_author -> {
                    openAboutAuthorActivity()
                    true
                }
                else -> false
            }
        }
        popup.show()
    }
}