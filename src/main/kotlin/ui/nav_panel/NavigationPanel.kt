package ui.nav_panel

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import navigation.NavItem
import ui.theme.SidePanelSettings


@Composable
fun SidePanel(isExpandable: Boolean = false, route: String = "", onNavigateTo: ((route: String) -> Unit)? = null) {

    var isExpanded by remember { mutableStateOf(false) }

    val panelWidth by animateDpAsState(
        when (isExpanded) {
            true -> SidePanelSettings.widthExpanded
            false -> SidePanelSettings.widthCollapsed
        }
    )

    var selectedRoute by remember(route) { mutableStateOf(route) }

    NavigationRail(
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
        } else null,
        content = {
            NavItem.getItems().forEach { item ->
                NavigationRailItem(
                    alwaysShowLabel = panelWidth == SidePanelSettings.widthExpanded,
                    selected = selectedRoute == item.route,
                    icon = {
                        Icon(
                            painter = painterResource(item.pathToIcon),
                            contentDescription = "navigation item",
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                    },
                    label = {
                        Text(item.title)
                    },
                    onClick = {
                        selectedRoute = item.route
                        onNavigateTo?.invoke(item.route)
                    }
                )
            }
        }
    )
}