package ui.screens.root

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import domain.entities.fieldsmappers.FieldsMapperFactory
import domain.entities.usecase_factories.IGetListUseCaseFactory
import domain.entities.usecase_factories.IUpdateUseCaseFactory
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import settings.AppSettingsRepository
import ui.screens.nav_host.NavHostComponent
import ui.screens.nav_host.NavHostUi
import ui.screens.navigation_rail.NavigationRailComponent
import ui.screens.navigation_rail.NavigationRailUi


@Composable
fun RootUi() {

    val di = localDI()
    val appSettingsRepository by di.instance<AppSettingsRepository>()
    val lifecycle = LifecycleRegistry()
    val updateUseCaseFactory by di.instance<IUpdateUseCaseFactory>()
    val listUseCaseFactory by di.instance<IGetListUseCaseFactory>()
    val fieldsMapperFactory: FieldsMapperFactory by di.instance()
    val navHostComponent =
        remember {
            NavHostComponent(
                componentContext = DefaultComponentContext(lifecycle),
                fieldsMapperFactory = fieldsMapperFactory,
                appSettingsRepository = appSettingsRepository,
                updateUseCaseFactory = updateUseCaseFactory,
                getListUseCaseFactory = listUseCaseFactory
            )
        }

    Row(modifier = Modifier.background(MaterialTheme.colors.background)) {


        NavigationRailUi(NavigationRailComponent(onNavigateTo = {
            navHostComponent.setDestination(it.route)
        }))

        Box(modifier = Modifier.fillMaxHeight().weight(1f)) {
            NavHostUi(component = navHostComponent)
        }
    }
}