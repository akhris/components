package domain.entities.fieldsmappers

import domain.entities.Unit

class UnitFieldsMapper : BaseFieldsMapper<Unit>() {

    override fun getFields(entity: Unit): Map<EntityFieldID, Any?> {
        return mapOf(
            EntityFieldID.StringID("tag_unit", "unit") to entity.unit,
            EntityFieldID.BooleanID("tag_is_multipliable", "is multipliable") to entity.isMultipliable
        )
    }

    override fun mapIntoEntity(entity: Unit, field: EntityField): Unit {
        return when (val column = field.fieldID) {
            is EntityFieldID.StringID -> entity.copy(unit = (field as EntityField.StringField).value)
            is EntityFieldID.BooleanID -> entity.copy(isMultipliable = (field as EntityField.BooleanField).value)
            else -> throw IllegalArgumentException("field with column: $column was not found in entity: $entity")
        }
    }


}