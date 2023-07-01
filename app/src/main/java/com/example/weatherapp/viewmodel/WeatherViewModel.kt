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
    //State to handle the list of cities
    private val _cityListState = MutableStateFlow<List<City>>(mutableListOf())
    val cityList = _cityListState.asStateFlow()

    //State to handle City Weather Info
    private val _cityWeatherInfo = MutableStateFlow(CityWeatherInfo())
    val cityWeatherInfo = _cityWeatherInfo.asStateFlow()

    //History List to store the cities which are already searched
    private val historyList by lazy { mutableStateListOf<City>() }

    /*
    * Take the string input and update the list of cities or recently searched city list
    * */
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

    /*
    * Take the selected City info and fetch the Weather Information for that City
    * */
    fun getWeatherInfoByCity(city: City, isCurrentLocation: Boolean) {
        if(!isCurrentLocation) historyList.add(city)
        viewModelScope.launch {
            _cityWeatherInfo.value = WeatherRepository().getWeatherInfoByCity(city)
            Log.d("WeatherApp", _cityWeatherInfo.value.toString())
        }
    }

    /*
    * Get the Weather icon for the selected City*/
    fun getWeatherIcon(cityWeatherInfo: CityWeatherInfo): String {
        return WeatherRepository().getWeatherIcon(cityWeatherInfo)
    }
}