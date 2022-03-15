package ui.screens.entities_screen

import com.akhris.domain.core.entities.IEntity
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import domain.entities.usecase_factories.IGetListUseCaseFactory
import ui.screens.entities_screen.entities_list.EntitiesListComponent
import ui.screens.entities_screen.entities_sidepanel.EntitiesSidePanelComponent
import kotlin.reflect.KClass

class EntitiesScreenComponent(
    componentContext: ComponentContext,
    private val entityClasses: List<KClass<out IEntity<*>>>,
//    private val fieldsMapperFactory: FieldsMapperFactory,
    private val getListUseCaseFactory: IGetListUseCaseFactory
) : IEntitiesScreen, ComponentContext by componentContext {


    private val listRouter =
        router(
            initialConfiguration = EntitiesListConfig.EntitiesList(entityClass = entityClasses.firstOrNull()),
            key = "list_router",
            childFactory = ::createListChild
        )


    private val sidePanelRouter =
        router(
            initialConfiguration = SidePanelConfig.SidePanel,
            key = "side_panel_router",
            childFactory = ::createSidePanel
        )


    override val listRouterState: Value<RouterState<*, IEntitiesScreen.ListChild>> = listRouter.state
    override val sidePanelRouterState: Value<RouterState<*, IEntitiesScreen.SidePanelChild>> = sidePanelRouter.state


    private fun createListChild(
        entitiesListConfig: EntitiesListConfig,
        componentContext: ComponentContext
    ): IEntitiesScreen.ListChild {
        return when (entitiesListConfig) {
            is EntitiesListConfig.EntitiesList -> IEntitiesScreen.ListChild.List(
                component = EntitiesListComponent(
                    componentContext = componentContext,
                    getEntities = entitiesListConfig.entityClass?.let { getListUseCaseFactory.getListUseCase(it) }
                )
            )
        }
    }

    private fun createSidePanel(
        sidePanelConfig: SidePanelConfig,
        componentContext: ComponentContext
    ): IEntitiesScreen.SidePanelChild {
        return when (sidePanelConfig) {
            SidePanelConfig.SidePanel -> IEntitiesScreen.SidePanelChild.SidePanel(
                EntitiesSidePanelComponent(
                    entityClasses = entityClasses,
                    componentContext = componentContext,
                    onEntitySelected = {newEntity->
                        listRouter.navigate { stack ->
                            stack
                                .dropLastWhile { it is EntitiesListConfig.EntitiesList }
                                .plus(EntitiesListConfig.EntitiesList(entityClass = newEntity))
                        }
                    }
                )
            )
        }
    }


    sealed class EntitiesListConfig : Parcelable {


        @Parcelize
        data class EntitiesList(val entityClass: KClass<out IEntity<*>>?) : EntitiesListConfig()
    }

    sealed class SidePanelConfig : Parcelable {
        @Parcelize
        object SidePanel : SidePanelConfig()
    }
}