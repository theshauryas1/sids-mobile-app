package com.nurthure.monitor.ui.screens.alerts

import com.nurthure.monitor.domain.model.Alert

data class AlertsUiState(
    val alerts: List<Alert> = emptyList()
)
