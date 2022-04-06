package domain.entities.fieldsmappers

import domain.entities.Unit

class UnitFieldsMapper : BaseFieldsMapper<Unit>() {


    override fun getEntityIDs(entity: Unit): List<EntityFieldID> {
        return listOf(
            EntityFieldID.StringID(Companion.tag_unit, "unit"),
            EntityFieldID.BooleanID(Companion.tag_is_multipliable, "is multipliable")
        )
    }


    override fun getFieldParamsByFieldID(entity: Unit, fieldID: EntityFieldID): DescriptiveFieldValue {
        return when (fieldID) {
            is EntityFieldID.StringID -> DescriptiveFieldValue.CommonField(entity = entity.unit, description = "unit")
            is EntityFieldID.BooleanID -> DescriptiveFieldValue.CommonField(
                entity = entity.isMultipliable,
                description = "can be prefixed with k-, m-, u-, ..."
            )
            else -> throw IllegalArgumentException("field with id: $fieldID was not found in entity: $entity")
        }
    }

    override fun mapIntoEntity(entity: Unit, field: EntityField): Unit {
        return when (val column = field.fieldID) {
            is EntityFieldID.StringID -> entity.copy(unit = (field as EntityField.StringField).value)
            is EntityFieldID.BooleanID -> entity.copy(isMultipliable = (field as EntityField.BooleanField).value)
            else -> throw IllegalArgumentException("field with column: $column was not found in entity: $entity")
        }
    }

    companion object {
        const val tag_unit = "tag_unit"
        const val tag_is_multipliable = "tag_is_multipliable"
    }


}