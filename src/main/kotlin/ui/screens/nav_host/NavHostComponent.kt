package ui.screens.nav_host

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.bringToFront
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import domain.entities.usecase_factories.IGetListUseCaseFactory
import navigation.NavItem
import navigation.Screen
import settings.AppSettingsRepository
import ui.screens.settings.SettingsComponent
import ui.screens.types_of_data.TypesOfDataComponent

class NavHostComponent(
    componentContext: ComponentContext,
    private val appSettingsRepository: AppSettingsRepository,
    private val getListUseCaseFactory: IGetListUseCaseFactory
) :
    INavHost, ComponentContext by componentContext {

    private val router =
        router(
            initialConfiguration = Config(NavItem.getDefaultHome().route),
            handleBackButton = true, // Pop the back stack on back button press
            childFactory = ::createChild
        )

    override val routerState: Value<RouterState<Config, INavHost.Child>> = router.state

    private val _state = MutableValue(INavHost.Model(null))

    override val state: Value<INavHost.Model> = _state

    override fun setDestination(route: String) {
        router.bringToFront(Config(route))
    }

    private fun createChild(config: Config, componentContext: ComponentContext): INavHost.Child {
        val screen = when (config.route) {
            Screen.Warehouse.route -> null
            Screen.Income.route -> null
            Screen.Types.route -> INavHost.Child.TypesOfData(
                TypesOfDataComponent(
                    componentContext = componentContext,
                    getListUseCaseFactory = getListUseCaseFactory
                )
            )
            Screen.Settings.route -> INavHost.Child.Settings(
                SettingsComponent(
                    componentContext = componentContext,
                    appSettingsRepository = appSettingsRepository
                )
            )
            Screen.Projects.route -> null
            else -> null
        }
        return screen ?: throw UnsupportedOperationException("unknown root: ${config.route}")
    }


    @Parcelize
    data class Config(val route: String) : Parcelable

}