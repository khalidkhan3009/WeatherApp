@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.example.weatherapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.weatherapp.constant.Constants
import com.example.weatherapp.extensions.getDescription
import com.example.weatherapp.extensions.getFeelsLike
import com.example.weatherapp.extensions.getMaxTemp
import com.example.weatherapp.extensions.getMinTemp
import com.example.weatherapp.extensions.getStateOrEmpty
import com.example.weatherapp.extensions.getTemperature
import com.example.weatherapp.extensions.toCelsius
import com.example.weatherapp.model.City
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class SearchActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    val viewModel: WeatherViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    CityWeather()
                }
            }
        }
        requestLocationPermission()
        requestLastKnownLocation()
    }

    private fun requestLocationPermission() {
        val locationPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            when {
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                    Log.d("WeatherApp", "${Manifest.permission.ACCESS_COARSE_LOCATION} Permission Granted")
                    requestLastKnownLocation()
                }
            }
        }
        locationPermissionRequest.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    private fun requestLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) { return }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnSuccessListener {
            viewModel.getWeatherInfoByCity(City("", it.latitude, it.longitude, "", ""), true)
            Log.d("WeatherApp", "Latitude: ${it.latitude} Longitude: ${it.longitude}")
        }
    }
}

@Composable
fun CityWeather(modifier: Modifier = Modifier) {
    val viewModel = viewModel { WeatherViewModel() }
    Column {
        Spacer(modifier.height(16.dp))
        SearchBar(viewModel, modifier = Modifier.padding(horizontal = 16.dp))
        SearchSection(viewModel)
        Spacer(modifier.height(30.dp))
        WeatherInformation(viewModel, modifier)
    }
}

@Composable
fun SearchBar(viewModel: WeatherViewModel, modifier: Modifier = Modifier) {
    var inputVal by rememberSaveable { mutableStateOf("") }
    TextField(
        value = inputVal,
        placeholder = { Text(text = "Search City, Zipcode.....") },
        onValueChange = { inputVal = it },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = null
            )
        },
        colors = TextFieldDefaults.textFieldColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier
            .heightIn(min = 56.dp)
            .fillMaxWidth()
    )
    viewModel.updateListOfCities(inputVal)
}

@Composable
fun SearchSection(viewModel: WeatherViewModel) {
    val cityList by viewModel.cityList.collectAsState()
    LazyColumn(modifier = Modifier.padding(all = 16.dp)) {
        items(items = cityList) { city ->
            Text(
                text = "${city.name}, ${city.state.getStateOrEmpty()}${city.country}",
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(all = 10.dp)
                    .clickable { viewModel.getWeatherInfoByCity(city, false) }
            )
        }
    }
    Divider(color = Color.Black, thickness = 1.dp, modifier = Modifier.padding(start = 16.dp, end = 16.dp))
}


@Composable
fun WeatherInformation(viewModel: WeatherViewModel, modifier: Modifier) {
    val cityWeatherInfo by viewModel.cityWeatherInfo.collectAsState()
    Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = cityWeatherInfo.name,
                    fontSize = 32.sp
                )
                Text(text = "Description: ${cityWeatherInfo.getDescription()}")
            }

            AsyncImage(
                model = viewModel.getWeatherIcon(cityWeatherInfo),
                contentDescription = "Cloudy",
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
            )
        }
        Spacer(modifier.height(16.dp))
        Text(text = "Temperature: ${cityWeatherInfo.getTemperature()}")
        Spacer(modifier.height(16.dp))
        Text(text = "Feels Like: ${cityWeatherInfo.getFeelsLike()}")

        Spacer(modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            Text(text = "Min: ${cityWeatherInfo.getMinTemp()}")
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "Max: ${cityWeatherInfo.getMaxTemp()}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherAppTheme {
        CityWeather()
    }
}
