package ui.screens.entities_screen

import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.utils.log
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.activeChild
import com.arkivanov.decompose.router.replaceCurrent
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
import persistence.columnMappers.ColumnMappersFactory
import persistence.datasources.EntitiesList
import persistence.repository.GroupingSpec
import persistence.repository.Specification
import strings.StringsIDs
import ui.screens.entities_screen.entities_filter.EntitiesFilterComponent
import ui.screens.entities_screen.entities_filter.toSpec
import ui.screens.entities_screen.entities_grouping.EntitiesGroupingComponent
import ui.screens.entities_screen.entities_list.EntitiesListComponent
import ui.screens.entities_screen.entities_search.EntitiesSearchComponent
import ui.screens.entities_screen.entities_selector.EntitiesSelectorComponent
import ui.screens.entities_screen.entities_view_settings.EntitiesViewSettingsComponent
import kotlin.reflect.KClass

class EntitiesScreenComponent constructor(
    componentContext: ComponentContext,
    private val entityClasses: List<KClass<out IEntity<*>>>,
    private val fieldsMapperFactory: FieldsMapperFactory,
    private val columnMappersFactory: ColumnMappersFactory,
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
            initialConfiguration = EntitiesFilterConfig.EntitiesFilter(entities = EntitiesList.empty()),
            key = "filter_router",
            childFactory = ::createFilterChild
        )

    private val groupingRouter =
        router(
            initialConfiguration = EntitiesGroupingConfig.EntitiesGrouping(),
            key = "grouping_router",
            childFactory = ::createGroupingChild
        )


    private val viewSettingsRouter =
        router(
            initialConfiguration = EntitiesViewSettingsConfig.ViewSettings,
            key = "view_settings_router",
            childFactory = ::createViewSettingsChild
        )

    private val searchRouter =
        router(
            initialConfiguration = EntitiesSearchConfig.EntitiesSearch,
            key = "search_router",
            childFactory = ::createSearchChild
        )


    override val listRouterState: Value<RouterState<*, IEntitiesScreen.ListChild>> = listRouter.state

    override val selectorRouterState: Value<RouterState<*, IEntitiesScreen.EntitiesSelectorChild>> =
        sidePanelRouter.state

    override val filterRouterState: Value<RouterState<*, IEntitiesScreen.EntitiesFilterChild>> =
        filterRouter.state


    override val viewSettingsRouterState: Value<RouterState<*, IEntitiesScreen.ViewSettingsChild>> =
        viewSettingsRouter.state

    override val searchRouterState: Value<RouterState<*, IEntitiesScreen.EntitiesSearchChild>> = searchRouter.state

    override val groupingRouterState: Value<RouterState<*, IEntitiesScreen.EntitiesGroupingChild>> =
        groupingRouter.state

    private fun createListChild(
        entitiesListConfig: EntitiesListConfig,
        componentContext: ComponentContext
    ): IEntitiesScreen.ListChild {

        return when (entitiesListConfig) {
            is EntitiesListConfig.EntitiesList -> {
                log("creating new list component in $this")
                IEntitiesScreen.ListChild.List(
                    component = EntitiesListComponent(
                        componentContext = componentContext,
                        fSpec = entitiesListConfig.filterSpecification,
                        sSpec = entitiesListConfig.searchSpecification,
                        gSpec = entitiesListConfig.groupSpecification,
                        getEntities = entitiesListConfig.entityClass?.let { getListUseCaseFactory.getListUseCase(it) },
                        updateEntity = entitiesListConfig.entityClass?.let { updateUseCaseFactory.getUpdateUseCase(it) },
                        removeEntity = entitiesListConfig.entityClass?.let { removeUseCaseFactory.getRemoveUseCase(it) },
                        insertEntity = entitiesListConfig.entityClass?.let { insertUseCaseFactory.getInsertUseCase(it) },
                        onEntitiesLoaded = { entities ->
                            val prevClass =
                                (filterRouter.activeChild.configuration as? EntitiesFilterConfig.EntitiesFilter)?.entityClass
                            val currentClass = entitiesListConfig.entityClass

                            if (prevClass != currentClass) {
                                log("navigating to new filter router. prevClass: $prevClass currentClass: $currentClass")
                                filterRouter.replaceCurrent(
                                    EntitiesFilterConfig.EntitiesFilter(
                                        entities = entities,
                                        entityClass = currentClass
                                    )
                                )
                                groupingRouter.replaceCurrent(
                                    EntitiesGroupingConfig.EntitiesGrouping(
                                        entityClass = currentClass
                                    )
                                )
                            }
                        },
                        onItemsCountLoaded = { c ->
                            _state.reduce {
                                it.copy(itemsCount = c)
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
                        listRouter.replaceCurrent(EntitiesListConfig.EntitiesList(entityClass = newEntity))
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
                    entityClass = entitiesFilterConfig.entityClass,
                    getEntities = entitiesFilterConfig.entityClass?.let { getListUseCaseFactory.getListUseCase(it) },
                    fieldsMapperFactory = fieldsMapperFactory,
                    columnMapperFactory = columnMappersFactory,
                    onFiltersChange = { newFilters ->
                        val currentClass = listRouter.activeChild.configuration.entityClass
                        listRouter.replaceCurrent(
                            listRouter.activeChild.configuration.copy(
                                filterSpecification = newFilters.toSpec(
                                    currentClass
                                )
                            )
                        )
                    }
                )
            )
        }
    }

    private fun createGroupingChild(
        groupConfig: EntitiesGroupingConfig,
        componentContext: ComponentContext
    ): IEntitiesScreen.EntitiesGroupingChild {
        return when (groupConfig) {
            is EntitiesGroupingConfig.EntitiesGrouping -> IEntitiesScreen.EntitiesGroupingChild.EntitiesGrouping(
                component = EntitiesGroupingComponent(
                    componentContext = componentContext,
                    entityClass = groupConfig.entityClass,
                    fieldsMapperFactory = fieldsMapperFactory,
                    onGroupingChange = { newGroupBy ->
                        listRouter.replaceCurrent(
                            listRouter.activeChild.configuration.copy(
                                groupSpecification = newGroupBy?.let { Specification.Grouped(GroupingSpec(it)) }
                            )
                        )
                    }
                )
            )
        }
    }

    private fun createSearchChild(
        searchConfig: EntitiesSearchConfig,
        componentContext: ComponentContext
    ): IEntitiesScreen.EntitiesSearchChild {
        return when (searchConfig) {
            EntitiesSearchConfig.EntitiesSearch -> IEntitiesScreen.EntitiesSearchChild.EntitiesSearch(
                component = EntitiesSearchComponent(
                    componentContext = componentContext,
                    onSearchChange = { newSearchString ->
                        listRouter.replaceCurrent(
                            listRouter.activeChild.configuration.copy(
                                searchSpecification = Specification.Search(
                                    newSearchString
                                )
                            )
                        )
                    }
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
        log("initializing EntitiesScreenComponent: $this")
        entityClasses.firstOrNull()?.let {
            updateTitleAndDescription(it)
        }
    }


    sealed class EntitiesListConfig : Parcelable {
        @Parcelize
        data class EntitiesList(
            val entityClass: KClass<out IEntity<*>>?,
            val filterSpecification: Specification.Filtered = Specification.Filtered(),
            val groupSpecification: Specification.Grouped? = null,
            val searchSpecification: Specification.Search = Specification.Search()
        ) : EntitiesListConfig()
    }

    sealed class EntitiesSelectorConfig : Parcelable {
        @Parcelize
        object EntitiesSelector : EntitiesSelectorConfig()
    }

    sealed class EntitiesFilterConfig : Parcelable {
        @Parcelize
        data class EntitiesFilter(
            val entityClass: KClass<out IEntity<*>>? = null,
            val entities: EntitiesList<out IEntity<*>>
        ) : EntitiesFilterConfig()
    }

    sealed class EntitiesGroupingConfig : Parcelable {
        @Parcelize
        data class EntitiesGrouping(
            val entityClass: KClass<out IEntity<*>>? = null,
        ) : EntitiesGroupingConfig()
    }

    sealed class EntitiesViewSettingsConfig : Parcelable {
        @Parcelize
        object ViewSettings : EntitiesViewSettingsConfig()
    }

    sealed class EntitiesSearchConfig : Parcelable {
        @Parcelize
        object EntitiesSearch : EntitiesSearchConfig()
    }
}

val KClass<out IEntity<*>>.title: StringsIDs?
    get() {
        return when (this) {
            ItemIncome::class -> StringsIDs.itemIncome_title
            ItemOutcome::class -> StringsIDs.itemOutcome_title
            WarehouseItem::class -> StringsIDs.warehouseItem_title
            ObjectType::class -> StringsIDs.types_title
            Parameter::class -> StringsIDs.parameters_title
            domain.entities.Unit::class -> StringsIDs.units_title
            Item::class -> StringsIDs.items_title
            Container::class -> StringsIDs.containers_title
            Supplier::class -> StringsIDs.suppliers_title
            Project::class -> StringsIDs.projects_title
            Invoice::class -> StringsIDs.invoice_title
            else -> null
        }
    }

val KClass<out IEntity<*>>.description: StringsIDs?
    get() {
        return when (this) {
            ItemIncome::class -> StringsIDs.itemIncome_description
            ItemOutcome::class -> StringsIDs.itemOutcome_description
            WarehouseItem::class -> StringsIDs.warehouseItem_description
            ObjectType::class -> StringsIDs.types_description
            Parameter::class -> StringsIDs.parameters_description
            domain.entities.Unit::class -> StringsIDs.units_description
            Item::class -> StringsIDs.items_description
            Container::class -> StringsIDs.containers_description
            Supplier::class -> StringsIDs.suppliers_description
            Project::class -> StringsIDs.projects_description
            Invoice::class -> StringsIDs.invoice_description
            else -> null
        }
    }