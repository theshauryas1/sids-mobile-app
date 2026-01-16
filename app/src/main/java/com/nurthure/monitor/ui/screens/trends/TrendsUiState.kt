package com.nurthure.monitor.ui.screens.trends

data class TrendsUiState(
    val selectedTimeRange: String = "1h",
    val respirationAvg: Int? = null,
    val co2Avg: Int? = null,
    val hasRespirationData: Boolean = false,
    val hasCO2Data: Boolean = false
)
