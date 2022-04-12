// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.akhris.domain.core.application.InsertEntity
import com.akhris.domain.core.utils.LogUtils
import com.akhris.domain.core.utils.log
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import di.di
import domain.application.*
import domain.entities.fieldsmappers.FieldsMapperFactory
import domain.entities.usecase_factories.*
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.kodein.di.DI
import org.kodein.di.compose.localDI
import org.kodein.di.compose.withDI
import org.kodein.di.instance
import persistence.ExposedDbSettings
import persistence.columnMappers.ColumnMappersFactory
import persistence.repository.IPagingRepository
import persistence.repository.Specification
import settings.AppSetting
import settings.AppSettingsRepository
import strings.LocalizedStrings
import strings.StringProvider
import test.*
import ui.screens.nav_host.INavHost
import ui.screens.nav_host.NavHostComponent
import ui.screens.root.RootUi
import ui.theme.AppSettings
import ui.theme.AppTheme

fun main() {
    LogUtils.isLogEnabled = true
    ExposedDbSettings.db   //connect to database

    val appSettingsRepository by di.instance<AppSettingsRepository>()
    val lifecycle = LifecycleRegistry()
    val updateUseCaseFactory by di.instance<IUpdateUseCaseFactory>()
    val listUseCaseFactory by di.instance<IGetListUseCaseFactory>()
    val fieldsMapperFactory: FieldsMapperFactory by di.instance()
    val columnMappersFactory: ColumnMappersFactory by di.instance()
    val getUseCaseFactory: IGetUseCaseFactory by di.instance()
    val removeUseCaseFactory: IRemoveUseCaseFactory by di.instance()
    val insertUseCaseFactory: IInsertUseCaseFactory by di.instance()

    val rootComponent = NavHostComponent(
        componentContext = DefaultComponentContext(lifecycle),
        fieldsMapperFactory = fieldsMapperFactory,
        columnMappersFactory = columnMappersFactory,
        appSettingsRepository = appSettingsRepository,
        getUseCaseFactory = getUseCaseFactory,
        updateUseCaseFactory = updateUseCaseFactory,
        getListUseCaseFactory = listUseCaseFactory,
        insertUseCaseFactory = insertUseCaseFactory,
        removeUseCaseFactory = removeUseCaseFactory
    )

    application {

        Window(
            onCloseRequest = ::exitApplication,
            title = AppSettings.appTitle,
            icon = painterResource("vector/grid_view_black_24dp.svg"),
            content = {
                mainWindow(rootComponent)
            }
        )
    }

}

//fun main() =
//    application {
//
//    }

@Composable
private fun mainWindow(rootComponent: INavHost) = withDI(di) {


    val di = localDI()
    val settingsRepository: AppSettingsRepository by di.instance()

    PrepopulateDatabase()

    val isDarkTheme by remember(settingsRepository) {
        settingsRepository
            .observeSetting(AppSettingsRepository.key_is_dark_theme)
            .distinctUntilChanged()
            .map {
                if (it is AppSetting.BooleanSetting) {
                    it.value
                } else false
            }
    }.collectAsState(null)

    val stringProvider by remember(settingsRepository) {
        settingsRepository.getLocalizedStringProvider().distinctUntilChanged()
    }.collectAsState(null)


    remember(stringProvider) {
        log("got new string provider from settingsRepository: $stringProvider")
    }
    Root(rootComponent, isDarkTheme ?: false, stringProvider ?: StringProvider())


}

@Composable
private fun Root(component: INavHost, isDarkTheme: Boolean, stringProvider: StringProvider) {

    val localizedStrings =
        remember<LocalizedStrings>(stringProvider) { { stringID -> stringProvider.getLocalizedString(stringID.name) } }

    AppTheme(darkTheme = isDarkTheme) {
        RootUi(component = component, localizedStrings = localizedStrings)
    }
}

