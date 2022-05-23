package domain.entities.fieldsmappers

import domain.entities.Parameter
import domain.entities.Unit

class ParameterFieldsMapper : IFieldsMapper<Parameter> {

    private val tag_unit = "tag_unit"

    override fun getEntityIDs(): List<EntityFieldID> {
        return listOf(
            EntityFieldID.StringID(tag = EntityFieldID.tag_name, name = "name"),
            EntityFieldID.StringID(tag = EntityFieldID.tag_description, name = "description"),
            EntityFieldID.EntityID(tag = tag_unit, name = "unit")
        )
    }


    override fun getFieldByID(entity: Parameter, fieldID: EntityFieldID): EntityField? {
        return when (fieldID) {
            is EntityFieldID.EntityID -> EntityField.EntityLink.EntityLinkSimple(
                fieldID = fieldID,
                description = "parameter's unit",
                entity = entity.unit,
                entityClass = Unit::class
            )
            is EntityFieldID.StringID -> when (fieldID.tag) {
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
            else -> throw IllegalArgumentException("field with id: $fieldID was not found in entity: $entity")
        }
    }

    override fun mapIntoEntity(entity: Parameter, field: EntityField): Parameter {
        return when (val fieldID = field.fieldID) {
            is EntityFieldID.StringID -> {
                when (fieldID.tag) {
                    "tag_name" -> entity.copy(name = (field as EntityField.StringField).value)
                    "tag_description" -> entity.copy(description = (field as EntityField.StringField).value)
                    else -> throw IllegalArgumentException("unknown tag ${fieldID.tag} for entity: $entity")
                }

            }
            is EntityFieldID.EntityID -> {
                val unit = (field as? EntityField.EntityLink)?.entity as? Unit
                entity.copy(unit = unit)
            }
            else -> throw IllegalArgumentException("field with column: $fieldID was not found in entity: $entity")
        }
    }
}
