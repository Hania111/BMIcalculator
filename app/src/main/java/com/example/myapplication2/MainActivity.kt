package com.example.myapplication2

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import com.example.myapplication2.unit.system.ImperialSystem
import com.example.myapplication2.unit.system.MetricSystem
import com.example.myapplication2.unit.system.UnitSystem
import kotlin.math.round


class MainActivity : AppCompatActivity() {

    private var currentInterpretation: String? = null
    private var currentColor : Int? = Color.parseColor("#00000000")
    private var currentBMI : Double = 0.0
    private var currentUnitSystem: UnitSystem = MetricSystem()

    private lateinit var result: TextView
    private lateinit var button_menu: ImageButton
    private lateinit var button_calculate: Button
    private lateinit var heihtMessageTV: TextView
    private lateinit var weightMessageTV: TextView

    private val PREFS_NAME = "bmi_prefs"
    private val sharedPref: SharedPreferences by lazy {
        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        result = findViewById<TextView>(R.id.resultTV)
        button_menu = findViewById<ImageButton>(R.id.ButtonMenu)
        button_calculate = findViewById<Button>(R.id.buttonCalculate)
        heihtMessageTV = findViewById<TextView>(R.id.HeightTV)
        weightMessageTV = findViewById<TextView>(R.id.WeightTV)
        setMessage()

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
        val interpertation  = interpretBMI(currentBMI)
        currentInterpretation = interpertation.first
        currentColor = interpertation.second
        val formattedResult = getString(R.string.bmi_result_format, currentBMI.toString(), currentInterpretation)
        result.text = formattedResult
        result.setBackgroundColor(currentColor!!)
    }

    private fun calculateBMI() {
        val height = findViewById<EditText>(R.id.editTextHeight).text.toString().toDoubleOrNull()
        val weight = findViewById<EditText>(R.id.editTextWeight).text.toString().toDoubleOrNull()
        currentBMI = 0.0
        if (height != null && weight != null) {
            val convertedHeight = currentUnitSystem.convertHeight(height)
            val convertedWeight = currentUnitSystem.convertWeight(weight)
            currentBMI = String.format("%.2f", convertedWeight / (convertedHeight * convertedHeight)).toDouble()
        }
        saveToHistory(weight, height)
    }

    private fun interpretBMI (bmi: Double) : Pair<String, Int> {
        return when {
            bmi < 16 -> Pair("Severely underweight", Color.parseColor("#CC0000"))
            bmi in 16.0..16.99 -> Pair("Underweight", Color.parseColor("#FF6666"))
            bmi in 17.0..18.49 -> Pair("Mildly underweight", Color.parseColor("#FFCCCC"))
            bmi in 18.5..24.99 -> Pair("Normal", Color.parseColor("#93C572"))
            bmi in 25.0..29.99 -> Pair("Overweight", Color.parseColor("#FFCCCC"))
            bmi in 30.0..34.99 -> Pair("Obese Class I", Color.parseColor("#FF6666"))
            bmi in 35.0..39.99 -> Pair("Obese Class II", Color.parseColor("#CC0000"))
            bmi >= 40 -> Pair("Obese Class III", Color.parseColor("#FF0000"))
            else -> Pair("Invalid BMI", Color.parseColor("#FF0000"))
        }
    }

    public fun openBMIdescriptionActivity(){
        if (currentBMI!= 0.0){
            val intent = Intent(this, BMI_description::class.java)
            intent.putExtra("BMI_INTERPRETATION_KEY", currentInterpretation)
            intent.putExtra("BMI_COLOR_KEY", currentColor)
            intent.putExtra("BMI", currentBMI.toString())
            startActivity(intent)
        }
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
                    sideMenu(button_menu)
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

    fun sideMenu(view : View){
        val popup = PopupMenu(this, view)
        popup.menuInflater.inflate(R.menu.sub_popup_menu, popup.menu)

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.metric_option -> {
                    switchToMetricSystem()
                    true
                }
                R.id.imperial_option -> {
                    switchToImperialSystem()
                    true
                }
                else -> false
            }.also {
                if (it) { // If either metric or imperial option was selected
                    clearChoice()
                    setMessage()
                }
            }
        }
        popup.show()

    }

    fun switchToMetricSystem(){
        currentUnitSystem = MetricSystem()
    }
    fun switchToImperialSystem(){
        currentUnitSystem = ImperialSystem()
    }

    fun clearChoice(){
        val height = findViewById<EditText>(R.id.editTextHeight)
        val weight = findViewById<EditText>(R.id.editTextWeight)
        val result = findViewById<TextView>(R.id.resultTV)
        height.text.clear()
        weight.text.clear()
        result.text=" "
        currentColor = Color.parseColor("#00000000")
        result.setBackgroundColor(currentColor!!)
    }

    fun setMessage(){
        heihtMessageTV.text = currentUnitSystem.unitHeightMessage()
        weightMessageTV.text = currentUnitSystem.unitWeightMessage()
    }

    private fun saveToHistory(weight: Double?, height: Double?) {
        val historyList = getHistoryFromPreferences().toMutableList()
        val newData = "$currentBMI, $weight, $height, ${currentUnitSystem.javaClass.simpleName}"

        if (historyList.size >= 10) {
            historyList.removeAt(0)
        }
        historyList.add(newData)

        with(sharedPref.edit()) {
            putStringSet("history", historyList.toSet())
            apply()
        }
    }

    private fun getHistoryFromPreferences(): List<String> {
        return sharedPref.getStringSet("history", emptySet())?.toList() ?: emptyList()
    }






}