package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import coil.compose.AsyncImage
import com.example.myapplication.api.Client.get
import com.example.myapplication.model.WeatherResponse
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val _loc = MutableStateFlow<Location?>(null)
//    private val loc = _loc


    @SuppressLint("StateFlowValueCalledInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
        } else {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    // Got last known location. In some rare situations this can be null.
                    _loc.value = location
                    Log.d("Location", "${location?.longitude}")
                    Log.d("Location", "${location?.latitude}")

                }
        }
        setContent {
            MyApplicationTheme {
                Main(location = _loc.value)
            }
        }


    }


}


@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main(modifier: Modifier = Modifier, location: Location? = null) {
    var expanded by remember { mutableStateOf(false) }
    val items = mapOf(2130037 to "北海道", 2130658 to "青森", 2111834 to "岩手", 1856035 to "沖縄")
    var selectedItem by remember { mutableStateOf(items[2130037]) }
    var cityId by remember {
        mutableIntStateOf(2130037)
    }
    var weatherResponse by remember { mutableStateOf<WeatherResponse?>(null) }

    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                TextField(
                    value = selectedItem.toString(),
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    items.forEach { (key, value) ->
                        DropdownMenuItem(
                            text = { Text(text = items[key].toString()) },
                            onClick = {
                                selectedItem = items[key]
                                expanded = false
                                cityId = key
                            }
                        )
                    }
                }
            }
            Button(
                onClick = {
                    scope.launch {
                        weatherResponse = get(cityId)
                    }
                },
                modifier = Modifier
                    .padding(top = 16.dp)
            ) {
                Text("Click me")
            }
            weatherResponse?.let {
                Text(text = "都市名: ${selectedItem}")
                Spacer(modifier = Modifier.padding(bottom = 20.dp))
                it.list.forEach { weatherData ->
                    val sdf = java.text.SimpleDateFormat("yyyy/MM/dd HH:mm")
                    val date = java.util.Date(weatherData.dt * 1000)
                    val f = sdf.format(date)
                    location?.toString()?.let { it1 -> Text(text = it1) }
                    Text(text = "時間: $f")
                    Text(text = "温度: ${weatherData.main.temp}")
                    Text(text = "体感気温: ${weatherData.main.feelsLike}")
                    Text(text = "陸上気圧: ${weatherData.main.pressure}")
                    Text(text = "湿度: ${weatherData.main.humidity}")
                    Text(text = "天気: ${weatherData.weather.firstOrNull()?.description}")
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Icon")
                        AsyncImage(
                            model = "https://openweathermap.org/img/wn/${weatherData.weather.firstOrNull()?.icon}@2x.png",
                            contentDescription = null
                        )
                    }

                    Text(text = "風速: ${weatherData.wind.speed}")
                    Text(text = "風向: ${weatherData.wind.deg}")
                    Text(text = "瞬間風速: ${weatherData.wind.gust}")
                    Text(text = "降水確率: ${weatherData.pop * 100}%")
                    if (weatherData.snow?.snowVolume != null) {
                        Text(text = "積雪量: ${weatherData.snow.snowVolume}mm")

                    }
                    Text(text = "予測時刻: ${weatherData.dateTime}")
                    Text(text = "-------------------------------------")
                    Spacer(modifier = Modifier.padding(4.dp))
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_3A)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Main()
    }
}