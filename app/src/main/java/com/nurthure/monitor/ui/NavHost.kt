package com.nurthure.monitor.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nurthure.monitor.ui.components.BottomNavBar
import com.nurthure.monitor.ui.screens.alerts.AlertsScreen
import com.nurthure.monitor.ui.screens.monitor.MonitorScreen
import com.nurthure.monitor.ui.screens.settings.SettingsScreen
import com.nurthure.monitor.ui.screens.trends.TrendsScreen

sealed class Screen(val route: String, val title: String) {
    object Monitor : Screen("monitor", "Monitor")
    object Trends : Screen("trends", "Trends")
    object Alerts : Screen("alerts", "Alerts")
    object Settings : Screen("settings", "Settings")
}

@Composable
fun NurthureNavHost() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Monitor.route

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Monitor.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Monitor.route) {
                MonitorScreen()
            }
            composable(Screen.Trends.route) {
                TrendsScreen()
            }
            composable(Screen.Alerts.route) {
                AlertsScreen()
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
        }
    }
}
