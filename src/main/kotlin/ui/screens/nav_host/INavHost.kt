package ui.screens.nav_host

import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import navigation.Screen
import ui.screens.settings.ISettings
import ui.screens.types_of_data.ITypesOfData

interface INavHost {

    val state: Value<Model>

    fun setDestination(route: String)

    data class Model(val destination: Screen?)

    val routerState: Value<RouterState<*, Child>>

    sealed class Child {
        class Settings(val component: ISettings) : Child()
        class TypesOfData(val component: ITypesOfData): Child()
    }
}