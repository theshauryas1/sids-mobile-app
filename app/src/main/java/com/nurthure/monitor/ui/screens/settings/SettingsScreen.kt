package com.nurthure.monitor.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nurthure.monitor.ui.theme.*

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    var showWifiDialog by remember { mutableStateOf(false) }
    var showGeminiDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(scrollState)
    ) {
        // Header
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineLarge,
            color = TextPrimary,
            modifier = Modifier.padding(24.dp)
        )

        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Connection Section
            SectionTitle("CONNECTION")
            
            Card(
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                shape = RoundedCornerShape(16.dp)
            ) {
                SettingItem(
                    title = "Raspberry Pi WiFi",
                    subtitle = "${uiState.piAddress}:${uiState.piPort}",
                    onClick = { showWifiDialog = true }
                )
            }

            // Sensor Thresholds Section
            SectionTitle("SENSOR THRESHOLDS")
            
            Card(
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    ThresholdSlider(
                        label = "Max CO₂ (MH-Z19C)",
                        value = uiState.co2Threshold.toFloat(),
                        valueLabel = "${uiState.co2Threshold} ppm",
                        range = 400f..2000f,
                        onValueChange = { viewModel.setCO2Threshold(it.toInt()) }
                    )
                    
                    Divider(color = Background, thickness = 1.dp)
                    
                    ThresholdSlider(
                        label = "Body Temp (MLX90614)",
                        value = uiState.tempThreshold,
                        valueLabel = "36°C - 38°C",
                        range = 35f..40f,
                        onValueChange = { }
                    )
                }
            }

            // Hardware Modules Section
            SectionTitle("HARDWARE MODULES")
            
            Card(
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    HardwareItem(
                        name = "mmWave Radar",
                        status = if (uiState.isConnected) "Active" else "Offline",
                        isActive = uiState.isConnected
                    )
                    Divider(color = Background, thickness = 1.dp)
                    HardwareItem(
                        name = "BME688 + MH-Z19C",
                        status = if (uiState.isConnected) "Active" else "Offline",
                        isActive = uiState.isConnected
                    )
                    Divider(color = Background, thickness = 1.dp)
                    HardwareItem(
                        name = "MQ-135 Gas Sensor",
                        status = if (uiState.isConnected) "Calibrated" else "Offline",
                        isActive = uiState.isConnected
                    )
                }
            }

            // Gemini API Section
            SectionTitle("SMART ANALYSIS (GEMINI)")
            
            Card(
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                shape = RoundedCornerShape(16.dp)
            ) {
                SettingItem(
                    title = "Gemini API Key",
                    subtitle = if (uiState.hasGeminiKey) "••••••••" else "Not configured",
                    onClick = { showGeminiDialog = true }
                )
            }

            // Export Section
            SectionTitle("DATA EXPORT")
            
            Card(
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.exportCSV() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Export CSV")
                    }
                    
                    OutlinedButton(
                        onClick = { viewModel.exportJSON() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Export JSON")
                    }
                }
            }

            // Safety Notice
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = AccentIndigo.copy(alpha = 0.08f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        tint = AccentIndigo
                    )
                    Text(
                        text = "Safety Notice: This device uses non-contact sensors (Radar, IR, Gas). Always ensure clear line-of-sight for the camera and radar. Not a medical device.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // WiFi Config Dialog
    if (showWifiDialog) {
        WifiConfigDialog(
            currentAddress = uiState.piAddress,
            currentPort = uiState.piPort,
            onDismiss = { showWifiDialog = false },
            onSave = { address, port ->
                viewModel.savePiConnection(address, port)
                showWifiDialog = false
            }
        )
    }

    // Gemini API Dialog
    if (showGeminiDialog) {
        GeminiKeyDialog(
            onDismiss = { showGeminiDialog = false },
            onSave = { key ->
                viewModel.saveGeminiKey(key)
                showGeminiDialog = false
            }
        )
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelSmall,
        color = TextMuted,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(start = 4.dp)
    )
}

@Composable
fun SettingItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted
            )
        }
        
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = AccentTeal),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text("Configure", style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun ThresholdSlider(
    label: String,
    value: Float,
    valueLabel: String,
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )
            Text(
                text = valueLabel,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = AccentTeal
            )
        }
        
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            colors = SliderDefaults.colors(
                thumbColor = AlertCritical,
                activeTrackColor = AlertCritical.copy(alpha = 0.5f),
                inactiveTrackColor = Background
            ),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun HardwareItem(
    name: String,
    status: String,
    isActive: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = TextPrimary
        )
        
        Surface(
            color = if (isActive) AccentGreen.copy(alpha = 0.1f) else TextMuted.copy(alpha = 0.1f),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = status,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = if (isActive) AccentGreen else TextMuted,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun WifiConfigDialog(
    currentAddress: String,
    currentPort: String,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var address by remember { mutableStateOf(currentAddress) }
    var port by remember { mutableStateOf(currentPort) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("WiFi Configuration") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Raspberry Pi IP Address") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = port,
                    onValueChange = { port = it },
                    label = { Text("Port") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSave(address, port) }) {
                Text("Save & Connect")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun GeminiKeyDialog(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var apiKey by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Gemini API Key") },
        text = {
            OutlinedTextField(
                value = apiKey,
                onValueChange = { apiKey = it },
                label = { Text("Enter your API key") },
                singleLine = true
            )
        },
        confirmButton = {
            Button(onClick = { onSave(apiKey) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
