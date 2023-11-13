package com.example.myapplication2

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.datastore.core.DataStore
import com.example.myapplication2.unit.system.ImperialSystem
import com.example.myapplication2.unit.system.MetricSystem
import com.example.myapplication2.unit.system.UnitSystem

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
//import com.plcoding.datastoreandroid.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private var currentInterpretation: String? = null
    private var currentColor : Int? = Color.parseColor("#00000000")
    private var currentUnitSystem: UnitSystem = MetricSystem()
    private val maxRecords = 10
    private var currentRecord = 0
    private var latestBmiRecord : BmiRecord? = null

    private lateinit var result: TextView
    private lateinit var button_menu: ImageButton
    private lateinit var button_calculate: Button
    private lateinit var heihtMessageTV: TextView
    private lateinit var weightMessageTV: TextView


    private lateinit var dataStore: DataStore<androidx.datastore.preferences.core.Preferences>

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configuration()

        button_calculate.setOnClickListener{
            handleCalculateButtonClick()
        }

        result.setOnClickListener {
            openBMIdescriptionActivity()
        }

        button_menu.setOnClickListener(){
            menu(button_menu)
        }
    }

    private fun handleCalculateButtonClick(){
        latestBmiRecord = createBMIRecord()  // wyliczenia bmi
        printBMI()
        lifecycleScope.launch{
            latestBmiRecord?.let { it1 -> saveBmiRecord(currentRecord.toString(), it1) }
        }
        currentRecord += 1
    }
    private fun createBMIRecord(): BmiRecord{
        val height = findViewById<EditText>(R.id.editTextHeight).text.toString().toDoubleOrNull()
        val weight = findViewById<EditText>(R.id.editTextWeight).text.toString().toDoubleOrNull()
        return BmiRecord(calculateBMI(height,weight), weight, height, currentUnitSystem.unitWeight(), currentUnitSystem.unitHeight())
    }

    private fun configuration(){
        result = findViewById<TextView>(R.id.resultTV)
        button_menu = findViewById<ImageButton>(R.id.ButtonMenu)
        button_calculate = findViewById<Button>(R.id.buttonCalculate)
        heihtMessageTV = findViewById<TextView>(R.id.HeightTV)
        weightMessageTV = findViewById<TextView>(R.id.WeightTV)
        setMessage()
        dataStore = createDataStore(name = getString(R.string.historyDS))
    }

    private suspend fun saveBmiRecord(key: String, bmiRecord: BmiRecord){
        val dataStoreKey = preferencesKey<String>(key)
        dataStore.edit {settings ->
            settings[dataStoreKey] = bmiRecord.toJson()
        }
    }

    private suspend fun readBmiRecord(key: String) : String? {
        val dataStoreKey = preferencesKey<String>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }

    private fun printBMI (){
        val interpertation  = interpretBMI(latestBmiRecord!!.bmi)
        currentInterpretation = interpertation.first
        currentColor = interpertation.second
        val formattedResult = getString(R.string.bmi_result_format, latestBmiRecord!!.bmi.toString(), currentInterpretation)
        result.text = formattedResult
        result.setBackgroundColor(currentColor!!)
    }

    private fun calculateBMI(height : Double?, weight : Double?):Double {
        var currentBMI = 0.0
        if (height != null && weight != null) {
            val convertedHeight = currentUnitSystem.convertHeight(height)
            val convertedWeight = currentUnitSystem.convertWeight(weight)
            currentBMI = String.format("%.2f", convertedWeight / (convertedHeight * convertedHeight)).toDouble()
        }
        return currentBMI
    }

    private fun interpretBMI (bmi: Double) : Pair<String, Int> {
        return when {
            bmi < 16 -> Pair(getString(R.string.bmi_category_severely_underweight), Color.parseColor(getString(R.string.category_dark_red)))
            bmi in 16.0..16.99 -> Pair(getString(R.string.bmi_category_underweight), Color.parseColor(getString(R.string.category_red)))
            bmi in 17.0..18.49 -> Pair(getString(R.string.bmi_category_mildly_underweight), Color.parseColor(getString(R.string.category_pink)))
            bmi in 18.5..24.99 -> Pair( getString(R.string.bmi_category_normal), Color.parseColor(getString(R.string.category_normal)))
            bmi in 25.0..29.99 -> Pair(getString(R.string.bmi_category_overweight), Color.parseColor(getString(R.string.category_pink)))
            bmi in 30.0..34.99 -> Pair( getString(R.string.bmi_category_obese_class_i), Color.parseColor(getString(R.string.category_red)))
            bmi in 35.0..39.99 -> Pair(getString(R.string.bmi_category_obese_class_ii),Color.parseColor(getString(R.string.category_dark_red)))
            bmi >= 40 -> Pair( getString(R.string.bmi_category_obese_class_iii), Color.parseColor(getString(R.string.category_dark_dark_red)))
            else -> Pair(getString(R.string.invalid_bmi), Color.parseColor(getString(R.string.category_dark_dark_red)))
        }
    }

    public fun openBMIdescriptionActivity(){
        if (latestBmiRecord!!.bmi!= 0.0){
            val intent = Intent(this, BmiDescriptionActivity::class.java)
            intent.putExtra("BMI_INTERPRETATION_KEY", currentInterpretation)
            intent.putExtra("BMI_COLOR_KEY", currentColor)
            intent.putExtra("BMI", latestBmiRecord!!.bmi.toString())
            startActivity(intent)
        }
    }

    fun openHistoryActivity(){
        val intent = Intent(this, HistoryActivity::class.java)
        intent.putExtra("CURRENT_RECORD", currentRecord)
        intent.putExtra("MAX_RECORDS", maxRecords)
        startActivity(intent)
    }

    fun openAboutAuthorActivity(){
        val intent = Intent(this, AboutAuthorActivity::class.java)
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


}