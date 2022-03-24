package ui.screens.nav_host

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.bringToFront
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import domain.entities.*
import domain.entities.fieldsmappers.FieldsMapperFactory
import domain.entities.usecase_factories.IGetListUseCaseFactory
import domain.entities.usecase_factories.IGetUseCaseFactory
import domain.entities.usecase_factories.IRemoveUseCaseFactory
import domain.entities.usecase_factories.IUpdateUseCaseFactory
import navigation.NavItem
import navigation.Screen
import settings.AppSettingsRepository
import ui.screens.entities_screen.EntitiesScreenComponent
import ui.screens.settings.SettingsComponent

/**
 * Main navigation component that holds all destinations
 */
class NavHostComponent(
    componentContext: ComponentContext,
    private val fieldsMapperFactory: FieldsMapperFactory,
    private val appSettingsRepository: AppSettingsRepository,
    private val getUseCaseFactory: IGetUseCaseFactory,
    private val updateUseCaseFactory: IUpdateUseCaseFactory,
    private val getListUseCaseFactory: IGetListUseCaseFactory,
    private val removeUseCaseFactory: IRemoveUseCaseFactory
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
        return when (config.route) {
            Screen.Settings.route -> INavHost.Child.Settings(
                SettingsComponent(
                    componentContext = componentContext,
                    appSettingsRepository = appSettingsRepository
                )
            )
            else -> {
                val entities = when (config.route) {
                    Screen.Warehouse.route -> listOf(WarehouseItem::class)
                    Screen.Income.route -> listOf(ItemIncome::class)
                    Screen.Outcome.route -> listOf(ItemOutcome::class)
                    Screen.Types.route -> listOf(
                        ObjectType::class,
                        Parameter::class,
                        domain.entities.Unit::class,
                        Item::class,
                        Container::class,
                        Supplier::class,
                        Project::class
                    )
                    else -> throw UnsupportedOperationException("unknown root: ${config.route}")
                }
                INavHost.Child.EntitiesListWithSidePanel(
                    EntitiesScreenComponent(
                        componentContext = componentContext,
                        entityClasses = entities,
                        fieldsMapperFactory = fieldsMapperFactory,
                        getListUseCaseFactory = getListUseCaseFactory,
                        updateUseCaseFactory = updateUseCaseFactory,
                        removeUseCaseFactory = removeUseCaseFactory
                    )
                )
            }
        }
    }


    @Parcelize
    data class Config(val route: String) : Parcelable

}