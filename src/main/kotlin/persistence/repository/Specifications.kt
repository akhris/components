package persistence.repository

import com.akhris.domain.core.repository.ISpecification

sealed class Specification : ISpecification {
    object QueryAll : Specification()
    data class Search(val searchString: String) : Specification()
}