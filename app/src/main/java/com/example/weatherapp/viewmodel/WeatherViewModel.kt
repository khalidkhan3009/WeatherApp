package com.example.weatherapp.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.City
import com.example.weatherapp.model.CityWeatherInfo
import com.example.weatherapp.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    //City List State
    private val _cityListState = MutableStateFlow<List<City>>(mutableListOf())
    val cityList = _cityListState.asStateFlow()

    //City Weather State
    private val _cityWeatherInfo = MutableStateFlow(CityWeatherInfo())
    val cityWeatherInfo = _cityWeatherInfo.asStateFlow()

    private val historyList by lazy { mutableStateListOf<City>() }

    fun updateListOfCities(inputVal: String) {
        viewModelScope.launch {
            if (inputVal.isNotEmpty()) {
                val listOfGeoCords = WeatherRepository().getListOfCities(inputVal)
                _cityListState.value = listOfGeoCords
            } else {
                _cityListState.value = historyList.reversed().take(5)
            }
        }
    }

    fun getWeatherInfoByCity(city: City, isCurrentLocation: Boolean) {
        if(!isCurrentLocation) historyList.add(city)
        viewModelScope.launch {
            _cityWeatherInfo.value = WeatherRepository().getWeatherInfoByCity(city)
            Log.d("WeatherApp", _cityWeatherInfo.value.toString())
        }
    }

    fun getWeatherIcon(cityWeatherInfo: CityWeatherInfo): String {
        return WeatherRepository().getWeatherIcon(cityWeatherInfo)
    }
}