// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.DrawerValue
import androidx.compose.material.MaterialTheme
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.akhris.domain.core.application.InsertEntity
import di.di
import domain.application.*
import navigation.Screen
import org.kodein.di.compose.localDI
import org.kodein.di.compose.withDI
import org.kodein.di.instance
import test.*
import ui.nav_panel.NavigationPanel
import ui.screens.NavHost
import ui.settings.AppSettingsRepository
import ui.theme.AppSettings
import ui.theme.AppTheme

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

    PrepopulateDatabase()

    val isLightTheme by remember(settingsRepository) {
        settingsRepository.isLightTheme
    }.collectAsState()

    AppTheme(darkTheme = !isLightTheme) {

        val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Open))


//        Scaffold(scaffoldState = scaffoldState,
//            topBar = {
//                TopAppBar(title = { Text("components") })
//            }, drawerContent = {
//                NavigationPanel(route = route ?: "", onNavigateTo = {
//                    route = it
//                })
//            },
//            drawerShape = MaterialTheme.shapes.large,
//            drawerGesturesEnabled = false, content = {
//                Box(modifier = Modifier.fillMaxHeight().padding(it)) {
//                    NavHost(route = route)
//                }
//            })

        Row(modifier = Modifier.background(MaterialTheme.colors.background)) {
            NavigationPanel(route = route ?: "", onNavigateTo = {
                route = it
            })
            Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                NavHost(route = route)
            }
        }
    }
}

@Composable
private fun PrepopulateDatabase() {
    var wasInitialized by remember { mutableStateOf(false) }

    val di = localDI()

    val insertObjectType by di.instance<InsertObjectType>()
    val insertParameter by di.instance<InsertParameter>()
    val insertUnit by di.instance<InsertUnit>()
    val insertItem by di.instance<InsertItem>()
    val insertContainer by di.instance<InsertContainer>()

    LaunchedEffect(wasInitialized) {
        if (!wasInitialized) {
            listOf(
                Items.Resistors.resistor1,
                Items.Resistors.resistor2,
                Items.Capacitors.capacitor1,
                Items.Capacitors.capacitor2
            ).forEach {
                insertItem(InsertEntity.Insert(it))
            }

            listOf(Types.resistorsType, Types.capacitorsType).forEach {
                insertObjectType(InsertEntity.Insert(it))
            }
            listOf(
                Parameters.Electronic.dielectricType,
                Parameters.Electronic.maxVoltage,
                Parameters.Electronic.packg,
                Parameters.Electronic.capacitance,
                Parameters.Electronic.resistance,
                Parameters.Electronic.dielectricType,
                Parameters.Electronic.tolerance,
                Parameters.Material.length,
                Parameters.Material.weight
            ).forEach {
                insertParameter(InsertEntity.Insert(it))
            }
            listOf(
                Units.Electronic.amps,
                Units.Electronic.volts,
                Units.Electronic.farads,
                Units.Electronic.ohm,
                Units.Electronic.watt,
                Units.Common.grams,
                Units.Common.meters,
                Units.Common.pcs,
                Units.Common.percent
            ).forEach {
                insertUnit(InsertEntity.Insert(it))
            }

            listOf(
                Containers.room407,
                Containers.shelving1,
                Containers.box1,
                Containers.box2,
                Containers.box3
            ).forEach {
                insertContainer(InsertEntity.Insert(it))
            }



            wasInitialized = true
        }
    }


}

private fun databaseConnect() {
//    Database.connect()
}