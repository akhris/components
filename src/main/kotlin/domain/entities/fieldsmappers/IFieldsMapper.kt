package domain.entities.fieldsmappers

import com.akhris.domain.core.entities.IEntity

interface IFieldsMapper<T : IEntity<*>> {
    //    fun mapFields(entity: Any): List<EntityField>
    fun getEntityColumns(entity: T): List<EntityFieldColumn>
    fun getFieldByColumn(entity: T, fieldColumn: EntityFieldColumn): EntityField?
    fun mapIntoEntity(entity: T, field: EntityField): T
}