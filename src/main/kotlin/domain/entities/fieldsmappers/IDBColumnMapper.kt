package domain.entities.fieldsmappers

import com.akhris.domain.core.entities.IEntity
import org.jetbrains.exposed.sql.Column

interface IDBColumnMapper<T : IEntity<*>> {
    fun getColumn(fieldID: EntityFieldID): Column<Any>?
}

