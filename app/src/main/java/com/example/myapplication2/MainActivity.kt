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
import android.widget.Toast
import androidx.datastore.core.DataStore
import com.example.myapplication2.unit.system.ImperialSystem
import com.example.myapplication2.unit.system.MetricSystem
import com.example.myapplication2.unit.system.UnitSystem

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import com.example.myapplication2.unit.system.UnitSystemType
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

    private lateinit var bmiCalculator: BmiCalculator
    private lateinit var dataStore: DataStore<androidx.datastore.preferences.core.Preferences>
    private lateinit var bmiRecordManager: BmiRecordManager

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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        latestBmiRecord?.let {
            outState.putString("SavedBmiRecord", it.toJson())
        }
        outState.putInt("CurrentRecord", currentRecord)
        val unitSystemType = when (currentUnitSystem) {
            is MetricSystem -> UnitSystemType.METRIC.typeName
            is ImperialSystem -> UnitSystemType.IMPERIAL.typeName
            else -> throw IllegalArgumentException("Unknown UnitSystem type")
        }
        outState.putString("CurrentUnitSystemType", unitSystemType)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.getString("SavedBmiRecord")?.let {
            latestBmiRecord = BmiRecord.fromJson(it)
        }
        currentRecord = savedInstanceState.getInt("CurrentRecord", 0)
        val unitSystemType = savedInstanceState.getString("CurrentUnitSystemType")
        currentUnitSystem = when (unitSystemType) {
            UnitSystemType.METRIC.typeName -> MetricSystem()
            UnitSystemType.IMPERIAL.typeName -> ImperialSystem()
            else -> throw IllegalArgumentException("Unknown UnitSystem type")
        }
        printBMI()
        setMessage()
    }

    private fun handleCalculateButtonClick(){
        val height = findViewById<EditText>(R.id.editTextHeight).text.toString().toDoubleOrNull()
        val weight = findViewById<EditText>(R.id.editTextWeight).text.toString().toDoubleOrNull()
        if (height == null || weight == null || height == 0.0 || weight == 0.0) {
            Toast.makeText(this,
                getString(R.string.please_enter_valid_height_and_weight_values), Toast.LENGTH_LONG).show()
            return
        }
        latestBmiRecord = bmiRecordManager.createBmiRecord(height, weight, currentUnitSystem)
        printBMI()

        lifecycleScope.launch{
            latestBmiRecord?.let { record ->
                bmiRecordManager.saveBmiRecord(currentRecord.toString(), record)
            }
        }
        currentRecord += 1
    }


    private fun configuration(){
        result = findViewById<TextView>(R.id.resultTV)
        button_menu = findViewById<ImageButton>(R.id.ButtonMenu)
        button_calculate = findViewById<Button>(R.id.buttonCalculate)
        heihtMessageTV = findViewById<TextView>(R.id.HeightTV)
        weightMessageTV = findViewById<TextView>(R.id.WeightTV)
        setMessage()
        dataStore = createDataStore(name = getString(R.string.historyDS))
        bmiCalculator = BmiCalculator(this)
        bmiRecordManager = BmiRecordManager(this, dataStore)
    }


    private fun printBMI (){
        if(latestBmiRecord!= null){
            val interpertation  = bmiCalculator.interpretBMI(latestBmiRecord!!.bmi)
            currentInterpretation = interpertation.first
            currentColor = interpertation.second
            val formattedResult = getString(R.string.bmi_result_format, latestBmiRecord!!.bmi.toString(), currentInterpretation)
            result.text = formattedResult
            result.setBackgroundColor(currentColor!!)
        }
    }


    fun openBMIdescriptionActivity(){
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