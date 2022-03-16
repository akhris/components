package ui.screens.entities_screen.entities_list

import com.akhris.domain.core.entities.IEntity
import com.arkivanov.decompose.value.Value
import ui.screens.entities_screen.entities_view_settings.ItemRepresentationType

interface IEntitiesList<T : IEntity<*>> {
    //current state of the screen
    val state: Value<Model<T>>

    //methods to change the state:
    //set entities list:
    fun setEntitiesList(entities: List<T>)

    //    fun setTotalPages(totalPages: Int)
    fun setCurrentPage(currentPage: Long)
    fun setItemsPerPage(itemsPerPage: Long)

    //entities list model
    data class Model<T : IEntity<*>>(
        val entities: List<T> = listOf(),
        val pagingParameters: PagingParameters? = null,
        val itemRepresentationType: ItemRepresentationType = ItemRepresentationType.Card
    )

    data class PagingParameters(
        val currentPage: Long = 1L,
        val totalItems: Long = 0L,
        val itemsPerPage: Long = 50L
    ) {
        val totalPages = (totalItems / itemsPerPage) + 1
    }
}