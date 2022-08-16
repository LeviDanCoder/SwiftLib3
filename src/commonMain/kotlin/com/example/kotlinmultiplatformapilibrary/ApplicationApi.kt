package com.example.kotlinmultiplatformapilibrary

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

internal expect val ApplicationDispatcher: CoroutineDispatcher

class ApplicationApi {
    private val address = Url("https://cors-test.appspot.com/test")

    private val client = HttpClient() {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }

    fun about(callback: (String) -> Unit) {
        GlobalScope.launch(ApplicationDispatcher) {
            val status: Response = client.get {
                url(address.toString())
            }.body()

            withContext(Dispatchers.Main) {
                callback(status.status)
            }
        }
    }
}

@Serializable
data class Response(val status: String)