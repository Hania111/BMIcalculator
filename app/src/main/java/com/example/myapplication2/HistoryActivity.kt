package com.example.myapplication2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private lateinit var historrRV : RecyclerView
    private lateinit var returnButton: Button
    private lateinit var testTV : TextView
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var historyTV: TextView
    private lateinit var historyRV: RecyclerView
    private var currentRecord: Int = 0
    private var maxRecords: Int = 0

    private val PREFS_NAME = "bmi_prefs"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        currentRecord = intent.getIntExtra("CURRENT_RECORD", 0)
        maxRecords = intent.getIntExtra("MAX_RECORDS", 0)

        historyTV = findViewById<TextView>(R.id.historyTV)
        historyRV = findViewById(R.id.historyRecyclerView)
        historyRV.layoutManager = LinearLayoutManager(this)
        returnButton = findViewById<Button>(R.id.buttonReturn)
        returnButton.setOnClickListener {
            openMainActivity()
        }

        dataStore = createDataStore(name = "history")
        //displayBmiRecords(historyTV)

        lifecycleScope.launch{
            val records = createListOfBmiRecords()
            val adapter = HistoryAdapter(records)
            historrRV.adapter = adapter
        }


    }




    public fun openMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private suspend fun readBmiRecord(key: String) : String? {
        val dataStoreKey = preferencesKey<String>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }

    private fun displayBmiRecords(textView: TextView) {
        lifecycleScope.launch {
            val bmiRecordsList = createListOfBmiRecords()
            textView.text = bmiRecordsList.joinToString(separator = "\n") { it ?: "Brak danych" }
        }
    }

    private suspend fun createListOfBmiRecords(): List<String?> {
        return coroutineScope {
            (0 until maxRecords).map { i ->
                async {
                    readBmiRecord(i.toString())
                }
            }.awaitAll()
        }
    }






}


