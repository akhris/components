package ui.screens.entities_screen.entities_selector

import com.akhris.domain.core.entities.IEntity
import com.arkivanov.decompose.value.Value
import ui.screens.entities_screen.entities_view_settings.ItemRepresentationType
import kotlin.reflect.KClass

/**
 * Interface for Sidepanel component of Entity-with-sidepanel block.
 *
 * Component contains of entities selector and grouping/filtering block.
 *
 */
interface IEntitiesSelector {
    //current state of the screen
    val state: Value<Model>

    fun selectEntity(entityClass: KClass<out IEntity<*>>)

    //filter screen model:
    data class Model(
        val entitiesSelector: EntitiesSelector? = null,

        //cards / grid
        val itemRepresentationType: ItemRepresentationType = ItemRepresentationType.Card,

        // filter settings for each entity field IDs from the data of main list screen
//        val filters: List<FilterSettings> = listOf()
    )


    data class EntitiesSelector(val selection: KClass<out IEntity<*>>, val items: List<KClass<out IEntity<*>>>)
}