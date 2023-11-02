package com.example.myapplication2.unit.system

class MetricSystem : UnitSystem{
    override fun convertHeight(value: Double): Double = value // W systemie metrycznym nic nie zmieniamy
    override fun convertWeight(value: Double): Double = value
}