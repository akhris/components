package ui.screens.navigation_rail

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import ui.theme.NavigationPanelSettings
import utils.toLocalizedString

@Composable
fun NavigationRailUi(component: INavigationRail) {
    val navModel by component.models.subscribeAsState()

    val isExpandable by remember { mutableStateOf(false) }

    var isExpanded by remember { mutableStateOf(false) }

    val panelWidth by animateDpAsState(
        when (isExpanded) {
            true -> NavigationPanelSettings.widthExpanded
            false -> NavigationPanelSettings.widthCollapsed
        }
    )

    val destinations = remember(navModel) { navModel.destinations }
    val selectedDestination = remember(navModel) { navModel.currentDestination }

    NavigationRail(
        elevation = NavigationPanelSettings.elevation,
        modifier = Modifier.width(panelWidth),
        header = if (isExpandable) {
            {
                IconButton(modifier = Modifier.align(Alignment.End), onClick = {
                    isExpanded = !isExpanded
                },
                    content = {
                        Icon(
                            imageVector = when (isExpanded) {
                                true -> Icons.Rounded.KeyboardArrowLeft
                                false -> Icons.Rounded.KeyboardArrowRight
                            },
                            contentDescription = "expand or collapse icon"
                        )
                    }
                )
            }
        } else {
            {
                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(8.dp),
                    backgroundColor = MaterialTheme.colors.primarySurface,
                    onClick = {
                        component.onAddButtonClicked()
                    },
                    content = {
                        Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add button")
                    }
                )
            }
        },
        content = {
            destinations.forEach { item ->
                NavigationRailItem(
                    alwaysShowLabel = panelWidth == NavigationPanelSettings.widthExpanded,
                    selected = selectedDestination == item,
                    icon = {
                        Icon(
                            painter = painterResource(item.pathToIcon),
                            contentDescription = "navigation item",
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                    },
                    label = {
                        Text(item.title.toLocalizedString())
                    },
                    onClick = {
                        component.onDestinationSelected(item)
                    }
                )
            }
        }
    )
}