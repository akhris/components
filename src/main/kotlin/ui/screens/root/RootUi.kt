package ui.screens.root

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.akhris.domain.core.entities.IEntity
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import domain.entities.*
import domain.entities.Unit
import domain.entities.fieldsmappers.FieldsMapperFactory
import domain.entities.usecase_factories.*
import navigation.NavItem
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import settings.AppSettingsRepository
import strings.LocalizedStrings
import strings.defaultLocalizedStrings
import ui.dialogs.Type
import ui.dialogs.TypesPickerDialog
import ui.entity_renderers.AddEntityDialog
import ui.screens.nav_host.NavHostComponent
import ui.screens.nav_host.NavHostUi
import ui.screens.navigation_rail.NavigationRailComponent
import ui.screens.navigation_rail.NavigationRailUi


@Composable
fun RootUi(localizedStrings: LocalizedStrings = defaultLocalizedStrings) {
    //todo use stringProvider
    val di = localDI()
    val appSettingsRepository by di.instance<AppSettingsRepository>()
    val lifecycle = LifecycleRegistry()
    val updateUseCaseFactory by di.instance<IUpdateUseCaseFactory>()
    val listUseCaseFactory by di.instance<IGetListUseCaseFactory>()
    val fieldsMapperFactory: FieldsMapperFactory by di.instance()
    val getUseCaseFactory: IGetUseCaseFactory by di.instance()
    val removeUseCaseFactory: IRemoveUseCaseFactory by di.instance()
    val insertUseCaseFactory: IInsertUseCaseFactory by di.instance()

    val navHostComponent =
        remember {
            NavHostComponent(
                componentContext = DefaultComponentContext(lifecycle),
                fieldsMapperFactory = fieldsMapperFactory,
                appSettingsRepository = appSettingsRepository,
                updateUseCaseFactory = updateUseCaseFactory,
                getListUseCaseFactory = listUseCaseFactory,
                getUseCaseFactory = getUseCaseFactory,
                removeUseCaseFactory = removeUseCaseFactory,
                insertUseCaseFactory = insertUseCaseFactory
            )
        }

    var addClickedNavItem by remember { mutableStateOf<NavItem?>(null) }

    Row(modifier = Modifier.background(MaterialTheme.colors.background)) {
        NavigationRailUi(NavigationRailComponent(onNavigateTo = {
            navHostComponent.setDestination(it.route)
        }, onAddButtonClicked = {
            addClickedNavItem = it
        }), localizedStrings = localizedStrings)

        Box(modifier = Modifier.fillMaxHeight().weight(1f)) {
            NavHostUi(component = navHostComponent, localizedStrings = localizedStrings)
        }
    }

    addClickedNavItem?.let { navItem ->
        HandleAddButtonClicks(navItem, onDismiss = { addClickedNavItem = null }, localizedStrings = localizedStrings)
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun HandleAddButtonClicks(navItem: NavItem, onDismiss: () -> kotlin.Unit, localizedStrings: LocalizedStrings) {
    var addEntity by remember { mutableStateOf<IEntity<*>?>(null) }
    val showPicker = remember(navItem, addEntity) { navItem == NavItem.DataTypes && addEntity == null }



    addEntity?.let {
        AddEntityDialog(it, onDismiss)
    }

    if (showPicker) {
        TypesPickerDialog(onItemPicked = {
            addEntity = when (it) {
                Type.Containers -> Container()
                Type.Items -> Item()
                Type.None -> null
                Type.ObjectType -> ObjectType()
                Type.Parameters -> Parameter()
                Type.Suppliers -> Supplier()
                Type.Units -> Unit()
            }
        }, onDismiss = {
            if (addEntity == null)
                onDismiss()
        }, localizedStrings = localizedStrings)
    }

    LaunchedEffect(navItem) {
        addEntity = when (navItem) {
            NavItem.Income -> ItemIncome()
            NavItem.Outcome -> ItemOutcome()
            NavItem.Projects -> Project()
            else -> null
        }
    }

}