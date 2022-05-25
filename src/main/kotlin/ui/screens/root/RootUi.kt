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
import domain.entities.*
import domain.entities.Unit
import navigation.NavItem
import strings.StringProvider
import ui.dialogs.Type
import ui.dialogs.TypesPickerDialog
import ui.entity_renderers.AddEntityDialog
import ui.screens.nav_host.INavHost
import ui.screens.nav_host.NavHostUi
import ui.screens.navigation_rail.NavigationRailComponent
import ui.screens.navigation_rail.NavigationRailUi


@Composable
fun RootUi(component: INavHost, stringProvider: StringProvider) {
    //todo use stringProvider


    var addClickedNavItem by remember { mutableStateOf<NavItem?>(null) }

    Row(modifier = Modifier.background(MaterialTheme.colors.background)) {
        NavigationRailUi(NavigationRailComponent(onNavigateTo = {
            component.setDestination(it.route)
        }, onAddButtonClicked = {
            addClickedNavItem = it
        }), stringProvider = stringProvider)

        Box(modifier = Modifier.fillMaxHeight().weight(1f)) {
            NavHostUi(component = component, stringProvider = stringProvider)
        }
    }

    addClickedNavItem?.let { navItem ->
        HandleAddButtonClicks(navItem, onDismiss = { addClickedNavItem = null }, stringProvider = stringProvider)
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun HandleAddButtonClicks(navItem: NavItem, onDismiss: () -> kotlin.Unit, stringProvider: StringProvider) {
    var addEntity by remember { mutableStateOf<IEntity<*>?>(null) }
    val showPicker = remember(navItem, addEntity) { navItem == NavItem.DataTypes && addEntity == null }



    addEntity?.let {
        AddEntityDialog(it, stringProvider = stringProvider, onDismiss)
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
                Type.Invoices -> Invoice()
            }
        }, onDismiss = {
            if (addEntity == null)
                onDismiss()
        }, stringProvider = stringProvider)
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