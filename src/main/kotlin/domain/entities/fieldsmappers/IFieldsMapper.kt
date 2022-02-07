package domain.entities.fieldsmappers

import com.akhris.domain.core.entities.IEntity

interface IFieldsMapper {
    fun mapFields(entity: Any): List<EntityField>
    fun <T: IEntity<*>>mapIntoEntity(entity: T, field: EntityField): T
}