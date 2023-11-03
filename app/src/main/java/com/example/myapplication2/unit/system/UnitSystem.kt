package com.example.myapplication2.unit.system

interface UnitSystem {
    fun convertHeight(value: Double): Double
    fun convertWeight(value: Double): Double
    fun unitHeightMessage():String
    fun unitWeightMessage():String
}