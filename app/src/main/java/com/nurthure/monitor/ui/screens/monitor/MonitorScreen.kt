package com.nurthure.monitor.ui.screens.monitor

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nurthure.monitor.domain.model.ConnectionState
import com.nurthure.monitor.ui.theme.*

@Composable
fun MonitorScreen(
    viewModel: MonitorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .verticalScroll(scrollState)
        ) {
            // Header
            MonitorHeader(
                isConnected = uiState.connectionState is ConnectionState.Connected
            )

            // Content
            Column(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Main sensor cards row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    RespirationCard(
                        value = uiState.respiration,
                        modifier = Modifier.weight(1.2f)
                    )
                    AudioCard(
                        status = uiState.audioState,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Secondary sensors row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SmallSensorCard(
                        label = "BODY TEMP",
                        value = uiState.bodyTemp?.let { "${it}°" } ?: "--",
                        subLabel = "MLX90614",
                        modifier = Modifier.weight(1f)
                    )
                    PostureCard(
                        posture = uiState.posture,
                        modifier = Modifier.weight(1f)
                    )
                    RadarCard(
                        isActive = uiState.radarActive,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Environment card
                EnvironmentCard(
                    temp = uiState.envTemp,
                    co2 = uiState.co2,
                    voc = uiState.voc,
                    gasIsSafe = uiState.gasIsSafe
                )

                // Smart Analysis card
                SmartAnalysisCard(
                    analysis = uiState.smartAnalysis,
                    onRefresh = { viewModel.refreshAnalysis() }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Disconnected overlay
        if (uiState.connectionState !is ConnectionState.Connected) {
            DisconnectedOverlay(
                onRetry = { viewModel.retryConnection() }
            )
        }
    }
}

@Composable
fun MonitorHeader(isConnected: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column {
            Text(
                text = "Live Monitor",
                style = MaterialTheme.typography.headlineLarge,
                color = TextPrimary
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (isConnected) AccentGreen else TextMuted)
                )
                Text(
                    text = if (isConnected) "System Nominal" else "Device Not Connected",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isConnected) AccentGreen else AlertCritical
                )
            }
        }
        
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton(
                onClick = { },
                modifier = Modifier
                    .size(40.dp)
                    .background(CardBackground, RoundedCornerShape(12.dp))
            ) {
                Icon(Icons.Default.Videocam, contentDescription = "Camera", tint = TextSecondary)
            }
            
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(AccentTeal.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = "Profile", tint = AccentTeal)
            }
        }
    }
}

@Composable
fun RespirationCard(value: Int?, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Respiration",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary
                )
                Surface(
                    color = AccentTeal.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "_WAVE",
                        style = MaterialTheme.typography.labelSmall,
                        color = AccentTeal,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
            
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    text = value?.toString() ?: "--",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentTeal
                )
                Text(
                    text = "rpm",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
                )
            }
            
            // Waveform
            WaveformCanvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(top = 8.dp),
                hasSignal = value != null
            )
        }
    }
}

@Composable
fun WaveformCanvas(modifier: Modifier = Modifier, hasSignal: Boolean) {
    var phase by remember { mutableFloatStateOf(0f) }
    
    LaunchedEffect(hasSignal) {
        if (hasSignal) {
            while (true) {
                phase += 0.1f
                kotlinx.coroutines.delay(50)
            }
        }
    }
    
    Canvas(modifier = modifier) {
        if (!hasSignal) {
            drawLine(
                color = TextMuted,
                start = Offset(0f, size.height / 2),
                end = Offset(size.width, size.height / 2),
                strokeWidth = 2f
            )
            return@Canvas
        }
        
        val path = Path()
        val amplitude = size.height * 0.35f
        val centerY = size.height / 2
        
        for (x in 0..size.width.toInt() step 2) {
            val y = centerY + amplitude * kotlin.math.sin((x / 30f) + phase)
            if (x == 0) {
                path.moveTo(x.toFloat(), y)
            } else {
                path.lineTo(x.toFloat(), y)
            }
        }
        
        drawPath(
            path = path,
            color = AccentTeal,
            style = Stroke(width = 3f)
        )
    }
}

@Composable
fun AudioCard(status: String?, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Audio (MEMS)",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary
                )
                Icon(Icons.Default.Mic, contentDescription = null, tint = AccentGreen)
            }
            
            Text(
                text = status ?: "--",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(top = 24.dp)
            )
        }
    }
}

@Composable
fun SmallSensorCard(
    label: String,
    value: String,
    subLabel: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
            Text(
                text = value,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = AccentTeal,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = subLabel,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                color = TextMuted
            )
        }
    }
}

@Composable
fun PostureCard(posture: String?, modifier: Modifier = Modifier) {
    val isProne = posture?.lowercase() == "prone"
    
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "POSTURE",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = if (isProne) AlertCritical else if (posture != null) AccentGreen else TextMuted,
                modifier = Modifier
                    .size(28.dp)
                    .padding(vertical = 8.dp)
            )
            Text(
                text = posture ?: "--",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
        }
    }
}

@Composable
fun RadarCard(isActive: Boolean?, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "RADAR",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
            Icon(
                imageVector = Icons.Default.Sensors,
                contentDescription = null,
                tint = if (isActive == true) AccentIndigo else TextMuted,
                modifier = Modifier
                    .size(28.dp)
                    .padding(vertical = 8.dp)
            )
            Text(
                text = if (isActive == null) "--" else if (isActive) "Active" else "Inactive",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}

@Composable
fun EnvironmentCard(
    temp: Float?,
    co2: Int?,
    voc: Float?,
    gasIsSafe: Boolean?
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "NURSERY ENVIRONMENT",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                letterSpacing = 1.sp
            )
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                EnvironmentItem(label = "Temp", value = temp?.let { "${it}°" } ?: "--")
                EnvironmentItem(label = "CO₂", value = co2?.toString() ?: "--", unit = "ppm")
                EnvironmentItem(label = "VOC", value = voc?.toString() ?: "--")
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Gas",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = if (gasIsSafe == true) AccentGreen else if (gasIsSafe == false) AlertCritical else TextMuted,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun EnvironmentItem(label: String, value: String, unit: String? = null) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted
        )
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = AccentTeal,
            modifier = Modifier.padding(top = 4.dp)
        )
        if (unit != null) {
            Text(
                text = unit,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                color = TextMuted
            )
        }
    }
}

@Composable
fun SmartAnalysisCard(analysis: String?, onRefresh: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(AccentPurple, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(
                        text = "Smart Analysis",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AccentPurple
                    )
                }
                
                TextButton(onClick = onRefresh) {
                    Text("Refresh", color = AccentPurple)
                }
            }
            
            Text(
                text = analysis ?: "Waiting for sensor data to generate analysis.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                lineHeight = 22.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun DisconnectedOverlay(onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background.copy(alpha = 0.95f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                Icons.Default.WifiOff,
                contentDescription = null,
                tint = AlertCritical,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = "Device Not Connected",
                style = MaterialTheme.typography.titleLarge,
                color = AlertCritical
            )
            Text(
                text = "Waiting for Raspberry Pi...",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = AccentTeal)
            ) {
                Text("Retry Connection")
            }
        }
    }
}
