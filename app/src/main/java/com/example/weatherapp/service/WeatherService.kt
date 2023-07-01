package com.example.weatherapp.service

import com.example.weatherapp.constant.Constants
import com.example.weatherapp.model.City
import com.example.weatherapp.model.CityWeatherInfo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherService {

    @GET("/geo/1.0/direct?limit=5")
    suspend fun getGeoCordsByCityName(@Query("q") cityName: String, @Query("appid") apiKey: String = Constants.API_KEY): List<City>

    @GET("/data/2.5/weather")
    suspend fun getWeatherInfoByCity(@Query("lat") lat: Double, @Query("lon") lon: Double, @Query("appid") apiKey: String = Constants.API_KEY): CityWeatherInfo

    companion object {
        val BASE_URL = "https://api.openweathermap.org"
        fun create(): WeatherService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(WeatherService::class.java)
        }
    }
}