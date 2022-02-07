package domain.entities.fieldsmappers

import com.akhris.domain.core.entities.IEntity
import domain.entities.ObjectType
import domain.entities.Parameter
import domain.entities.Unit

class ObjectTypeFieldsMapper : IFieldsMapper {
    override fun mapFields(entity: Any): List<EntityField> {
        return when (entity) {
            is ObjectType -> mapObjectTypeFields(entity)
            else -> throw IllegalArgumentException("$this cannot map $entity fields, use another mapper")
        }
    }

    override fun <T : IEntity<*>> mapIntoEntity(entity: T, field: EntityField): T {
        return when (entity) {
            is ObjectType -> mapIntoObjectType(entity, field) as T
            else -> throw IllegalArgumentException("$this cannot map $entity fields, use another mapper")
        }
    }

    private fun mapObjectTypeFields(type: ObjectType): List<EntityField> {
        return listOf(
            EntityField.StringField(
                tag = "objecttype_field_name",
                name = "name",
                description = "type name",
                value = type.name
            ),
            EntityField.EntityLinksList(
                tag = "objecttype_field_parameters",
                name = "type parameters",
                description = "default parameters set that is mandatory for this type",
                entities = type.parameters.mapIndexed { i, p ->
                    EntityField.EntityLink(
                        tag = "objecttype_field_parameter_link$i",
                        name = p.name,
                        description = p.description,
                        entity = p
                    )
                }


            ),
            EntityField.CaptionField(
                tag = "objecttype_field_id",
                name = "UUID",
                description = "unique id of the object type",
                caption = type.id
            )
        )
    }

    private fun mapIntoObjectType(type: ObjectType, field: EntityField): ObjectType {
        return when (field.tag) {
            "objecttype_field_name" -> type.copy(name = (field as EntityField.StringField).value)
            "objecttype_field_parameters" -> type.copy(parameters = ((field as EntityField.EntityLinksList).entities as List<Parameter>))
            else -> throw IllegalArgumentException("field with tag: ${field.tag} was not found in entity: $type")
        }
    }
}