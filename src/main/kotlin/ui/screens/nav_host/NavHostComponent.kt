package ui.screens.nav_host

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.bringToFront
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import domain.entities.ItemIncome
import domain.entities.fieldsmappers.FieldsMapperFactory
import domain.entities.usecase_factories.IGetListUseCaseFactory
import domain.entities.usecase_factories.IUpdateUseCaseFactory
import navigation.NavItem
import navigation.Screen
import settings.AppSettingsRepository
import ui.screens.entity_screen_with_filter.EntityWithFilterComponent
import ui.screens.settings.SettingsComponent
import ui.screens.types_of_data.TypesOfDataComponent

/**
 * Main navigation component that holds all destinations
 */
class NavHostComponent(
    componentContext: ComponentContext,
    private val fieldsMapperFactory: FieldsMapperFactory,
    private val appSettingsRepository: AppSettingsRepository,
    private val updateUseCaseFactory: IUpdateUseCaseFactory,
    private val getListUseCaseFactory: IGetListUseCaseFactory
) :
    INavHost, ComponentContext by componentContext {

    /**
     * Router instance
     */
    private val router =
        router(
            initialConfiguration = Config(NavItem.getDefaultHome().route),
            handleBackButton = true, // Pop the back stack on back button press
            childFactory = ::createChild
        )

    /**
     * Exposes Router State
     */
    override val routerState: Value<RouterState<Config, INavHost.Child>> = router.state

    /**
     * Navigate to destination by route.
     */
    override fun setDestination(route: String) {
        router.bringToFront(Config(route))
    }

    /**
     * Child components factory.
     * Creates
     */
    private fun createChild(config: Config, componentContext: ComponentContext): INavHost.Child {
        val screen = when (config.route) {
            Screen.Warehouse.route -> null
            Screen.Income.route -> INavHost.Child.EntitiesListWithFilter(
                EntityWithFilterComponent(
                    componentContext = componentContext,
                    entityClass = ItemIncome::class,
                    fieldsMapperFactory = fieldsMapperFactory
                )
            )
            Screen.Types.route -> INavHost.Child.TypesOfData(
                TypesOfDataComponent(
                    componentContext = componentContext,
                    updateUseCaseFactory = updateUseCaseFactory,
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