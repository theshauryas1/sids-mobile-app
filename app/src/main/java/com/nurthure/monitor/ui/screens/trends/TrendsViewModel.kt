package com.nurthure.monitor.ui.screens.trends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nurthure.monitor.data.repository.SensorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrendsViewModel @Inject constructor(
    private val repository: SensorRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TrendsUiState())
    val uiState: StateFlow<TrendsUiState> = _uiState.asStateFlow()

    init {
        loadTrendsData()
    }

    fun selectTimeRange(range: String) {
        _uiState.update { it.copy(selectedTimeRange = range) }
        loadTrendsData()
    }

    private fun loadTrendsData() {
        viewModelScope.launch {
            val hours = when (_uiState.value.selectedTimeRange) {
                "1h" -> 1
                "24h" -> 24
                "7d" -> 24 * 7
                "1m" -> 24 * 30
                else -> 1
            }
            
            val stats = repository.getReadingStats(hours)
            
            _uiState.update { state ->
                state.copy(
                    respirationAvg = stats.respirationAvg,
                    co2Avg = stats.co2Avg,
                    hasRespirationData = stats.respirationAvg != null,
                    hasCO2Data = stats.co2Avg != null
                )
            }
        }
    }
}
