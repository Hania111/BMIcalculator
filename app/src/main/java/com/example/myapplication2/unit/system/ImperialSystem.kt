package com.example.myapplication2.unit.system

class ImperialSystem : UnitSystem{
    override fun convertHeight(value: Double): Double {
        // konwersja z cm na cale
        return value * 0.393701
    }

    override fun convertWeight(value: Double): Double {
        // konwersja z kg na funty
        return value * 2.20462
    }
}