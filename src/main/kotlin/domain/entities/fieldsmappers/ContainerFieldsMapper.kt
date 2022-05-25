package domain.entities.fieldsmappers

import domain.entities.Container
import strings.StringsIDs

class ContainerFieldsMapper : IFieldsMapper<Container> {

    override fun getEntityIDs(): List<EntityFieldID> {
        return listOf(
            EntityFieldID(tag = EntityFieldID.tag_name, name = "name"),
            EntityFieldID(tag = EntityFieldID.tag_description, name = "description"),
            EntityFieldID(tag = EntityFieldID.tag_entity_id, name = "parent container")
        )
    }

    override fun getFieldByID(entity: Container, fieldID: EntityFieldID): EntityField {
        return when (fieldID.tag) {
            EntityFieldID.tag_entity_id -> EntityField.EntityLink.EntityLinkSimple(
                fieldID = fieldID,
                entity = entity.parentContainer,
                entityClass = Container::class,
                descriptionID = StringsIDs.parent_container.name
            )
            EntityFieldID.tag_name -> EntityField.StringField(
                fieldID = fieldID,
                descriptionID = StringsIDs.item_s_name.name,
                value = entity.name
            )
            EntityFieldID.tag_description -> EntityField.StringField(
                fieldID = fieldID,
                descriptionID = StringsIDs.items_description.name,
                value = entity.description
            )
            else -> throw IllegalArgumentException("field with id: $fieldID was not found in entity: $entity")
        }
    }


    override fun mapIntoEntity(entity: Container, field: EntityField): Container {
        return when (field) {
            is EntityField.StringField -> {
                when (field.fieldID.tag) {
                    "tag_name" -> entity.copy(name = field.value)
                    "tag_description" -> entity.copy(description = field.value)
                    else -> throw IllegalArgumentException("unknown tag ${field.fieldID.tag} for entity: $entity")
                }

            }
            is EntityField.EntityLink-> entity.copy(parentContainer = field.entity as? Container)
            else -> throw IllegalArgumentException("field with column: ${field.fieldID} was not found in entity: $entity")
        }
    }

}