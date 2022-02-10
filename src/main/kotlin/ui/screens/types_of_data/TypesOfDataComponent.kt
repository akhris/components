package ui.screens.types_of_data

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce
import domain.entities.usecase_factories.IGetListUseCaseFactory
import ui.screens.types_of_data.types_selector.ITypesSelector
import ui.screens.types_of_data.types_selector.TypesSelectorComponent

class TypesOfDataComponent(
    componentContext: ComponentContext,
    getListUseCaseFactory: IGetListUseCaseFactory
) :
    ITypesOfData, ComponentContext by componentContext {

    private val _selectedItem = MutableValue<ITypesSelector.Type>(ITypesSelector.Type.None)

    override val selectedItem: Value<ITypesSelector.Type> = _selectedItem

    private val listRouter =
        EntitiesListRouter(componentContext = componentContext, getListUseCaseFactory = getListUseCaseFactory)

    private val filterRouter =
        router(
            initialConfiguration = FilterRouter.Config.Filter(null),
            key = "filter_router",
            handleBackButton = false, // Pop the back stack on back button press
            childFactory = ::createChild
        )


    private fun createChild(
        config: FilterRouter.Config,
        componentContext: ComponentContext
    ): ITypesOfData.FilterChild {
        return when (config) {
            is FilterRouter.Config.Filter -> ITypesOfData.FilterChild.Filter(
                TypesSelectorComponent(
                    selectedType = _selectedItem.value,
                    onTypeSelected = { selection ->
                        listRouter.showEntities(selection)
                        _selectedItem.reduce {
                            selection
                        }
                    }
                )
            )
            is FilterRouter.Config.None -> ITypesOfData.FilterChild.None
        }
    }


    override val listRouterState: Value<RouterState<*, ITypesOfData.ListChild>> = listRouter.state

    override val filterRouterState: Value<RouterState<*, ITypesOfData.FilterChild>> = filterRouter.state

}