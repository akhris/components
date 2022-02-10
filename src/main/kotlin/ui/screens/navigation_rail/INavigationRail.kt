package ui.screens.navigation_rail

import com.arkivanov.decompose.value.Value
import navigation.NavItem

interface INavigationRail {

    val models: Value<Model>

    fun onDestinationSelected(destination: NavItem)

    data class Model(
        val destinations: List<NavItem>,
        val currentDestination: NavItem
    )


}