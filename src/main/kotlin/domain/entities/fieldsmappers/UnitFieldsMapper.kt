package domain.entities.fieldsmappers

import domain.entities.Unit

class UnitFieldsMapper : BaseFieldsMapper<Unit>() {

    override fun getFields(entity: Unit): Map<EntityFieldColumn, Any?> {
        return mapOf(
            EntityFieldColumn.StringColumn() to entity.unit,
            EntityFieldColumn.BooleanColumn() to entity.isMultipliable
        )
    }

    override fun mapIntoEntity(entity: Unit, field: EntityField): Unit {
        return when (val column = field.fieldColumn) {
            is EntityFieldColumn.StringColumn -> entity.copy(unit = (field as EntityField.StringField).value)
            is EntityFieldColumn.BooleanColumn -> entity.copy(isMultipliable = (field as EntityField.BooleanField).value)
            else -> throw IllegalArgumentException("field with column: $column was not found in entity: $entity")
        }
    }


}