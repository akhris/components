package ui.screens

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
    Text(text = screen.route, style = MaterialTheme.typography.h2)
}