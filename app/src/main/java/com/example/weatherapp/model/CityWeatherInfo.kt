package com.example.weatherapp.model

import org.json.JSONArray
import org.json.JSONObject

data class CityWeatherInfo(
    //val coord: JSONObject,
    val weather: List<Weather> = emptyList(),
    //val base: String,
    val main: Map<String, Double> = hashMapOf(),
    //val visibility: Long
    /*    val wind: JSONObject,
        val clouds: JSONObject,
        val dt: Long,
        val sys: JSONObject,
        val timeZone: Long,
        val id: Long,*/
    val name: String = ""
    //val cod: Int
)