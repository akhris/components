package domain.entities.fieldsmappers

import domain.entities.Container

class ContainerFieldsMapper : BaseFieldsMapper<Container>() {

    override fun getFields(entity: Container): Map<EntityFieldColumn, Any?> {
        return mapOf(
            EntityFieldColumn.NameColumn to entity.name,
            EntityFieldColumn.DescriptionColumn to entity.description,
            EntityFieldColumn.ContainerColumn(entity.parentContainer?.id ?: "") to entity.parentContainer?.id
        )
    }


    override fun mapIntoEntity(entity: Container, field: EntityField): Container {
        return when (val fieldColumn = field.fieldColumn) {
            EntityFieldColumn.NameColumn -> entity.copy(name = (field as EntityField.StringField).value)
            EntityFieldColumn.DescriptionColumn -> entity.copy(description = (field as EntityField.StringField).value)
            is EntityFieldColumn.ContainerColumn -> entity.copy(parentContainer = (field as EntityField.EntityLink).entity as Container?)
            else -> throw IllegalArgumentException("field with column: $fieldColumn was not found in entity: $entity")
        }
    }

}