package domain.entities.fieldsmappers

import com.akhris.domain.core.entities.IEntity

/**
 * Interface to get fields of any [IEntity] as some unified classes
 */
interface IFieldsMapper<T : IEntity<*>> {
    /**
     * Get all entity field IDs of [IEntity].
     */
    fun getEntityIDs(): List<EntityFieldID>

    /**
     * Read actual field value of [entity] for given [fieldID] or null
     */
    fun getFieldByID(entity: T, fieldID: EntityFieldID): EntityField?

    /**
     * Write new field value of [field] into [entity] and returns updated one
     */
    fun mapIntoEntity(entity: T, field: EntityField): T
}


