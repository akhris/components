package domain.entities.fieldsmappers

import domain.entities.Container

class ContainerFieldsMapper : BaseFieldsMapper<Container>() {


    override fun getEntityIDs(entity: Container): List<EntityFieldID> {
        return listOf(
            EntityFieldID.StringID(name = "name", tag = EntityFieldID.tag_name),
            EntityFieldID.StringID(name = "description", tag = EntityFieldID.tag_description),
            EntityFieldID.EntityID(tag = "entity_id", "parent container", entityClass = Container::class)
        )
    }

    override fun getFieldParamsByFieldID(entity: Container, fieldID: EntityFieldID): DescriptiveFieldValue {
        return when (fieldID) {
            is EntityFieldID.EntityID -> DescriptiveFieldValue.CommonField(
                entity = entity.parentContainer,
                description = "parent container"
            )
            is EntityFieldID.StringID -> when (fieldID.tag) {
                EntityFieldID.tag_name -> DescriptiveFieldValue.CommonField(entity = entity.name, description = "item's name")
                EntityFieldID.tag_description -> DescriptiveFieldValue.CommonField(
                    entity = entity.description,
                    description = "item's description"
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