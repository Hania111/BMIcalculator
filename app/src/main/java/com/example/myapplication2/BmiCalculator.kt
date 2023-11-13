package com.example.myapplication2

import android.content.Context
import android.graphics.Color
import com.example.myapplication2.unit.system.UnitSystem

class BmiCalculator(private val context: Context) {
    fun calculateBMI(height : Double?, weight : Double?, unitSystem: UnitSystem):Double {
        var currentBMI = 0.0
        if (height != null && weight != null) {
            val convertedHeight = unitSystem.convertHeight(height)
            val convertedWeight = unitSystem.convertWeight(weight)
            currentBMI = String.format("%.2f", convertedWeight / (convertedHeight * convertedHeight)).toDouble()
        }
        return currentBMI
    }

    fun interpretBMI (bmi: Double) : Pair<String, Int> {
        return when {
            bmi < 16 -> Pair(context.getString(R.string.bmi_category_severely_underweight), Color.parseColor(context.getString(R.string.category_dark_red)))
            bmi in 16.0..16.99 -> Pair(context.getString(R.string.bmi_category_underweight), Color.parseColor(context.getString(R.string.category_red)))
            bmi in 17.0..18.49 -> Pair(context.getString(R.string.bmi_category_mildly_underweight), Color.parseColor(context.getString(R.string.category_pink)))
            bmi in 18.5..24.99 -> Pair(context.getString(R.string.bmi_category_normal), Color.parseColor(context.getString(R.string.category_normal)))
            bmi in 25.0..29.99 -> Pair(context.getString(R.string.bmi_category_overweight), Color.parseColor(context.getString(R.string.category_pink)))
            bmi in 30.0..34.99 -> Pair(context.getString(R.string.bmi_category_obese_class_i), Color.parseColor(context.getString(R.string.category_red)))
            bmi in 35.0..39.99 -> Pair(context.getString(R.string.bmi_category_obese_class_ii), Color.parseColor(context.getString(R.string.category_dark_red)))
            bmi >= 40 -> Pair(context.getString(R.string.bmi_category_obese_class_iii), Color.parseColor(context.getString(R.string.category_dark_dark_red)))
            else -> Pair(context.getString(R.string.invalid_bmi), Color.parseColor(context.getString(R.string.category_dark_dark_red)))
        }
    }
}