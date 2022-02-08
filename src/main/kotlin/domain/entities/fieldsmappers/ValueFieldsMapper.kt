package domain.entities.fieldsmappers

import com.akhris.domain.core.entities.IEntity
import domain.entities.Parameter
import domain.entities.Value

class ValueFieldsMapper : IFieldsMapper {
    override fun mapFields(entity: Any): List<EntityField> {
        return when (entity) {
            is Value -> mapValueFields(entity)
            else -> throw IllegalArgumentException("$this cannot map $entity fields, use another mapper")
        }
    }

    override fun <T : IEntity<*>> mapIntoEntity(entity: T, field: EntityField): T {
        return when (entity) {
            is Value -> mapIntoValue(entity, field) as T
            else -> throw IllegalArgumentException("$this cannot map $entity fields, use another mapper")
        }
    }

    private fun mapValueFields(value: Value): List<EntityField> {
        return listOfNotNull(
            EntityField.StringField(
                tag = "value_field_value",
                name = "value",
                description = "String representing value of parameter",
                value = value.value
            ),
            EntityField.EntityLink(
                tag = "value_field_parameter",
                name = "parameter",
                description = "parameter corresponding to value",
                entity = value.parameter
            ),
            value.factor?.let {
                EntityField.FloatField(
                    tag = "value_field_factor",
                    name = "factor",
                    description = "factor to multiply value",
                    value = value.factor
                )
            },
            EntityField.CaptionField(
                tag = "value_field_id",
                name = "UUID",
                description = "unique id of the value",
                caption = value.id
            )
        )
    }

    private fun mapIntoValue(value: Value, field: EntityField): Value {
        return when (field.tag) {
            "value_field_value" -> value.copy(value = (field as EntityField.StringField).value)
            "value_field_parameter" -> value.copy(parameter = ((field as EntityField.EntityLink).entity as Parameter))
            "value_field_factor" -> value.copy(factor = (field as EntityField.FloatField).value)
            else -> throw IllegalArgumentException("field with tag: ${field.tag} was not found in entity: $value")
        }
    }
}