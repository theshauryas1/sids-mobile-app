package com.nurthure.monitor.ui.screens.trends

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nurthure.monitor.ui.theme.*

@Composable
fun TrendsScreen(
    viewModel: TrendsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        // Header
        Text(
            text = "Trends",
            style = MaterialTheme.typography.headlineLarge,
            color = TextPrimary,
            modifier = Modifier.padding(24.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Time tabs
            TimeTabRow(
                selectedTab = uiState.selectedTimeRange,
                onTabSelected = { viewModel.selectTimeRange(it) }
            )

            // Respiration chart
            ChartCard(
                title = "Respiration (mmWave)",
                badge = "_WAVE",
                average = uiState.respirationAvg?.let { "$it rpm" } ?: "-- rpm",
                hasData = uiState.hasRespirationData
            )

            // CO2 chart
            ChartCard(
                title = "CO₂ Levels (MH-Z19C)",
                badge = null,
                average = uiState.co2Avg?.let { "$it ppm" } ?: "-- ppm",
                hasData = uiState.hasCO2Data
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun TimeTabRow(
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    val tabs = listOf("1h", "24h", "7d", "1m")
    
    Card(
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            tabs.forEach { tab ->
                val isSelected = selectedTab == tab
                
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) Background else CardBackground)
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    TextButton(
                        onClick = { onTabSelected(tab) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = tab.uppercase(),
                            style = MaterialTheme.typography.labelLarge,
                            color = if (isSelected) AccentTeal else TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChartCard(
    title: String,
    badge: String?,
    average: String,
    hasData: Boolean
) {
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
                    if (badge != null) {
                        Surface(
                            color = AccentTeal.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = badge,
                                style = MaterialTheme.typography.labelSmall,
                                color = AccentTeal,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                }
                
                Text(
                    text = "Avg: $average",
                    style = MaterialTheme.typography.bodySmall,
                    color = AccentTeal
                )
            }

            // Chart placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(top = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (!hasData) {
                    Text(
                        text = "No data available",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted
                    )
                } else {
                    // Placeholder for Vico chart
                    Text(
                        text = "Chart data loading...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted
                    )
                }
            }
        }
    }
}
