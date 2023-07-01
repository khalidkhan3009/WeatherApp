@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.weatherapp.constant.Constants
import com.example.weatherapp.extensions.getStateOrEmpty
import com.example.weatherapp.extensions.toCelsius
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.viewmodel.WeatherViewModel

class SearchActivity : ComponentActivity() {
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
    var inputVal by rememberSaveable { mutableStateOf("London") }
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
                    .clickable { viewModel.getWeatherInfoByCity(city) }
            )
        }
    }
    Divider(color = Color.Black, thickness = 2.dp, modifier = Modifier.padding(start = 16.dp, end = 16.dp))
}


@Composable
fun WeatherInformation(viewModel: WeatherViewModel, modifier: Modifier) {
    val cityWeatherInfo by viewModel.cityWeatherInfo.collectAsState()
    Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, ) {
            Text(
                text = cityWeatherInfo.name,
                fontSize = 24.sp
            )

            AsyncImage(
                model = viewModel.getWeatherIcon(cityWeatherInfo),
                contentDescription = "Cloudy",
                modifier = Modifier.width(100.dp).height(100.dp)
            )
        }
        Spacer(modifier.height(16.dp))
        Text(text = "Temperature: ${cityWeatherInfo.main[Constants.TEMP]?.toCelsius()}")
        Spacer(modifier.height(16.dp))
        Text(text = "Feels Like: ${cityWeatherInfo.main[Constants.FEELS_LIKE]?.toCelsius()}")
        //Text(text = "Description: ${cityWeatherInfo.weather[0].description}")
        Spacer(modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            Text(text = "Min: ${cityWeatherInfo.main[Constants.MIN_TEMP]?.toCelsius()}")
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "Max: ${cityWeatherInfo.main[Constants.MAX_TEMP]?.toCelsius()}")
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
