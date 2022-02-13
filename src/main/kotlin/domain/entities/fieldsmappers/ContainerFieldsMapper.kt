package domain.entities.fieldsmappers

import domain.entities.Container

class ContainerFieldsMapper : BaseFieldsMapper<Container>() {

    override fun getFields(entity: Container): Map<EntityFieldID, Any?> {
        return mapOf(
            EntityFieldID.StringID(name = "name", tag = "tag_name") to entity.name,
            EntityFieldID.StringID(name = "description", tag = "tag_description") to entity.description,
            EntityFieldID.EntityID(tag = "entity_id", "parent container") to entity.parentContainer?.id
        )
    }


    override fun mapIntoEntity(entity: Container, field: EntityField): Container {
        return when (val fieldID = field.fieldID) {
            is EntityFieldID.StringID -> {
                when (fieldID.tag) {
                    "tag_name" -> entity.copy(name = (field as EntityField.StringField).value)
                    "tag_description" -> entity.copy(description = (field as EntityField.StringField).value)
                    else -> throw IllegalArgumentException("unknown tag ${fieldID.tag} for entity: $entity")
                }

            }
            is EntityFieldID.EntityID -> entity.copy(parentContainer = (field as EntityField.EntityLink).entity as Container?)
            else -> throw IllegalArgumentException("field with column: $fieldID was not found in entity: $entity")
        }
    }

}