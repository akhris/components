package ui.screens

import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import navigation.Screen
import ui.screens.datatypes.DataTypesScreen
import ui.screens.patterns.ScreenWithFilterSheet

@Composable
fun NavHost(route: String? = null) {
    val screen = when (route) {
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
        Screen.Projects.route -> {
            Screen.Projects
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
        Screen.Income -> {}
        Screen.Outcome -> {}
        Screen.Projects -> {
            ScreenWithFilterSheet(isOpened = true, content = {
                Icon(
                    modifier = Modifier.align(Alignment.Center),
                    imageVector = Icons.Rounded.ThumbUp,
                    tint = MaterialTheme.colors.onSurface,
                    contentDescription = "projects screen content"
                )
            }, filterContent = {
                Icon(
                    imageVector = Icons.Rounded.Star,
                    contentDescription = "projects screen content"
                )
            })
        }
        Screen.Types -> {
            DataTypesScreen()
        }
        Screen.Warehouse -> {

        }
        Screen.Settings -> {
            SettingsScreen()
        }
        Screen.Places -> {}
    }
}