package com.example.weatherapp.extensions

fun Float.toCelsius() = "${(this - 273.15f).toInt()}℃"
fun String?.getStateOrEmpty(): String {
    return if(!this.isNullOrEmpty()) "$this, " else ""
}