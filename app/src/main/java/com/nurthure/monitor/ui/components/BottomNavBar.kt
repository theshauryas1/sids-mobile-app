package com.nurthure.monitor.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.nurthure.monitor.ui.Screen
import com.nurthure.monitor.ui.theme.AccentTeal
import com.nurthure.monitor.ui.theme.CardBackground
import com.nurthure.monitor.ui.theme.TextMuted

data class NavItem(
    val screen: Screen,
    val icon: ImageVector,
    val label: String
)

val navItems = listOf(
    NavItem(Screen.Monitor, Icons.Default.MonitorHeart, "Monitor"),
    NavItem(Screen.Trends, Icons.Default.History, "History"),
    NavItem(Screen.Alerts, Icons.Default.Notifications, "Alerts"),
    NavItem(Screen.Settings, Icons.Default.Settings, "Settings")
)

@Composable
fun BottomNavBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = CardBackground
    ) {
        navItems.forEach { item ->
            val selected = currentRoute == item.screen.route
            
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(item.screen.route) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(text = item.label)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AccentTeal,
                    selectedTextColor = AccentTeal,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted,
                    indicatorColor = CardBackground
                )
            )
        }
    }
}
