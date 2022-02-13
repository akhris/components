package domain.entities.fieldsmappers

import domain.entities.ObjectType
import domain.entities.Parameter

class ObjectTypeFieldsMapper : BaseFieldsMapper<ObjectType>() {

    private val tag_const_part = "tag_parameter_"

    override fun getFields(entity: ObjectType): Map<EntityFieldID, Any?> {
        return listOfNotNull(
            EntityFieldID.StringID("tag_name", "name") to entity.name
        ).plus(
            entity.parameters.mapIndexed { index, p ->
                EntityFieldID.EntityID(tag = "$tag_const_part$index", name = "parameter 1") to p
//                EntityFieldID.ParameterID(it.id, it.name) to it
            }
        ).toMap()
    }


    override fun mapIntoEntity(entity: ObjectType, field: EntityField): ObjectType {
        return when (val column = field.fieldID) {
            is EntityFieldID.StringID -> entity.copy(name = (field as EntityField.StringField).value)
            is EntityFieldID.EntityID -> setParameter(entity, field)
            else -> throw IllegalArgumentException("field with column: $column was not found in entity: $entity")
        }
    }

    private fun setParameter(objectType: ObjectType, field: EntityField): ObjectType {
        val fieldID = field.fieldID
        val paramIndex = fieldID.tag.substring(startIndex = tag_const_part.length).toIntOrNull() ?: return objectType

        return objectType.copy(
            parameters = objectType.parameters.mapIndexed { index, parameter ->
                if (index == paramIndex) {
                    (field as EntityField.EntityLink).entity as Parameter
                } else parameter
            }
        )
    }

}
//    override fun mapFields(entity: Any): List<EntityField> {
//        return when (entity) {
//            is ObjectType -> mapObjectTypeFields(entity)
//            else -> throw IllegalArgumentException("$this cannot map $entity fields, use another mapper")
//        }
//    }

//    private fun mapObjectTypeFields(type: ObjectType): List<EntityField> {
//        return listOf(
//            EntityField.StringField(
//                tag = "objecttype_field_name",
//                name = "name",
//                description = "type name",
//                value = type.name
//            ),
//            EntityField.EntityLinksList(
//                tag = "objecttype_field_parameters",
//                name = "type parameters",
//                description = "default parameters set that is mandatory for this type",
//                entities = type.parameters.mapIndexed { i, p ->
//                    EntityField.EntityLink(
//                        tag = "objecttype_field_parameter_link$i",
//                        name = p.name,
//                        description = p.description,
//                        entity = p
//                    )
//                }
//
//
//            ),
//            EntityField.CaptionField(
//                tag = "objecttype_field_id",
//                name = "UUID",
//                description = "unique id of the object type",
//                caption = type.id
//            )
//        )
//    }
//

