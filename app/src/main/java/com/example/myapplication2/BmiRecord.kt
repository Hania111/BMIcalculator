package com.example.myapplication2

import com.example.myapplication2.unit.system.UnitSystem
import com.google.gson.Gson

data class BmiRecord(
    val bmi: Double,
    val weight: Double?,
    val height: Double?,
    val unitSystem: UnitSystem?,
    //val timestamp: Long // można użyć do sortowania i usuwania najstarszych rekordów
){
    fun toJson(): String {
        return Gson().toJson(this)
    }

    companion object {
        fun fromJson(json: String): BmiRecord {
            return Gson().fromJson(json, BmiRecord::class.java)
        }
    }
}
