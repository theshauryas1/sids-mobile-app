package com.nurthure.monitor.domain.model

sealed class ConnectionState {
    object Disconnected : ConnectionState()
    object Connecting : ConnectionState()
    data class Connected(val address: String) : ConnectionState()
    data class Error(val message: String) : ConnectionState()
}
