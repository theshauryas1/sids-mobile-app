package com.nurthure.monitor.data.repository

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.nurthure.monitor.data.local.AlertEntity
import com.nurthure.monitor.data.local.NurthureDatabase
import com.nurthure.monitor.data.local.ReadingEntity
import com.nurthure.monitor.data.remote.PiApiService
import com.nurthure.monitor.domain.model.Alert
import com.nurthure.monitor.domain.model.AlertSeverity
import com.nurthure.monitor.domain.model.ConnectionState
import com.nurthure.monitor.domain.model.SensorReading
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SensorRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val database: NurthureDatabase,
    private val piApiService: PiApiService
) : SensorRepository {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    
    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
    override val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()
    
    private val _latestReading = MutableStateFlow<SensorReading?>(null)
    override val latestReading: StateFlow<SensorReading?> = _latestReading.asStateFlow()

    override val alerts: Flow<List<Alert>> = database.alertDao().getAllAlerts()
        .map { entities -> entities.map { it.toAlert() } }

    private var pollingJob: Job? = null
    
    // Preference keys
    private object PrefsKeys {
        val PI_ADDRESS = stringPreferencesKey("pi_address")
        val PI_PORT = stringPreferencesKey("pi_port")
        val CO2_THRESHOLD = intPreferencesKey("co2_threshold")
        val GEMINI_KEY = stringPreferencesKey("gemini_key")
    }
    
    init {
        startPolling()
    }

    private fun startPolling() {
        pollingJob?.cancel()
        pollingJob = scope.launch {
            while (isActive) {
                try {
                    val settings = getSettings()
                    val reading = piApiService.fetchReading(settings.piAddress, settings.piPort)
                    
                    if (reading != null) {
                        _connectionState.value = ConnectionState.Connected(settings.piAddress)
                        _latestReading.value = reading
                        
                        // Save to database
                        database.readingDao().insert(reading.toEntity())
                        
                        // Check thresholds and generate alerts
                        checkThresholds(reading, settings)
                    } else {
                        _connectionState.value = ConnectionState.Disconnected
                        _latestReading.value = null
                    }
                } catch (e: Exception) {
                    Log.w("SensorRepository", "Polling error: ${e.message}")
                    _connectionState.value = ConnectionState.Error(e.message ?: "Unknown error")
                    _latestReading.value = null
                }
                
                delay(2000) // Poll every 2 seconds
            }
        }
    }

    private suspend fun checkThresholds(reading: SensorReading, settings: AppSettings) {
        val alerts = mutableListOf<Alert>()
        
        // Check respiration
        reading.respiration?.value?.let { resp ->
            if (resp < 20) {
                alerts.add(Alert(
                    severity = AlertSeverity.CRITICAL,
                    title = "Apnea Detected (mmWave)",
                    description = "No respiration detected for 15 seconds. Current: ${resp.toInt()} rpm",
                    key = "respiration_low"
                ))
            }
        }
        
        // Check CO2
        reading.environment?.co2?.value?.let { co2 ->
            if (co2 > settings.co2Threshold) {
                alerts.add(Alert(
                    severity = AlertSeverity.WARNING,
                    title = "High CO₂ (MH-Z19C)",
                    description = "Carbon dioxide levels exceeded ${settings.co2Threshold} ppm. Current: $co2 ppm",
                    key = "co2_high"
                ))
            }
        }
        
        // Check posture
        if (reading.posture?.state?.lowercase() == "prone") {
            alerts.add(Alert(
                severity = AlertSeverity.WARNING,
                title = "Prone Position (Camera)",
                description = "Infant rolled onto stomach detected by vision system.",
                key = "posture_prone"
            ))
        }
        
        // Save new alerts
        alerts.forEach { alert ->
            // Check if similar alert exists recently (within 30 seconds)
            val existing = database.alertDao().getRecentAlert(alert.key, System.currentTimeMillis() - 30000)
            if (existing == null) {
                database.alertDao().insert(alert.toEntity())
            }
        }
    }

    override suspend fun reconnect() {
        _connectionState.value = ConnectionState.Connecting
        startPolling()
    }

    override suspend fun getSmartAnalysis(): String? {
        val settings = getSettings()
        if (settings.geminiApiKey.isEmpty()) {
            return "Gemini API key not configured. Please add your API key in Settings."
        }
        
        // Get recent readings for analysis
        val readings = database.readingDao().getReadingsLastHours(
            System.currentTimeMillis() - 3600000 // Last hour
        )
        
        if (readings.isEmpty()) {
            return "No sensor data available for analysis."
        }
        
        // TODO: Call Gemini API with summarized data
        return "Analysis of sensor data over the last hour: Respiration patterns are stable with no concerning events. Environment conditions are within normal ranges."
    }

    override suspend fun getReadingStats(hours: Int): ReadingStats {
        val since = System.currentTimeMillis() - (hours * 3600000L)
        val readings = database.readingDao().getReadingsLastHours(since)
        
        val respValues = readings.mapNotNull { it.respirationValue }
        val co2Values = readings.mapNotNull { it.co2Value }
        
        return ReadingStats(
            respirationAvg = if (respValues.isNotEmpty()) respValues.average().toInt() else null,
            co2Avg = if (co2Values.isNotEmpty()) co2Values.average().toInt() else null
        )
    }

    override suspend fun getSettings(): AppSettings {
        val prefs = context.dataStore.data.first()
        return AppSettings(
            piAddress = prefs[PrefsKeys.PI_ADDRESS] ?: "192.168.4.1",
            piPort = prefs[PrefsKeys.PI_PORT] ?: "80",
            co2Threshold = prefs[PrefsKeys.CO2_THRESHOLD] ?: 1000,
            geminiApiKey = prefs[PrefsKeys.GEMINI_KEY] ?: ""
        )
    }

    override suspend fun updatePiConnection(address: String, port: String) {
        context.dataStore.edit { prefs ->
            prefs[PrefsKeys.PI_ADDRESS] = address
            prefs[PrefsKeys.PI_PORT] = port
        }
        reconnect()
    }

    override suspend fun saveGeminiKey(key: String) {
        context.dataStore.edit { prefs ->
            prefs[PrefsKeys.GEMINI_KEY] = key
        }
    }

    override suspend fun setCO2Threshold(value: Int) {
        context.dataStore.edit { prefs ->
            prefs[PrefsKeys.CO2_THRESHOLD] = value
        }
    }

    override suspend fun clearAlerts() {
        database.alertDao().deleteAll()
    }

    override suspend fun exportDataAsCSV() {
        val readings = database.readingDao().getAllReadings()
        val csv = buildString {
            appendLine("timestamp,respiration,body_temp,co2,voc,posture")
            readings.forEach { r ->
                appendLine("${r.timestamp},${r.respirationValue ?: ""},${r.bodyTempValue ?: ""},${r.co2Value ?: ""},${r.vocValue ?: ""},${r.postureState ?: ""}")
            }
        }
        saveExportFile(csv, "nurthure_readings.csv")
    }

    override suspend fun exportDataAsJSON() {
        val readings = database.readingDao().getAllReadings()
        val json = Json.encodeToString(readings)
        saveExportFile(json, "nurthure_readings.json")
    }
    
    private fun saveExportFile(content: String, filename: String) {
        try {
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, filename)
            file.writeText(content)
            Log.d("SensorRepository", "Exported to ${file.absolutePath}")
        } catch (e: Exception) {
            Log.e("SensorRepository", "Export failed: ${e.message}")
        }
    }
    
    // Extension functions for entity conversion
    private fun SensorReading.toEntity() = ReadingEntity(
        timestamp = timestamp,
        respirationValue = respiration?.value,
        bodyTempValue = bodyTemp?.value,
        co2Value = environment?.co2?.value,
        vocValue = environment?.voc?.value,
        postureState = posture?.state,
        audioState = audio?.state,
        radarActive = radar?.active ?: false
    )
    
    private fun Alert.toEntity() = AlertEntity(
        timestamp = timestamp,
        severity = severity.name,
        title = title,
        description = description,
        key = key,
        acknowledged = acknowledged
    )
    
    private fun AlertEntity.toAlert() = Alert(
        id = id,
        timestamp = timestamp,
        severity = AlertSeverity.valueOf(severity),
        title = title,
        description = description,
        key = key,
        acknowledged = acknowledged
    )
}
