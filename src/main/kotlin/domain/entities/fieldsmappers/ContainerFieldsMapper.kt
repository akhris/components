package domain.entities.fieldsmappers

import domain.entities.Container

class ContainerFieldsMapper : IFieldsMapper<Container> {


    override fun getEntityIDs(): List<EntityFieldID> {
        return listOf(
            EntityFieldID.StringID(name = "name", tag = EntityFieldID.tag_name),
            EntityFieldID.StringID(name = "description", tag = EntityFieldID.tag_description),
            EntityFieldID.EntityID(
                name = "parent container",
                tag = EntityFieldID.tag_entity_id,
                entityClass = Container::class
            )
        )
    }

    override fun getFieldByID(entity: Container, fieldID: EntityFieldID): EntityField {
        return when (fieldID) {
            is EntityFieldID.EntityID -> EntityField.EntityLink.EntityLinkSimple(
                fieldID = fieldID,
                entity = entity.parentContainer,
                description = "parent container"
            )
            is EntityFieldID.StringID -> when (fieldID.tag) {
                EntityFieldID.tag_name -> EntityField.StringField(
                    fieldID = fieldID,
                    description = "item's name",
                    value = entity.name
                )
                EntityFieldID.tag_description -> EntityField.StringField(
                    fieldID = fieldID,
                    description = "item's description",
                    value = entity.description
                )
                else -> throw IllegalArgumentException("field with tag: ${fieldID.tag} was not found in entity: $entity")
            }
            else -> throw IllegalArgumentException("field with id: $fieldID was not found in entity: $entity")
        }
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
            is EntityFieldID.EntityID -> entity.copy(parentContainer = (field as EntityField.EntityLink).entity as? Container)
            else -> throw IllegalArgumentException("field with column: $fieldID was not found in entity: $entity")
        }
    }

}