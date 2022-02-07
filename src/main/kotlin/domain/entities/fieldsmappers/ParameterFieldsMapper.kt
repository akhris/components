package domain.entities.fieldsmappers

import com.akhris.domain.core.entities.IEntity
import domain.entities.Parameter
import domain.entities.Unit

class ParameterFieldsMapper : IFieldsMapper {
    override fun mapFields(entity: Any): List<EntityField> {
        return when (entity) {
            is Parameter -> mapParameterFields(entity)
            else -> throw IllegalArgumentException("$this cannot map $entity fields, use another mapper")
        }
    }

    override fun <T : IEntity<*>> mapIntoEntity(entity: T, field: EntityField): T {
        return when (entity) {
            is Parameter -> mapIntoParameter(entity, field) as T
            else -> throw IllegalArgumentException("$this cannot map $entity fields, use another mapper")
        }
    }

    private fun mapParameterFields(param: Parameter): List<EntityField> {
        return listOfNotNull(
            EntityField.StringField(
                tag = "parameter_field_name",
                name = "name",
                description = "parameter name",
                value = param.name
            ), EntityField.StringField(
                tag = "parameter_field_description",
                name = "description",
                description = "parameter description",
                value = param.description
            ),

            EntityField.EntityLink(
                tag = "parameter_unit",
                name = param.unit?.unit ?: "unit",
                description = "parameter unit",
                entity = param.unit
            ),
            EntityField.CaptionField(
                tag = "parameter_field_id",
                name = "UUID",
                description = "unique id of the parameter",
                caption = param.id
            )
        )
    }

    private fun mapIntoParameter(param: Parameter, field: EntityField): Parameter {
        return when (field.tag) {
            "parameter_field_name" -> param.copy(name = (field as EntityField.StringField).value)
            "parameter_field_description" -> param.copy(description = (field as EntityField.StringField).value)
            "parameter_unit" -> {
                val unit = (field as? EntityField.EntityLink)?.entity as? Unit
                param.copy(unit = unit)
            }
            else -> throw IllegalArgumentException("field with tag: ${field.tag} was not found in entity: $param")
        }
    }
}