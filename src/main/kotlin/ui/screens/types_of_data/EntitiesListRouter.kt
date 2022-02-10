package ui.screens.types_of_data

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import domain.entities.usecase_factories.IGetListUseCaseFactory
import ui.screens.types_of_data.data_types_list.DataTypesListComponent
import ui.screens.types_of_data.types_selector.ITypesSelector

class EntitiesListRouter(
    componentContext: ComponentContext,
    private val getListUseCaseFactory: IGetListUseCaseFactory
) {

    private val router =
        componentContext.router<Config, ITypesOfData.ListChild>(
            initialConfiguration = Config.EntitiesList(ITypesSelector.Type.getDefaultHomeType()),
            key = "list_router",
            childFactory = ::createChild
        )

    val state: Value<RouterState<Config, ITypesOfData.ListChild>> = router.state


    fun showEntities(type: ITypesSelector.Type) {
        router.navigate { stack ->
            stack
                .dropLastWhile { it is Config.EntitiesList }
                .plus(Config.EntitiesList(type))
        }
    }

    private fun createChild(config: Config, componentContext: ComponentContext): ITypesOfData.ListChild =
        when (config) {
            is Config.EntitiesList -> ITypesOfData.ListChild.List(
                DataTypesListComponent(
                    type = config.type,
                    getListUseCaseFactory = getListUseCaseFactory,
                    componentContext = componentContext
                )
            )
            is Config.None -> ITypesOfData.ListChild.None
        }


    sealed class Config : Parcelable {
        @Parcelize
        class EntitiesList(val type: ITypesSelector.Type) : Config()

        @Parcelize
        object None : Config()
    }

}