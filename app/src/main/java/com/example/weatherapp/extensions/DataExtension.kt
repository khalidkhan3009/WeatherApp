package com.example.weatherapp.extensions

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.toUpperCase
import com.example.weatherapp.constant.Constants
import com.example.weatherapp.model.CityWeatherInfo
import java.util.Locale

fun Double.toCelsius() = "${(this - 273.15f).toInt()}℃"
fun String?.getStateOrEmpty(): String {
    return if(!this.isNullOrEmpty()) "$this, " else ""
}

fun CityWeatherInfo.getTemperature() = this.main[Constants.TEMP]?.toCelsius()
fun CityWeatherInfo.getFeelsLike() = this.main[Constants.FEELS_LIKE]?.toCelsius()
fun CityWeatherInfo.getDescription(): String {
    return if(this.weather.isNotEmpty()){
        weather[0].description.capitalize(LocaleList.current)
    } else {
        ""
    }
}

fun CityWeatherInfo.getMinTemp() = this.main[Constants.MIN_TEMP]?.toCelsius()
fun CityWeatherInfo.getMaxTemp() = this.main[Constants.MAX_TEMP]?.toCelsius()



