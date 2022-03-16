package ui.screens.entities_screen

import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.utils.log
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import domain.entities.*
import domain.entities.fieldsmappers.FieldsMapperFactory
import domain.entities.usecase_factories.IGetListUseCaseFactory
import strings.Strings
import ui.screens.entities_screen.entities_filter.EntitiesFilterComponent
import ui.screens.entities_screen.entities_list.EntitiesListComponent
import ui.screens.entities_screen.entities_selector.EntitiesSelectorComponent
import ui.screens.entities_screen.entities_view_settings.EntitiesViewSettingsComponent
import kotlin.reflect.KClass

class EntitiesScreenComponent(
    componentContext: ComponentContext,
    private val entityClasses: List<KClass<out IEntity<*>>>,
    private val fieldsMapperFactory: FieldsMapperFactory,
    private val getListUseCaseFactory: IGetListUseCaseFactory
) : IEntitiesScreen, ComponentContext by componentContext {

    private val _state = MutableValue(IEntitiesScreen.Model())

    override val state: Value<IEntitiesScreen.Model> = _state

    private val listRouter =
        router(
            initialConfiguration = EntitiesListConfig.EntitiesList(entityClass = entityClasses.firstOrNull()),
            key = "list_router",
            childFactory = ::createListChild
        )


    private val sidePanelRouter =
        router(
            initialConfiguration = EntitiesSelectorConfig.EntitiesSelector,
            key = "side_panel_router",
            childFactory = ::createSelectorChild
        )

    private val filterRouter =
        router(
            initialConfiguration = EntitiesFilterConfig.EntitiesFilter(listOf()),
            key = "filter_router",
            childFactory = ::createFilterChild
        )

    private val viewSettingsRouter =
        router(
            initialConfiguration = EntitiesViewSettingsConfig.ViewSettings,
            key = "view_settings_router",
            childFactory = ::createViewSettingsChild
        )

    override val listRouterState: Value<RouterState<*, IEntitiesScreen.ListChild>> = listRouter.state

    override val selectorRouterState: Value<RouterState<*, IEntitiesScreen.EntitiesSelectorChild>> =
        sidePanelRouter.state

    override val filterRouterState: Value<RouterState<*, IEntitiesScreen.EntitiesFilterChild>> =
        filterRouter.state

    override val viewSettingsRouterState: Value<RouterState<*, IEntitiesScreen.ViewSettingsChild>> =
        viewSettingsRouter.state

    private fun createListChild(
        entitiesListConfig: EntitiesListConfig,
        componentContext: ComponentContext
    ): IEntitiesScreen.ListChild {
        return when (entitiesListConfig) {
            is EntitiesListConfig.EntitiesList -> IEntitiesScreen.ListChild.List(
                component = EntitiesListComponent(
                    componentContext = componentContext,
                    getEntities = entitiesListConfig.entityClass?.let { getListUseCaseFactory.getListUseCase(it) },
                    onEntitiesLoaded = { entities ->
                        filterRouter.navigate { stack ->
                            stack.dropLastWhile { it is EntitiesFilterConfig.EntitiesFilter }
                                .plus(EntitiesFilterConfig.EntitiesFilter(entities = entities))
                        }
                    }
                )
            )
        }
    }

    private fun createSelectorChild(
        entitiesSelectorConfig: EntitiesSelectorConfig,
        componentContext: ComponentContext
    ): IEntitiesScreen.EntitiesSelectorChild {
        return when (entitiesSelectorConfig) {
            EntitiesSelectorConfig.EntitiesSelector -> IEntitiesScreen.EntitiesSelectorChild.EntitiesSelector(
                EntitiesSelectorComponent(
                    entityClasses = entityClasses,
                    componentContext = componentContext,
                    onEntitySelected = { newEntity ->
                        listRouter.navigate { stack ->
                            stack
                                .dropLastWhile { it is EntitiesListConfig.EntitiesList }
                                .plus(EntitiesListConfig.EntitiesList(entityClass = newEntity))
                        }
                        updateTitleAndDescription(newEntity)
                    }
                )
            )
        }
    }

    private fun createFilterChild(
        entitiesFilterConfig: EntitiesFilterConfig,
        componentContext: ComponentContext
    ): IEntitiesScreen.EntitiesFilterChild {
        return when (entitiesFilterConfig) {
            is EntitiesFilterConfig.EntitiesFilter -> IEntitiesScreen.EntitiesFilterChild.EntitiesFilter(
                EntitiesFilterComponent(
                    componentContext = componentContext,
                    entities = entitiesFilterConfig.entities,
                    mapperFactory = fieldsMapperFactory
                )
            )
        }
    }

    private fun createViewSettingsChild(
        entitiesViewSettingsConfig: EntitiesViewSettingsConfig,
        componentContext: ComponentContext
    ): IEntitiesScreen.ViewSettingsChild {
        return when (entitiesViewSettingsConfig) {
            EntitiesViewSettingsConfig.ViewSettings -> IEntitiesScreen.ViewSettingsChild.ViewSettings(
                EntitiesViewSettingsComponent(
                    componentContext = componentContext,
                    onTypeChanged = {
                        //todo redraw entities list here
                        log("type changed: $it")
                    }
                )
            )
        }
    }

    private fun updateTitleAndDescription(entityClass: KClass<out IEntity<*>>) {
        _state.reduce {
            it.copy(screenTitle = entityClass.title, screenDescription = entityClass.description)
        }
    }

    init {
        entityClasses.firstOrNull()?.let {
            updateTitleAndDescription(it)
        }
    }


    sealed class EntitiesListConfig : Parcelable {
        @Parcelize
        data class EntitiesList(val entityClass: KClass<out IEntity<*>>?) : EntitiesListConfig()
    }

    sealed class EntitiesSelectorConfig : Parcelable {
        @Parcelize
        object EntitiesSelector : EntitiesSelectorConfig()
    }

    sealed class EntitiesFilterConfig : Parcelable {
        @Parcelize
        data class EntitiesFilter(val entities: List<IEntity<*>>) : EntitiesFilterConfig()
    }

    sealed class EntitiesViewSettingsConfig : Parcelable {
        @Parcelize
        object ViewSettings : EntitiesViewSettingsConfig()
    }


}

val KClass<out IEntity<*>>.title: Strings?
    get() {
        return when (this) {
            ObjectType::class -> Strings.TypesOfData.types_title
            Parameter::class -> Strings.TypesOfData.parameters_title
            domain.entities.Unit::class -> Strings.TypesOfData.units_title
            Item::class -> Strings.TypesOfData.items_title
            Container::class -> Strings.TypesOfData.containers_title
            Supplier::class -> Strings.TypesOfData.suppliers_title
            Project::class -> Strings.TypesOfData.projects_title
            else -> null
        }
    }

val KClass<out IEntity<*>>.description: Strings?
    get() {
        return when (this) {
            ObjectType::class -> Strings.TypesOfData.types_description
            Parameter::class -> Strings.TypesOfData.parameters_description
            domain.entities.Unit::class -> Strings.TypesOfData.units_description
            Item::class -> Strings.TypesOfData.items_description
            Container::class -> Strings.TypesOfData.containers_description
            Supplier::class -> Strings.TypesOfData.suppliers_description
            Project::class -> Strings.TypesOfData.projects_description
            else -> null
        }
    }