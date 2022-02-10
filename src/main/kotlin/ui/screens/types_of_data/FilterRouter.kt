package ui.screens.types_of_data

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import ui.screens.types_of_data.types_selector.ITypesSelector
import ui.screens.types_of_data.types_selector.TypesSelectorComponent

class FilterRouter(componentContext: ComponentContext, private val onTypeSelected: (ITypesSelector.Type) -> Unit) {

    private val router =
        componentContext.router<Config, ITypesOfData.FilterChild>(
            initialConfiguration = Config.Filter(null),
            key = "filter_router",
            childFactory = ::createChild
        )

    val state: Value<RouterState<Config, ITypesOfData.FilterChild>> = router.state

    private fun createChild(
        config: Config,
        componentContext: ComponentContext
    ): ITypesOfData.FilterChild =
        when (config) {
            is Config.Filter -> ITypesOfData.FilterChild.Filter(
                TypesSelectorComponent(selectedType = config.selectedType, onTypeSelected = onTypeSelected)
            )
            is Config.None -> ITypesOfData.FilterChild.None
        }


    sealed class Config : Parcelable {
        @Parcelize
        data class Filter(val selectedType: ITypesSelector.Type?) : Config()

        @Parcelize
        object None : Config()
    }
}