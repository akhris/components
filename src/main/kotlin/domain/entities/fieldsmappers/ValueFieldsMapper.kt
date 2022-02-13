package domain.entities.fieldsmappers

import domain.entities.Parameter
import domain.entities.Value

class ValueFieldsMapper : BaseFieldsMapper<Value>() {

    override fun getFields(entity: Value): Map<EntityFieldID, Any?> {
        return mapOf(
            EntityFieldID.StringID(tag = "tag_value", name = "value") to entity.value,
            EntityFieldID.FloatID(tag = "tag_factor", name = "factor") to entity.factor,
            EntityFieldID.EntityID(tag = "tag_parameter", name = "parameter") to entity.parameter
        )
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