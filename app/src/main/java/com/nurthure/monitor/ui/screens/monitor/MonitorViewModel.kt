package com.nurthure.monitor.ui.screens.monitor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nurthure.monitor.data.repository.SensorRepository
import com.nurthure.monitor.domain.model.ConnectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MonitorViewModel @Inject constructor(
    private val repository: SensorRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MonitorUiState())
    val uiState: StateFlow<MonitorUiState> = _uiState.asStateFlow()

    init {
        observeConnection()
        observeReadings()
    }

    private fun observeConnection() {
        viewModelScope.launch {
            repository.connectionState.collect { state ->
                _uiState.update { it.copy(connectionState = state) }
                
                // Clear values when disconnected
                if (state !is ConnectionState.Connected) {
                    _uiState.update { it.copy(
                        respiration = null,
                        audioState = null,
                        bodyTemp = null,
                        posture = null,
                        radarActive = null,
                        envTemp = null,
                        co2 = null,
                        voc = null,
                        gasIsSafe = null
                    )}
                }
            }
        }
    }

    private fun observeReadings() {
        viewModelScope.launch {
            repository.latestReading.collect { reading ->
                reading?.let { r ->
                    _uiState.update { state ->
                        state.copy(
                            respiration = r.respiration?.value?.toInt(),
                            audioState = r.audio?.state?.replaceFirstChar { it.uppercase() },
                            bodyTemp = r.bodyTemp?.value,
                            posture = r.posture?.state?.replaceFirstChar { it.uppercase() },
                            radarActive = r.radar?.active,
                            envTemp = r.environment?.temp?.value,
                            co2 = r.environment?.co2?.value,
                            voc = r.environment?.voc?.value,
                            gasIsSafe = r.environment?.gas?.safe
                        )
                    }
                }
            }
        }
    }

    fun retryConnection() {
        viewModelScope.launch {
            repository.reconnect()
        }
    }

    fun refreshAnalysis() {
        viewModelScope.launch {
            val analysis = repository.getSmartAnalysis()
            _uiState.update { it.copy(smartAnalysis = analysis) }
        }
    }
}
