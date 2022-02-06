package ui.screens

import androidx.compose.runtime.Composable
import navigation.Screen

@Composable
fun NavHost(route: String? = null) {
    val screen = when (route) {
        Screen.Database.route -> {
            Screen.Database
        }
        Screen.Warehouse.route -> {
            Screen.Warehouse
        }
        Screen.Income.route -> {
            Screen.Income
        }
        Screen.Types.route -> {
            Screen.Types
        }
        Screen.Settings.route -> {
            Screen.Settings
        }
        else -> {
            null
        }
    }
    screen?.let {
        NavigateToScreen(it)
    }
}

@Composable
private fun NavigateToScreen(screen: Screen) {
    when (screen) {
        Screen.Database -> {}
        Screen.Income -> {}
        Screen.Outcome -> {}
        Screen.Projects -> {}
        Screen.Types -> {
            DataTypesScreen()
        }
        Screen.Warehouse -> {

        }
        Screen.Settings -> {
            SettingsScreen()
        }
    }
}