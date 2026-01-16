package com.nurthure.monitor.data.remote

import android.util.Log
import com.nurthure.monitor.domain.model.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PiApiService @Inject constructor(
    private val client: HttpClient
) {
    private val json = Json { ignoreUnknownKeys = true }
    
    suspend fun fetchReading(address: String, port: String): SensorReading? {
        return try {
            val response = client.get("http://$address:$port/readings")
            val body = response.bodyAsText()
            parseReading(body)
        } catch (e: Exception) {
            Log.w("PiApiService", "Failed to fetch: ${e.message}")
            null
        }
    }
    
    private fun parseReading(jsonString: String): SensorReading {
        val jsonObj = json.parseToJsonElement(jsonString).jsonObject
        
        return SensorReading(
            timestamp = System.currentTimeMillis(),
            respiration = jsonObj["respiration"]?.jsonObject?.let { resp ->
                RespirationData(
                    value = resp["value"]?.jsonPrimitive?.floatOrNull,
                    unit = resp["unit"]?.jsonPrimitive?.contentOrNull ?: "rpm"
                )
            },
            audio = jsonObj["audio"]?.jsonObject?.let { audio ->
                AudioData(
                    state = audio["state"]?.jsonPrimitive?.contentOrNull ?: "unknown",
                    level = audio["level"]?.jsonPrimitive?.floatOrNull ?: 0f
                )
            },
            bodyTemp = jsonObj["body_temp"]?.jsonObject?.let { temp ->
                BodyTempData(
                    value = temp["value"]?.jsonPrimitive?.floatOrNull,
                    unit = temp["unit"]?.jsonPrimitive?.contentOrNull ?: "C"
                )
            },
            posture = jsonObj["posture"]?.jsonObject?.let { posture ->
                PostureData(
                    state = posture["state"]?.jsonPrimitive?.contentOrNull ?: "unknown"
                )
            },
            radar = jsonObj["radar"]?.jsonObject?.let { radar ->
                RadarData(
                    active = radar["active"]?.jsonPrimitive?.booleanOrNull ?: false
                )
            },
            environment = jsonObj["environment"]?.jsonObject?.let { env ->
                EnvironmentData(
                    temp = TempData(
                        value = env["temp"]?.let {
                            when {
                                it is JsonObject -> it.jsonObject["value"]?.jsonPrimitive?.floatOrNull
                                it is JsonPrimitive -> it.floatOrNull
                                else -> null
                            }
                        }
                    ),
                    co2 = CO2Data(
                        value = env["co2"]?.let {
                            when {
                                it is JsonObject -> it.jsonObject["value"]?.jsonPrimitive?.intOrNull
                                it is JsonPrimitive -> it.intOrNull
                                else -> null
                            }
                        }
                    ),
                    voc = VOCData(
                        value = env["voc"]?.let {
                            when {
                                it is JsonObject -> it.jsonObject["value"]?.jsonPrimitive?.floatOrNull
                                it is JsonPrimitive -> it.floatOrNull
                                else -> null
                            }
                        }
                    ),
                    gas = GasData(
                        safe = env["gas"]?.let {
                            when {
                                it is JsonObject -> it.jsonObject["safe"]?.jsonPrimitive?.booleanOrNull
                                it is JsonPrimitive -> it.booleanOrNull
                                else -> null
                            }
                        } ?: true
                    )
                )
            }
        )
    }
}
