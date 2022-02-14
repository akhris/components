package domain.entities.fieldsmappers

import domain.entities.Parameter
import domain.entities.Value

class ValueFieldsMapper : BaseFieldsMapper<Value>() {

    private val tag_value = "tag_value"
    private val tag_factor = "tag_factor"
    private val tag_parameter = "tag_parameter"

    override fun getEntityIDs(entity: Value): List<EntityFieldID> {
        return listOf(
            EntityFieldID.StringID(tag = tag_value, name = "value"),
            EntityFieldID.FloatID(tag = tag_factor, name = "factor"),
            EntityFieldID.EntityID(tag = tag_parameter, name = "parameter")
        )
    }

    override fun getFieldParamsByFieldID(entity: Value, fieldID: EntityFieldID): DescriptiveFieldValue {
        return when (fieldID) {
            is EntityFieldID.StringID -> DescriptiveFieldValue(value = entity.value, description = "value")
            is EntityFieldID.FloatID -> DescriptiveFieldValue(
                value = entity.factor,
                description = "factor of the value"
            )
            is EntityFieldID.EntityID -> DescriptiveFieldValue(
                value = entity.parameter,
                description = "parameter of the value"
            )
            else -> throw IllegalArgumentException("field with id: $fieldID was not found in entity: $entity")
        }
    }

    override fun mapIntoEntity(entity: Value, field: EntityField): Value {
        return when (val column = field.fieldID) {
            is EntityFieldID.StringID -> entity.copy(value = (field as EntityField.StringField).value)
            is EntityFieldID.FloatID -> entity.copy(factor = (field as EntityField.FloatField).value)
            is EntityFieldID.EntityID -> entity.copy(parameter = (field as EntityField.EntityLink).entity as Parameter)
            else -> throw IllegalArgumentException("field with column: $column was not found in entity: $entity")
        }
    }


}