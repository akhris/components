package ui.screens.entities_screen.entities_search

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce

class EntitiesSearchComponent(
    componentContext: ComponentContext,
    private val onSearchChange: (String) -> Unit
) : IEntitiesSearch,
    ComponentContext by componentContext {

    private val _state = MutableValue(IEntitiesSearch.Model())

    override val state: Value<IEntitiesSearch.Model> = _state

    override fun setSearchString(search: String) {
        _state.reduce {
            it.copy(searchString = search)
        }
        onSearchChange(search)
    }


}