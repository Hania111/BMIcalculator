package com.example.myapplication2.unit.system

class ImperialSystem : UnitSystem{
    override fun convertHeight(value: Double): Double {
        // konwersja z m na cale
        //return value * 39.3701
        return value * 0.0254
    }

    override fun convertWeight(value: Double): Double {
        // konwersja z kg na funty
        //return value * 2.20462
        return value * 0.45
    }

    override fun unitHeightMessage(): String {
        return "Enter height in inches"
    }
    override fun unitWeightMessage(): String {
        return "Enter weight in pounds"
    }

    override fun unitHeight(): String {
        return "in"
    }

    override fun unitWeight(): String {
        return "lb"
    }

}