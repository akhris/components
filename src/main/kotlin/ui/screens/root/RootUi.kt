package ui.screens.root

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.akhris.domain.core.entities.IEntity
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import domain.entities.*
import domain.entities.Unit
import domain.entities.fieldsmappers.FieldsMapperFactory
import domain.entities.usecase_factories.IGetListUseCaseFactory
import domain.entities.usecase_factories.IGetUseCaseFactory
import domain.entities.usecase_factories.IUpdateUseCaseFactory
import navigation.NavItem
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import settings.AppSettingsRepository
import ui.dialogs.ListPickerDialog
import ui.entity_renderers.AddEntityDialog
import ui.screens.nav_host.NavHostComponent
import ui.screens.nav_host.NavHostUi
import ui.screens.navigation_rail.NavigationRailComponent
import ui.screens.navigation_rail.NavigationRailUi
import ui.screens.types_of_data.types_selector.ITypesSelector
import utils.toLocalizedString


@Composable
fun RootUi() {

    val di = localDI()
    val appSettingsRepository by di.instance<AppSettingsRepository>()
    val lifecycle = LifecycleRegistry()
    val updateUseCaseFactory by di.instance<IUpdateUseCaseFactory>()
    val listUseCaseFactory by di.instance<IGetListUseCaseFactory>()
    val fieldsMapperFactory: FieldsMapperFactory by di.instance()
    val getUseCaseFactory: IGetUseCaseFactory by di.instance()
    val navHostComponent =
        remember {
            NavHostComponent(
                componentContext = DefaultComponentContext(lifecycle),
                fieldsMapperFactory = fieldsMapperFactory,
                appSettingsRepository = appSettingsRepository,
                updateUseCaseFactory = updateUseCaseFactory,
                getListUseCaseFactory = listUseCaseFactory,
                getUseCaseFactory = getUseCaseFactory
            )
        }

    var addClickedNavItem by remember { mutableStateOf<NavItem?>(null) }



    Row(modifier = Modifier.background(MaterialTheme.colors.background)) {


        NavigationRailUi(NavigationRailComponent(onNavigateTo = {
            navHostComponent.setDestination(it.route)
        }, onAddButtonClicked = {
            addClickedNavItem = it
        }))

        Box(modifier = Modifier.fillMaxHeight().weight(1f)) {
            NavHostUi(component = navHostComponent)
        }
    }

    addClickedNavItem?.let { navItem ->
        HandleAddButtonClicks(navItem, onDismiss = { addClickedNavItem = null })
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun HandleAddButtonClicks(navItem: NavItem, onDismiss: () -> kotlin.Unit) {
    var addEntity by remember { mutableStateOf<IEntity<*>?>(null) }
    val showPicker = remember(navItem, addEntity) { navItem == NavItem.DataTypes && addEntity == null }



    addEntity?.let {
        AddEntityDialog(it, onDismiss)
    }

    if (showPicker) {
        val types = ITypesSelector.Type.getAllTypes()

        ListPickerDialog(
            items = types,
            title = "pick data type",
            mapper = { type ->
                ListItem(
                    modifier = Modifier.fillMaxWidth(),
                    text = {
                        Text(text = type.name?.toLocalizedString() ?: "")
                    }, secondaryText = {
                        Text(text = type.description?.toLocalizedString() ?: "")
                    }
                )
            },
            onItemPicked = {
                addEntity = when (it) {
                    ITypesSelector.Type.Containers -> Container()
                    ITypesSelector.Type.Items -> Item()
                    ITypesSelector.Type.None -> null
                    ITypesSelector.Type.ObjectType -> ObjectType()
                    ITypesSelector.Type.Parameters -> Parameter()
                    ITypesSelector.Type.Suppliers -> Supplier()
                    ITypesSelector.Type.Units -> Unit()
                }
            },
            onDismiss = {
                if (addEntity == null)
                    onDismiss()
            }
        )
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