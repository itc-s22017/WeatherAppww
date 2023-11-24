package com.example.myapplication.api

import com.example.myapplication.BuildConfig
import com.example.myapplication.model.WeatherResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


object Client {

    private val ktor = HttpClient(CIO) {
        engine {
            endpoint {
                connectTimeout = 5000
                requestTimeout = 5000
                socketTimeout = 5000
            }
        }
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }
            )
        }
    }

    suspend fun get(param: Int): WeatherResponse {
        return ktor.get { url("http://api.openweathermap.org/data/2.5/forecast?id=${param}&appid=${BuildConfig.API_KEY}&lang=ja&units=metric") }
            .body()
    }

}