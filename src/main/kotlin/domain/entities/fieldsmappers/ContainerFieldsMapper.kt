package domain.entities.fieldsmappers

import domain.entities.Container

class ContainerFieldsMapper : BaseFieldsMapper<Container>() {

    override fun getFields(entity: Container): Map<EntityFieldID, Any?> {
        return mapOf(
            EntityFieldID.NameID to entity.name,
            EntityFieldID.DescriptionID to entity.description,
            EntityFieldID.ContainerID(entity.parentContainer?.id ?: "") to entity.parentContainer?.id
        )
    }


    override fun mapIntoEntity(entity: Container, field: EntityField): Container {
        return when (val fieldColumn = field.fieldID) {
            EntityFieldID.NameID -> entity.copy(name = (field as EntityField.StringField).value)
            EntityFieldID.DescriptionID -> entity.copy(description = (field as EntityField.StringField).value)
            is EntityFieldID.ContainerID -> entity.copy(parentContainer = (field as EntityField.EntityLink).entity as Container?)
            else -> throw IllegalArgumentException("field with column: $fieldColumn was not found in entity: $entity")
        }
    }

}