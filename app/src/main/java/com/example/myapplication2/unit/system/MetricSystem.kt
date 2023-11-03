package com.example.myapplication2.unit.system

class MetricSystem : UnitSystem{
    override fun convertHeight(value: Double): Double = value // W systemie metrycznym nic nie zmieniamy
    override fun convertWeight(value: Double): Double = value
    override fun unitHeightMessage(): String {
       return "Enter height in meters"
    }
    override fun unitWeightMessage(): String {
        return "Enter weight in kg"
    }

}