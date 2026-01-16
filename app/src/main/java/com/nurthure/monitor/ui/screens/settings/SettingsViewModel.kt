package com.nurthure.monitor.ui.screens.settings

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
class SettingsViewModel @Inject constructor(
    private val repository: SensorRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
        observeConnection()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            val settings = repository.getSettings()
            _uiState.update { state ->
                state.copy(
                    piAddress = settings.piAddress,
                    piPort = settings.piPort,
                    co2Threshold = settings.co2Threshold,
                    hasGeminiKey = settings.geminiApiKey.isNotEmpty()
                )
            }
        }
    }

    private fun observeConnection() {
        viewModelScope.launch {
            repository.connectionState.collect { state ->
                _uiState.update { 
                    it.copy(isConnected = state is ConnectionState.Connected) 
                }
            }
        }
    }

    fun savePiConnection(address: String, port: String) {
        viewModelScope.launch {
            repository.updatePiConnection(address, port)
            _uiState.update { it.copy(piAddress = address, piPort = port) }
        }
    }

    fun saveGeminiKey(key: String) {
        viewModelScope.launch {
            repository.saveGeminiKey(key)
            _uiState.update { it.copy(hasGeminiKey = key.isNotEmpty()) }
        }
    }

    fun setCO2Threshold(value: Int) {
        viewModelScope.launch {
            repository.setCO2Threshold(value)
            _uiState.update { it.copy(co2Threshold = value) }
        }
    }

    fun exportCSV() {
        viewModelScope.launch {
            repository.exportDataAsCSV()
        }
    }

    fun exportJSON() {
        viewModelScope.launch {
            repository.exportDataAsJSON()
        }
    }
}
