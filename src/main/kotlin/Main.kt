// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import di.di
import navigation.Screen
import org.kodein.di.compose.localDI
import org.kodein.di.compose.withDI
import org.kodein.di.instance
import ui.nav_panel.SidePanel
import ui.screens.NavHost
import ui.settings.AppSettingsRepository
import ui.theme.AppSettings
import ui.theme.AppTheme

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

    MaterialTheme {
        Button(onClick = {
            text = "Hello, Desktop!"
        }) {
            Text(text)
        }
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = AppSettings.appTitle,
        icon = painterResource("vector/grid_view_black_24dp.svg"),
        content = {
            mainWindow()
        }
    )
}

@Composable
private fun mainWindow() = withDI(di) {
    var route by remember { mutableStateOf<String?>(Screen.homeScreen.route) }

    val di = localDI()
    val settingsRepository: AppSettingsRepository by di.instance()

    val isLightTheme by remember(settingsRepository) {
        settingsRepository.isLightTheme
    }.collectAsState()

    AppTheme(darkTheme = !isLightTheme) {
        Row(modifier = Modifier.background(MaterialTheme.colors.background)) {
            SidePanel(route = route ?: "", onNavigateTo = {
                route = it
            })
            Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                NavHost(route = route)
            }
        }
    }
}

private fun databaseConnect() {
//    Database.connect()
}