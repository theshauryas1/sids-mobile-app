package com.nurthure.monitor.data.repository

import com.nurthure.monitor.domain.model.Alert
import com.nurthure.monitor.domain.model.ConnectionState
import com.nurthure.monitor.domain.model.SensorReading
import kotlinx.coroutines.flow.Flow

interface SensorRepository {
    val connectionState: Flow<ConnectionState>
    val latestReading: Flow<SensorReading?>
    val alerts: Flow<List<Alert>>
    
    suspend fun reconnect()
    suspend fun getSmartAnalysis(): String?
    suspend fun getReadingStats(hours: Int): ReadingStats
    suspend fun getSettings(): AppSettings
    suspend fun updatePiConnection(address: String, port: String)
    suspend fun saveGeminiKey(key: String)
    suspend fun setCO2Threshold(value: Int)
    suspend fun clearAlerts()
    suspend fun exportDataAsCSV()
    suspend fun exportDataAsJSON()
}

data class ReadingStats(
    val respirationAvg: Int? = null,
    val co2Avg: Int? = null
)

data class AppSettings(
    val piAddress: String = "192.168.4.1",
    val piPort: String = "80",
    val co2Threshold: Int = 1000,
    val geminiApiKey: String = ""
)
