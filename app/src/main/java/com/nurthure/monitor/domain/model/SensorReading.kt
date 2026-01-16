package com.nurthure.monitor.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SensorReading(
    val timestamp: Long = System.currentTimeMillis(),
    val respiration: RespirationData? = null,
    val audio: AudioData? = null,
    val bodyTemp: BodyTempData? = null,
    val posture: PostureData? = null,
    val radar: RadarData? = null,
    val environment: EnvironmentData? = null
)

@Serializable
data class RespirationData(
    val value: Float?,
    val unit: String = "rpm",
    val confidence: Float = 1f
)

@Serializable
data class AudioData(
    val state: String = "unknown",
    val level: Float = 0f
)

@Serializable
data class BodyTempData(
    val value: Float?,
    val unit: String = "C"
)

@Serializable
data class PostureData(
    val state: String = "unknown",
    val confidence: Float = 1f
)

@Serializable
data class RadarData(
    val active: Boolean = false,
    val movement: Float = 0f
)

@Serializable
data class EnvironmentData(
    val temp: TempData? = null,
    val co2: CO2Data? = null,
    val voc: VOCData? = null,
    val gas: GasData? = null
)

@Serializable
data class TempData(
    val value: Float?,
    val unit: String = "C"
)

@Serializable
data class CO2Data(
    val value: Int?,
    val unit: String = "ppm"
)

@Serializable
data class VOCData(
    val value: Float?
)

@Serializable
data class GasData(
    val safe: Boolean = true
)
