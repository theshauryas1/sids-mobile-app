package com.nurthure.monitor.ui.screens.monitor

import com.nurthure.monitor.domain.model.ConnectionState

data class MonitorUiState(
    val connectionState: ConnectionState = ConnectionState.Disconnected,
    val respiration: Int? = null,
    val audioState: String? = null,
    val bodyTemp: Float? = null,
    val posture: String? = null,
    val radarActive: Boolean? = null,
    val envTemp: Float? = null,
    val co2: Int? = null,
    val voc: Float? = null,
    val gasIsSafe: Boolean? = null,
    val smartAnalysis: String? = null
)
