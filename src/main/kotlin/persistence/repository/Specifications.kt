package persistence.repository

import com.akhris.domain.core.repository.ISpecification
import domain.entities.fieldsmappers.EntityFieldID
import persistence.datasources.SliceValue

sealed class Specification : ISpecification {
    object QueryAll : Specification()
    data class Search(val searchString: String = "") : Specification()
    data class Paginated(val pageNumber: Long, val itemsPerPage: Long) : Specification()
    data class Filtered(val filters: List<FilterSpec> = listOf()) : Specification()
    data class Sorted(val spec: SortingSpec) : Specification()
    data class CombinedSpecification(val specs: List<Specification>) : Specification()
}

sealed class FilterSpec(

) {
    abstract val fieldID: EntityFieldID

    data class Values(
        val filteredValues: List<SliceValue<Any>>,
        override val fieldID: EntityFieldID
    ) : FilterSpec()

    data class Range<T>(
        val fromValue: T?,
        val toValue: T?,
        override val fieldID: EntityFieldID
    ) : FilterSpec()
}

data class SortingSpec(
    val fieldID: EntityFieldID,
    val isAscending: Boolean = true
)

data class GroupingSpec(
    val fieldID: EntityFieldID
)