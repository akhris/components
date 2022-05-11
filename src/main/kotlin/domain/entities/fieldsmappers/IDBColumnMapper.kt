package domain.entities.fieldsmappers

import com.akhris.domain.core.entities.IEntity
import org.jetbrains.exposed.sql.Column

interface IDBColumnMapper<T : IEntity<*>> {
    fun getColumn(fieldID: EntityFieldID): Result?
    data class Result(val column: Column<Any?>)
}


//todo return not just a column but a result class that may contain also foreign table info
// to get, for example, name of the parent container
