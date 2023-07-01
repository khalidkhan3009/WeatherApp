package com.example.weatherapp.model

/**
 * Data class that represents the Weather Information
 */
data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)