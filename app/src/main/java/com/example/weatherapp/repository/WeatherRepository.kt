package com.example.weatherapp.repository

import com.example.weatherapp.constant.Constants
import com.example.weatherapp.model.City
import com.example.weatherapp.model.CityWeatherInfo
import com.example.weatherapp.service.WeatherService

class WeatherRepository {
    private val weatherService by lazy { WeatherService.create() }
    suspend fun getListOfCities(inputVal: String): List<City> {
        return weatherService.getGeoCordsByCityName(inputVal)
    }

    suspend fun getWeatherInfoByCity(city: City): CityWeatherInfo {
        return weatherService.getWeatherInfoByCity(city.lat, city.lon)
    }

    fun getWeatherIcon(cityWeatherInfo: CityWeatherInfo): String {
        val weatherList = cityWeatherInfo.weather
        return if(weatherList.isNotEmpty())
            Constants.ICON_URL.replace("icon", weatherList[0].icon)
        else ""
    }
}