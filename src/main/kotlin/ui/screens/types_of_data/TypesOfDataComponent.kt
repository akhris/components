package ui.screens.types_of_data

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import domain.entities.usecase_factories.IGetListUseCaseFactory
import domain.entities.usecase_factories.IUpdateUseCaseFactory
import ui.screens.types_of_data.data_types_list.DataTypesListComponent
import ui.screens.types_of_data.types_selector.ITypesSelector
import ui.screens.types_of_data.types_selector.ItemRepresentationType
import ui.screens.types_of_data.types_selector.TypesSelectorComponent

class TypesOfDataComponent(
    componentContext: ComponentContext,
    private val updateUseCaseFactory: IUpdateUseCaseFactory,
    private val getListUseCaseFactory: IGetListUseCaseFactory
) :
    ITypesOfData, ComponentContext by componentContext {

    private val _selectedItem = MutableValue<ITypesSelector.Type>(ITypesSelector.Type.None)

    override val selectedItem: Value<ITypesSelector.Type> = _selectedItem

    private val _representationType =
        MutableValue<ItemRepresentationType>(ItemRepresentationType.Card)

    private val listRouter =
        router<EntitiesListConfig, ITypesOfData.ListChild>(
            initialConfiguration = EntitiesListConfig.EntitiesList(ITypesSelector.Type.getDefaultHomeType()),
            key = "list_router",
            childFactory = ::createListChild
        )

    private val filterRouter =
        router(
            initialConfiguration = FilterConfig.Filter(null),
            key = "filter_router",
            handleBackButton = false, // Pop the back stack on back button press
            childFactory = ::createFilterChild
        )


    private fun createFilterChild(
        filterConfig: FilterConfig,
        componentContext: ComponentContext
    ): ITypesOfData.FilterChild {
        return when (filterConfig) {
            is FilterConfig.Filter -> ITypesOfData.FilterChild.Filter(
                TypesSelectorComponent(
                    componentContext = componentContext,
                    selectedType = _selectedItem.value,
                    onTypeSelected = { selection ->
                        listRouter.navigate { stack ->
                            stack
                                .dropLastWhile { it is EntitiesListConfig.EntitiesList }
                                .plus(EntitiesListConfig.EntitiesList(selection))
                        }
                        _selectedItem.reduce {
                            selection
                        }
                    },
                    onRepresentationTypeChanged = { newType ->
                        _representationType.reduce { newType }
                    }
                )
            )
        }
    }

    private fun createListChild(
        entitiesListConfig: EntitiesListConfig,
        componentContext: ComponentContext
    ): ITypesOfData.ListChild {
        return when (entitiesListConfig) {
            is EntitiesListConfig.EntitiesList -> ITypesOfData.ListChild.List(
                DataTypesListComponent(
                    type = entitiesListConfig.type,
                    componentContext = componentContext,
                    representationType = _representationType,
                    updateUseCaseFactory = updateUseCaseFactory,
                    getListUseCaseFactory = getListUseCaseFactory
                )
            )
        }
    }

    sealed class EntitiesListConfig : Parcelable {
        @Parcelize
        class EntitiesList(val type: ITypesSelector.Type) : EntitiesListConfig()
    }

    sealed class FilterConfig : Parcelable {
        @Parcelize
        data class Filter(val selectedType: ITypesSelector.Type?) : FilterConfig()
    }


    override val listRouterState: Value<RouterState<*, ITypesOfData.ListChild>> = listRouter.state

    override val filterRouterState: Value<RouterState<*, ITypesOfData.FilterChild>> = filterRouter.state

}