@Composable
private fun PrepopulateDatabase() {
    var wasInitialized by remember { mutableStateOf(false) }

    val di = localDI()
    val getItemIncomesList by di.instance<GetItemIncomesList>()

    LaunchedEffect(wasInitialized) {

        val incomesCount = (getItemIncomesList.repo as? IPagingRepository)?.getItemsCount(Specification.QueryAll) ?: 0L
        log("incomes count: $incomesCount")

        if (incomesCount > 0) {
            log("database is already populated")
            return@LaunchedEffect
        }
        if (!wasInitialized) {
            prepopulateUnits(di)
            prepopulateParameters(di)
            prepopulateContainers(di)
            prepopulateObjectTypes(di)
            prepopulateSuppliers(di)
            prepopulateItems(di)
            prepopulateIncomes(di)
            prepopulateOutcomes(di)
            prepopulateProjects(di)
            wasInitialized = true
        }
    }

}

private suspend fun prepopulateItems(di: DI) {
    log("prepopulating items...")
    val insertItem by di.instance<InsertItem>()
    listOf(
        Items.Resistors.resistor1,
        Items.Resistors.resistor2,
        Items.Capacitors.capacitor1,
        Items.Capacitors.capacitor2
    ).forEach {
        insertItem(InsertEntity.Insert(it))
    }
}

private suspend fun prepopulateObjectTypes(di: DI) {
    log("prepopulating object types...")
    val insertObjectType by di.instance<InsertObjectType>()
    listOf(Types.resistorsType, Types.capacitorsType).forEach {
        insertObjectType(InsertEntity.Insert(it))
    }
}

private suspend fun prepopulateParameters(di: DI) {
    log("prepopulating parameters...")
    val insertParameter by di.instance<InsertParameter>()
    listOf(
        Parameters.Electronic.dielectricType,
        Parameters.Electronic.maxVoltage,
        Parameters.Electronic.packg,
        Parameters.Electronic.capacitance,
        Parameters.Electronic.resistance,
        Parameters.Electronic.wattage,
        Parameters.Electronic.tolerance,
        Parameters.Material.length,
        Parameters.Material.weight
    ).forEach {
        insertParameter(InsertEntity.Insert(it))
    }
}

private suspend fun prepopulateUnits(di: DI) {
    log("prepopulating units...")
    val insertUnit by di.instance<InsertUnit>()
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
}

private suspend fun prepopulateContainers(di: DI) {
    log("prepopulating containers...")
    listOf(
        Containers.room407,
        Containers.shelving1,
        Containers.box1,
        Containers.box2,
        Containers.box3
    ).forEach {
        val insertContainer by di.instance<InsertContainer>()
        insertContainer(InsertEntity.Insert(it))
    }
}

private suspend fun prepopulateSuppliers(di: DI) {
    log("prepopulating suppliers...")
    val insertSupplier by di.instance<InsertSupplier>()
    Suppliers.getAll().forEach {
        insertSupplier(InsertEntity.Insert(it))
    }
}

private suspend fun prepopulateIncomes(di: DI) {
    log("prepopulating incomes...")
    val insertItemIncome by di.instance<InsertItemIncome>()
    listOf(Incomes.income1, Incomes.income2, Incomes.income3).forEach {
        insertItemIncome(InsertEntity.Insert(it))
    }
}


private suspend fun prepopulateOutcomes(di: DI) {
    log("prepopulating outcomes...")
    val insertItemOutcome by di.instance<InsertItemOutcome>()
    listOf(Outcomes.outcome1, Outcomes.outcome2).forEach {
        insertItemOutcome(InsertEntity.Insert(it))
    }
}

private suspend fun prepopulateProjects(di: DI) {
    log("prepopulating projects...")
    val insertProject by di.instance<InsertProject>()
    listOf(Projects.project1, Projects.project2).forEach {
        insertProject(InsertEntity.Insert(it))
    }
}


private fun databaseConnect() {
//    Database.connect()
}