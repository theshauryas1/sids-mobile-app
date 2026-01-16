package com.nurthure.monitor.domain.model

data class Alert(
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val severity: AlertSeverity,
    val title: String,
    val description: String,
    val key: String,
    val acknowledged: Boolean = false
)

enum class AlertSeverity {
    CRITICAL,
    WARNING
}
