package com.example.myapplication2

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import com.example.myapplication2.unit.system.UnitSystem

class BmiRecordManager(private val context: Context,
                       private val dataStore: DataStore<Preferences>) {
    private val bmiCalculator = BmiCalculator(context)

    suspend fun saveBmiRecord(key: String, bmiRecord: BmiRecord) {
        val dataStoreKey = preferencesKey<String>(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = bmiRecord.toJson() // Założenie, że masz metodę toJson()
        }
    }

    fun createBmiRecord(height: Double, weight: Double, unitSystem: UnitSystem): BmiRecord {
        val bmi = bmiCalculator.calculateBMI(height, weight, unitSystem)
        return BmiRecord(bmi, weight, height, unitSystem.unitWeight(), unitSystem.unitHeight())
    }

    fun interpretBMI(bmi: Double): Pair<String, Int> {
        return bmiCalculator.interpretBMI(bmi)
    }

}