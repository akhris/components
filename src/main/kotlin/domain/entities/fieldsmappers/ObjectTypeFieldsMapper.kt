package domain.entities.fieldsmappers

import domain.entities.ObjectType
import domain.entities.Parameter

class ObjectTypeFieldsMapper : BaseFieldsMapper<ObjectType>() {

    private val tag_parameters = "tag_parameters"

    override fun getEntityIDs(): List<EntityFieldID> {
        return listOf(
            EntityFieldID.StringID(EntityFieldID.tag_name, "name"),
            EntityFieldID.EntitiesListID(
                tag = tag_parameters,
                name = "parameters",
//                entitiesIDs = List(entity.parameters.size) { index ->
//                    EntityFieldID.EntityID(
//                        tag = "$tag_parameters$index",
//                        name = "parameter ${index + 1}",
//                        entityClass = Parameter::class
//                    )
//                },
                entityClass = Parameter::class
            ),
            EntityFieldID.EntityID(tag = "parent_object_type", name = "parent type", entityClass = ObjectType::class)
        )
    }


    override fun getFieldParamsByFieldID(entity: ObjectType, fieldID: EntityFieldID): DescriptiveFieldValue {
        return when (fieldID) {
            is EntityFieldID.EntityID -> when (fieldID.tag) {
                "parent_object_type" -> {
                    DescriptiveFieldValue.CommonField(
                        entity = entity.parentObjectType,
                        description = "parent type"
                    )
                }
                else -> {
                    val index = fieldID.tag.substring(startIndex = tag_parameters.length).toIntOrNull() ?: -1
                    val parameter = entity.parameters.getOrNull(index)
                    DescriptiveFieldValue.CommonField(parameter, description = parameter?.name ?: "")
                }
            }
            is EntityFieldID.EntitiesListID -> DescriptiveFieldValue.CommonField(
                entity = entity.parameters,
                description = "parameters"
            )
            is EntityFieldID.StringID -> DescriptiveFieldValue.CommonField(
                entity = entity.name,
                description = "type's name"
            )
            else -> throw IllegalArgumentException("field with id: $fieldID was not found in entity: $entity")
        }
    }

    override fun mapIntoEntity(entity: ObjectType, field: EntityField): ObjectType {
        return when (val fieldID = field.fieldID) {
            is EntityFieldID.StringID -> entity.copy(name = (field as EntityField.StringField).value)
            is EntityFieldID.EntityID -> when (fieldID.tag) {
                "parent_object_type" -> {
                    entity.copy(parentObjectType = (field as EntityField.EntityLink).entity as? ObjectType)
                }
                else -> {
                    setParameter(entity, field)
                        ?: throw IllegalArgumentException("unknown tag ${fieldID.tag} for entity: $entity")
                }
            }

            is EntityFieldID.EntitiesListID -> entity.copy(parameters = (field as EntityField.EntityLinksList).entities.mapNotNull { it.entity as? Parameter })
            else -> throw IllegalArgumentException("field with column: $fieldID was not found in entity: $entity")
        }
    }

    private fun setParameter(objectType: ObjectType, field: EntityField): ObjectType? {
        val fieldID = field.fieldID
        val paramIndex = fieldID.tag.substring(startIndex = tag_parameters.length).toIntOrNull() ?: return null
        if (objectType.parameters.getOrNull(paramIndex) == null) {
            return null
        }
        return objectType.copy(
            parameters = objectType.parameters.mapIndexed { index, param ->
                if (index == paramIndex) {
                    (field as EntityField.EntityLink).entity as Parameter
                } else param
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

