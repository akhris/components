package ui.screens.entities_screen

import com.akhris.domain.core.entities.IEntity
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
import domain.entities.usecase_factories.IInsertUseCaseFactory
import domain.entities.usecase_factories.IRemoveUseCaseFactory
import domain.entities.usecase_factories.IUpdateUseCaseFactory
import strings.StringsIDs
import ui.screens.entities_screen.entities_filter.EntitiesFilterComponent
import ui.screens.entities_screen.entities_grouping.EntitiesGroupingComponent
import ui.screens.entities_screen.entities_list.EntitiesListComponent
import ui.screens.entities_screen.entities_selector.EntitiesSelectorComponent
import ui.screens.entities_screen.entities_view_settings.EntitiesViewSettingsComponent
import kotlin.reflect.KClass

class EntitiesScreenComponent(
    componentContext: ComponentContext,
    private val entityClasses: List<KClass<out IEntity<*>>>,
    private val fieldsMapperFactory: FieldsMapperFactory,
    private val getListUseCaseFactory: IGetListUseCaseFactory,
    private val updateUseCaseFactory: IUpdateUseCaseFactory,
    private val insertUseCaseFactory: IInsertUseCaseFactory,
    private val removeUseCaseFactory: IRemoveUseCaseFactory
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

    private val groupingRouter =
        router(
            initialConfiguration = EntitiesGroupingConfig.EntitiesGrouping(listOf()),
            key = "grouping_router",
            childFactory = ::createGroupingChild
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

    override val groupingRouterState: Value<RouterState<*, IEntitiesScreen.EntitiesGroupingChild>> =
        groupingRouter.state

    override val viewSettingsRouterState: Value<RouterState<*, IEntitiesScreen.ViewSettingsChild>> =
        viewSettingsRouter.state


    private fun createListChild(
        entitiesListConfig: EntitiesListConfig,
        componentContext: ComponentContext
    ): IEntitiesScreen.ListChild {

        return when (entitiesListConfig) {
            is EntitiesListConfig.EntitiesList -> {
                IEntitiesScreen.ListChild.List(
                    component = EntitiesListComponent(
                        componentContext = componentContext,
                        getEntities = entitiesListConfig.entityClass?.let { getListUseCaseFactory.getListUseCase(it) },
                        updateEntity = entitiesListConfig.entityClass?.let { updateUseCaseFactory.getUpdateUseCase(it) },
                        insertEntity = entitiesListConfig.entityClass?.let { insertUseCaseFactory.getInsertUseCase(it) },
                        removeEntity = entitiesListConfig.entityClass?.let { removeUseCaseFactory.getRemoveUseCase(it) },
                        onEntitiesLoaded = { entities ->
                            filterRouter.navigate { stack ->
                                stack.dropLastWhile { it is EntitiesFilterConfig.EntitiesFilter }
                                    .plus(EntitiesFilterConfig.EntitiesFilter(entities = entities))
                            }
                            groupingRouter.navigate { stack ->
                                stack.dropLastWhile { it is EntitiesGroupingConfig.EntitiesGrouping }
                                    .plus(EntitiesGroupingConfig.EntitiesGrouping(entities = entities))
                            }
                        }
                    )
                )
            }
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
    private fun createGroupingChild(
        entitiesFilterConfig: EntitiesGroupingConfig,
        componentContext: ComponentContext
    ): IEntitiesScreen.EntitiesGroupingChild {
        return when (entitiesFilterConfig) {
            is EntitiesGroupingConfig.EntitiesGrouping -> IEntitiesScreen.EntitiesGroupingChild.EntitiesGrouping(
                EntitiesGroupingComponent(
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
                    onTypeChanged = { newType ->
                        _state.reduce {
                            it.copy(itemRepresentationType = newType)
                        }
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

    sealed class EntitiesGroupingConfig : Parcelable {
        @Parcelize
        data class EntitiesGrouping(val entities: List<IEntity<*>>) : EntitiesGroupingConfig()
    }

    sealed class EntitiesViewSettingsConfig : Parcelable {
        @Parcelize
        object ViewSettings : EntitiesViewSettingsConfig()
    }


}

val KClass<out IEntity<*>>.title: StringsIDs?
    get() {
        return when (this) {
            ItemIncome::class->StringsIDs.itemIncome_title
            ItemOutcome::class->StringsIDs.itemOutcome_title
            WarehouseItem::class->StringsIDs.warehouseItem_title
            ObjectType::class -> StringsIDs.types_title
            Parameter::class -> StringsIDs.parameters_title
            domain.entities.Unit::class -> StringsIDs.units_title
            Item::class -> StringsIDs.items_title
            Container::class -> StringsIDs.containers_title
            Supplier::class -> StringsIDs.suppliers_title
            Project::class -> StringsIDs.projects_title
            else -> null
        }
    }

val KClass<out IEntity<*>>.description: StringsIDs?
    get() {
        return when (this) {
            ItemIncome::class->StringsIDs.itemIncome_description
            ItemOutcome::class->StringsIDs.itemOutcome_description
            WarehouseItem::class->StringsIDs.warehouseItem_description
            ObjectType::class -> StringsIDs.types_description
            Parameter::class -> StringsIDs.parameters_description
            domain.entities.Unit::class -> StringsIDs.units_description
            Item::class -> StringsIDs.items_description
            Container::class -> StringsIDs.containers_description
            Supplier::class -> StringsIDs.suppliers_description
            Project::class -> StringsIDs.projects_description
            else -> null
        }
    }