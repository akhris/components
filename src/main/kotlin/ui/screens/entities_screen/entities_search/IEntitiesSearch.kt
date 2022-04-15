package ui.screens.entities_screen.entities_search

import com.arkivanov.decompose.value.Value

interface IEntitiesSearch {

    val state: Value<Model>

    fun setSearchString(search: String)

    data class Model(val searchString: String = "")
}