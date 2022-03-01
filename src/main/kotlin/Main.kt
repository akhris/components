// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.akhris.domain.core.application.InsertEntity
import com.akhris.domain.core.utils.LogUtils
import di.di
import domain.application.*
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.kodein.di.compose.localDI
import org.kodein.di.compose.withDI
import org.kodein.di.instance
import persistence.exposed.DbSettings
import settings.AppSetting
import settings.AppSettingsRepository
import test.*
import ui.screens.root.RootUi
import ui.theme.AppSettings
import ui.theme.AppTheme

fun main() = application {
    LogUtils.isLogEnabled = true
    DbSettings.db   //connect to database
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

    val di = localDI()
    val settingsRepository: AppSettingsRepository by di.instance()

    PrepopulateDatabase()

    val isLightTheme by remember(settingsRepository) {
        settingsRepository
            .observeSetting(AppSettingsRepository.key_is_light_theme)
            .distinctUntilChanged()
            .map {
                if (it is AppSetting.BooleanSetting) {
                    it.value
                } else false
            }
    }.collectAsState(true)

    AppTheme(darkTheme = !isLightTheme) {
        RootUi()
    }
}

@Composable
private fun PrepopulateDatabase() {
    var wasInitialized by remember { mutableStateOf(false) }

    val di = localDI()


    val insertObjectType by di.instance<InsertObjectType>()
    val insertParameter by di.instance<InsertParameter>()
//    val insertUnit by di.instance<InsertUnit>()
    val insertItem by di.instance<InsertItem>()
    val insertContainer by di.instance<InsertContainer>()
    val insertSupplier by di.instance<InsertSupplier>()
    val insertItemIncome by di.instance<InsertItemIncome>()
    val insertItemOutcome by di.instance<InsertItemOutcome>()
    val insertProject by di.instance<InsertProject>()

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
//                objectTypesRepo.insert(it)
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
//                parameterRepo.insert(it)
                insertParameter(InsertEntity.Insert(it))
            }
//            listOf(
//                Units.Electronic.amps,
//                Units.Electronic.volts,
//                Units.Electronic.farads,
//                Units.Electronic.ohm,
//                Units.Electronic.watt,
//                Units.Common.grams,
//                Units.Common.meters,
//                Units.Common.pcs,
//                Units.Common.percent
//            ).forEach {
////                unitsRepository.insert(it)
//                insertUnit(InsertEntity.Insert(it))
//            }

            listOf(
                Containers.room407,
                Containers.shelving1,
                Containers.box1,
                Containers.box2,
                Containers.box3
            ).forEach {
//                containersRepository.insert(it)
                insertContainer(InsertEntity.Insert(it))
            }

            Suppliers.getAll().forEach {
//                suppliersRepository.insert(it)
                insertSupplier(InsertEntity.Insert(it))
            }

            listOf(Incomes.income1, Incomes.income2, Incomes.income3).forEach {
                insertItemIncome(InsertEntity.Insert(it))
            }

            listOf(Outcomes.outcome1, Outcomes.outcome2).forEach {
                insertItemOutcome(InsertEntity.Insert(it))
            }

            listOf(Projects.project1, Projects.project2).forEach {
                insertProject(InsertEntity.Insert(it))
            }

            wasInitialized = true
        }
    }


}

private fun databaseConnect() {
//    Database.connect()
}