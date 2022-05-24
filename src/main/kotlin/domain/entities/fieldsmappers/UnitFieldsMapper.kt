package domain.entities.fieldsmappers

import domain.entities.Unit

class UnitFieldsMapper : IFieldsMapper<Unit> {

    override fun getEntityIDs(): List<EntityFieldID> {
        return listOf(
            EntityFieldID(tag = tag_unit, name = "unit"),
            EntityFieldID(tag = tag_is_multipliable,name =  "is multipliable")
        )
    }

    override fun getFieldByID(entity: Unit, fieldID: EntityFieldID): EntityField {
        return when (fieldID.tag) {
            tag_unit -> EntityField.StringField(
                    fieldID = fieldID,
                    value = entity.unit,
                    description = "unit",
                    isPlaceholder = entity.unit.isEmpty()
                )
            tag_is_multipliable -> EntityField.BooleanField(
                    fieldID = fieldID,
                    value = entity.isMultipliable,
                    description = "can be prefixed with k-, m-, u-, ..."
                )
            else -> {
                throw IllegalArgumentException("field with id: $fieldID was not found in entity: $entity")
            }
        }
    }

    override fun mapIntoEntity(entity: Unit, field: EntityField): Unit {
        return when (field) {
            is EntityField.StringField -> entity.copy(unit = field.value)
            is EntityField.BooleanField -> entity.copy(isMultipliable = field.value)
            else -> throw IllegalArgumentException("field with column: ${field.fieldID} was not found in entity: $entity")
        }
    }

    companion object {
        const val tag_unit = "tag_unit"
        const val tag_is_multipliable = "tag_is_multipliable"
    }


}