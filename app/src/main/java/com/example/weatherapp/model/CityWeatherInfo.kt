package com.example.weatherapp.model

import org.json.JSONArray
import org.json.JSONObject
/**
 * Data class that represents the City Weather Information
 */
data class CityWeatherInfo(
    val name: String = "",
    val weather: List<Weather> = emptyList(),
    val main: Map<String, Double> = hashMapOf()

    //val coord: JSONObject,
    //val base: String,
    //val visibility: Long
    /*val wind: JSONObject,
      val clouds: JSONObject,
      val dt: Long,
      val sys: JSONObject,
      val timeZone: Long,
      val id: Long,
      val cod: Int*/
)