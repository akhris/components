package ui.screens.entities_screen.entities_filter

import com.arkivanov.decompose.value.Value
import domain.entities.fieldsmappers.EntityFieldID
import ui.screens.entities_screen.entities_filter.IEntitiesFilter.Filter

/**
 * Interface for Filter component of Entity-with-filter block.
 *
 * It's state contains of current Filter settings that is implemented as List of [Filter] objects.
 */
interface IEntitiesFilter {
    //current state of the screen
    val state: Value<Model>

    //methods to change the state:
    // add / update filter setting
    fun setFilter(filter: Filter)

    // remove filter setting
    fun removeFilter(filter: Filter)

    //filter screen model:
    data class Model(
        // filter settings for each entity field IDs from the data of main list screen
        val filters: List<Filter> = listOf()
    )

    /**
     * Filter for single FieldID.
     */
    sealed class Filter {
        abstract val fieldID: EntityFieldID
        abstract val isActive: Boolean

        /**
         * Filter type that has a list of filtering values
         */
        data class Values(
            override val fieldID: EntityFieldID,
            val fieldsList: List<FilteringValue> = listOf(),
            override val isActive: Boolean = false
        ) : Filter()

        /**
         * Filter type that has a range of filtering values
         */
        data class Range(
            override val fieldID: EntityFieldID,
            val from: Any? = null,
            val to: Any? = null,
            override val isActive: Boolean = false
        ) : Filter()
    }

    data class FilteringValue(val value: Any, val isFiltered: Boolean = false)
}

