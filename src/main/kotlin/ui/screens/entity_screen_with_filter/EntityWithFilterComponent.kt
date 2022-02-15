package ui.screens.entity_screen_with_filter

import com.akhris.domain.core.entities.IEntity
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import domain.entities.fieldsmappers.FieldsMapperFactory
import domain.entities.usecase_factories.IGetListUseCaseFactory
import ui.screens.entity_screen_with_filter.entities_filter.EntitiesFilterComponent
import ui.screens.entity_screen_with_filter.entities_filter.IEntitiesFilter
import ui.screens.entity_screen_with_filter.entities_list.EntitiesListComponent
import ui.screens.entity_screen_with_filter.entities_list.IEntitiesList
import kotlin.reflect.KClass

class EntityWithFilterComponent<T : IEntity<*>>(
    componentContext: ComponentContext,
    private val entityClass: KClass<T>,
    private val fieldsMapperFactory: FieldsMapperFactory,
    private val getListUseCaseFactory: IGetListUseCaseFactory
) : IEntityWithFilter,
    ComponentContext by componentContext {

    private val _filterModel = MutableValue(IEntitiesFilter.Model())
    private val _listModel = MutableValue(IEntitiesList.Model<T>())

    private val listRouter =
        router(
            initialConfiguration = EntitiesListConfig.EntitiesList,
            key = "list_router",
            childFactory = ::createListChild
        )

    private val filterRouter =
        router(
            initialConfiguration = EntitiesFilterConfig.Filter,
            key = "filter_router",
            handleBackButton = false, // Pop the back stack on back button press
            childFactory = ::createFilterChild
        )


    override val listRouterState: Value<RouterState<*, IEntityWithFilter.ListChild>> = listRouter.state
    override val filterRouterState: Value<RouterState<*, IEntityWithFilter.FilterChild>> = filterRouter.state


    private fun createFilterChild(
        entitiesFilterConfig: EntitiesFilterConfig,
        componentContext: ComponentContext
    ): IEntityWithFilter.FilterChild {
        return when (entitiesFilterConfig) {
            is EntitiesFilterConfig.Filter -> IEntityWithFilter.FilterChild.Filter(
                EntitiesFilterComponent(
                    componentContext = componentContext,
                    listModel = _listModel,
                    onFilterModelChanged = { changedModel ->
                        _filterModel.reduce { changedModel }
                    },
                    mapper = fieldsMapperFactory.getFieldsMapper(entityClass)
                )
            )
        }
    }

    private fun createListChild(
        entitiesListConfig: EntitiesListConfig,
        componentContext: ComponentContext
    ): IEntityWithFilter.ListChild {

        return when (entitiesListConfig) {
            is EntitiesListConfig.EntitiesList -> IEntityWithFilter.ListChild.List(
                EntitiesListComponent<T>(
                    componentContext = componentContext,
                    filterModel = _filterModel,
                    onListModelChanged = { changedModel ->
                        _listModel.reduce { changedModel }
                    },
                    getEntities = getListUseCaseFactory.getListUseCase(entityClass)
                )
            )
        }
    }

    sealed class EntitiesListConfig : Parcelable {
        @Parcelize
        object EntitiesList : EntitiesListConfig()
    }

    sealed class EntitiesFilterConfig : Parcelable {
        @Parcelize
        object Filter : EntitiesFilterConfig()
    }
}