package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import ui.settings.AppSettingsRepository

@Composable
fun SettingsScreen() {
    val di = localDI()
    val settingsRepository: AppSettingsRepository by di.instance()

    SettingsScreenContent(settingsRepository)
}

@Composable
fun SettingsScreenContent(settingsRepository: AppSettingsRepository) {

    val isLightTheme by remember(settingsRepository) { settingsRepository.isLightTheme }.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(vertical = 8.dp, horizontal = 32.dp)) {
        LightDarkThemeSelector(isLightTheme) {
            settingsRepository.setIsLightTheme(it)
        }
    }
}

@Composable
private fun LightDarkThemeSelector(isLight: Boolean, onThemeChanged: (Boolean) -> Unit) {

    SwitchPreference(
        isChecked = !isLight,
        onCheckedChange = { onThemeChanged(!it) },
        title = "Dark mode",
        description = "Toggle theme colors to light/dark mode",
        iconPath = "vector/dark_mode_black_24dp.svg"
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SwitchPreference(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    title: String = "",
    description: String? = null,
    iconPath: String? = null
) {
    Card(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
        ListItem(text =
        {
            Text(text = title)
        }, secondaryText = description?.let {
            {
                Text(text = it)
            }
        }, trailing = {
            Switch(checked = isChecked, onCheckedChange = onCheckedChange)
        }, icon = iconPath?.let {
            {
                Icon(painter = painterResource(it), contentDescription = "switch preference icon")
            }
        })
    }
}
