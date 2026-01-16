package com.nurthure.monitor.ui.screens.settings

data class SettingsUiState(
    val piAddress: String = "192.168.4.1",
    val piPort: String = "80",
    val co2Threshold: Int = 1000,
    val tempThreshold: Float = 37f,
    val isConnected: Boolean = false,
    val hasGeminiKey: Boolean = false
)
