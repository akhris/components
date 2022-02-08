package domain.entities.fieldsmappers

import com.akhris.domain.core.entities.IEntity
import domain.entities.Container

class ContainerFieldsMapper : IFieldsMapper {
    override fun mapFields(entity: Any): List<EntityField> {
        return when (entity) {
            is Container -> mapValueFields(entity)
            else -> throw IllegalArgumentException("$this cannot map $entity fields, use another mapper")
        }
    }

    override fun <T : IEntity<*>> mapIntoEntity(entity: T, field: EntityField): T {
        return when (entity) {
            is Container -> mapIntoValue(entity, field) as T
            else -> throw IllegalArgumentException("$this cannot map $entity fields, use another mapper")
        }
    }

    private fun mapValueFields(value: Container): List<EntityField> {
        return listOfNotNull(
            EntityField.StringField(
                tag = "place_field_name",
                name = "name",
                description = "String representing place's name",
                value = value.name
            ),
            EntityField.StringField(
                tag = "place_field_description",
                name = "description",
                description = "description of the place",
                value = value.description
            ),
            EntityField.EntityLink(
                tag = "place_field_parent_place",
                name = value.parentContainer?.name ?: "",
                description = "parent place",
                entity = value.parentContainer
            ),
            EntityField.CaptionField(
                tag = "place_field_id",
                name = "UUID",
                description = "unique id of the place",
                caption = value.id
            )
        )
    }

    private fun mapIntoValue(container: Container, field: EntityField): Container {
        return when (field.tag) {
            "place_field_name" -> container.copy(name = (field as EntityField.StringField).value)
            "place_field_description" -> container.copy(description = (field as EntityField.StringField).value)
            "place_field_parent_place" -> container.copy(parentContainer = (field as EntityField.EntityLink).entity as Container?)
            else -> throw IllegalArgumentException("field with tag: ${field.tag} was not found in entity: $container")
        }
    }
}