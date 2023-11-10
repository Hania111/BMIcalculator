package com.example.myapplication2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.first
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import com.example.myapplication2.unit.system.MetricSystem
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var historyRV: RecyclerView
    private var currentRecord: Int = 0
    private var maxRecords: Int = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        configuration()

        dataStore = createDataStore(name = "history")
        lifecycleScope.launch{
            val records = createListOfBmiRecords()
            //val adapter = HistoryAdapter(records)
            historyRV.adapter = HistoryAdapter(records)
        }


    }

    private fun configuration(){
        currentRecord = intent.getIntExtra("CURRENT_RECORD", 0)
        maxRecords = intent.getIntExtra("MAX_RECORDS", 0)
        historyRV = findViewById<RecyclerView>(R.id.historyRecyclerView)
        historyRV.layoutManager = LinearLayoutManager(this)
        historyRV.setHasFixedSize(true)
    }

    private suspend fun readBmiRecord(key: String) : String? {
        val dataStoreKey = preferencesKey<String>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]// ?: throw NoSuchElementException("No record found for key: $key")
    }

    private fun displayBmiRecords(textView: TextView) {  // to test display record as objects
        val stringBuilder = StringBuilder()
        lifecycleScope.launch {
            val bmiRecordsList = createListOfBmiRecords()
            bmiRecordsList.forEach { record ->
                if (record != null) {
                    stringBuilder.append("BMI: ${record.bmi}, Weight: ${record.weight}${record.weightUnit}, Height: ${record.height}${record.heightUnit}\n")
                } else {
                    stringBuilder.append("No data available\n")
                }
            }
            textView.text = stringBuilder.toString().trim()
        }

    }

    private suspend fun createListOfBmiRecords(): List<BmiRecord> {
        return coroutineScope {
            val limit = currentRecord - maxRecords

            (currentRecord-1 downTo limit).map { i ->
                async {
                    parseBmiRecords(readBmiRecord(i.toString()))
                }
            }.awaitAll().filterNotNull()
        }
    }

    private fun parseBmiRecords(jsonStrings: String?): BmiRecord?{
        var bmiRecord : BmiRecord? = null
        try {
            bmiRecord = jsonStrings?.let { BmiRecord.fromJson(it) }
            return bmiRecord
        } catch (e: JsonSyntaxException){
            Log.e("parseBmiRecords", "Error parsing JSON string: ${e.message}")
            Log.e("parseBmiRecords", "JSON causing error: $jsonStrings")
        }
        return bmiRecord
//        return try {
//            BmiRecord.fromJson(jsonStrings)
//        } catch (e: JsonSyntaxException) {
//            Log.e("parseBmiRecords", "Error parsing JSON string: ${e.message}")
//            Log.e("parseBmiRecords", "JSON causing error: $jsonStrings")
//            throw JsonSyntaxException("Error parsing JSON string: ${e.message}")
//        }
    }




}


