package domain.entities.fieldsmappers

import domain.entities.Parameter
import domain.entities.Unit

class ParameterFieldsMapper : IFieldsMapper<Parameter> {

    override fun getEntityIDs(): List<EntityFieldID> {
        return listOf(
            EntityFieldID(tag = EntityFieldID.tag_name, name = "name"),
            EntityFieldID(tag = EntityFieldID.tag_description, name = "description"),
            EntityFieldID(tag = tag_unit, name = "unit")
        )
    }


    override fun getFieldByID(entity: Parameter, fieldID: EntityFieldID): EntityField {
        return when (fieldID.tag) {
            tag_unit -> EntityField.EntityLink.EntityLinkSimple(
                fieldID = fieldID,
                description = "parameter's unit",
                entity = entity.unit,
                entityClass = Unit::class
            )
            EntityFieldID.tag_name -> EntityField.StringField(
                fieldID = fieldID,
                value = entity.name,
                description = "parameter's name"
            )
            EntityFieldID.tag_description ->
                EntityField.StringField(
                    fieldID = fieldID,
                    value = entity.description,
                    description = "parameter's description"
                )
            else -> throw IllegalArgumentException("field with tag: ${fieldID.tag} was not found in entity: $entity")
        }
    }

    override fun mapIntoEntity(entity: Parameter, field: EntityField): Parameter {
        return when (field) {
            is EntityField.EntityLink -> {
                val unit = (field as? EntityField.EntityLink)?.entity as? Unit
                entity.copy(unit = unit)
            }
            is EntityField.StringField -> {
                when (field.fieldID.tag) {
                    "tag_name" -> entity.copy(name = field.value)
                    "tag_description" -> entity.copy(description = field.value)
                    else -> throw IllegalArgumentException("unknown tag ${field.fieldID.tag} for entity: $entity")
                }
            }
            else -> throw IllegalArgumentException("field with column: ${field.fieldID} was not found in entity: $entity")
        }
    }

    companion object {
        const val tag_unit = "tag_unit"
    }
}
