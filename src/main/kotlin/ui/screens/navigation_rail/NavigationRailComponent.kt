package ui.screens.navigation_rail

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce
import navigation.NavItem

class NavigationRailComponent(private val onNavigateTo: (NavItem) -> Unit) :
    INavigationRail{

    private val _models = MutableValue(
        INavigationRail.Model(
            destinations = NavItem.getItems(),
            currentDestination = NavItem.getDefaultHome()
        )
    )

    override val models: Value<INavigationRail.Model> = _models


    override fun onDestinationSelected(destination: NavItem) {
        onNavigateTo(destination)
        _models.reduce {
            it.copy(currentDestination = destination)
        }
    }


}