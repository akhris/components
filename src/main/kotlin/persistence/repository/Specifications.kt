package persistence.repository

import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.repository.ISpecification
import domain.entities.fieldsmappers.EntityField
import domain.entities.fieldsmappers.EntityFieldID
import kotlin.reflect.KClass

sealed class Specification : ISpecification {
    object QueryAll : Specification()
    data class Search(val searchString: String) : Specification()
    data class ByItem(val itemID: String) : Specification()
    data class Paginated(val pageNumber: Long, val itemsPerPage: Long) : Specification()
    data class Filters(val filters: List<FilterSpec> = listOf()) : Specification()
}

data class FilterSpec(
    val entityClass: KClass<out IEntity<*>>,
    val fieldID: EntityFieldID,
    val filteredValues: List<EntityField>
)