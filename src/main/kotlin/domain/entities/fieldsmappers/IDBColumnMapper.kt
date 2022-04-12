package domain.entities.fieldsmappers

import com.akhris.domain.core.entities.IEntity

interface IDBColumnMapper<T : IEntity<*>> {
    fun getColumnName(fieldID: EntityFieldID): String?
}

