package domain.entities.fieldsmappers

import domain.entities.ObjectType
import domain.entities.Parameter
import utils.replace

class ObjectTypeFieldsMapper : IFieldsMapper<ObjectType> {

    override fun getEntityIDs(): List<EntityFieldID> {
        return listOf(
            EntityFieldID.StringID(EntityFieldID.tag_name, "name"),
            EntityFieldID.EntitiesListID(
                tag = tag_parameters,
                name = "parameters"
            ),
            EntityFieldID.EntityID(tag = tag_parent_type, name = "parent type")
        )
    }

    override fun getFieldByID(entity: ObjectType, fieldID: EntityFieldID): EntityField {
        return when (fieldID) {
            is EntityFieldID.EntityID -> when (val tag =
                fieldID.tag ?: throw IllegalArgumentException("tag must be set for $fieldID")) {
                tag_parent_type -> EntityField.EntityLink.EntityLinkSimple(
                    fieldID = fieldID,
                    entity = entity.parentEntity,
                    entityClass = ObjectType::class,
                    description = "parent type"
                )
                else -> {
                    //tag == parameter id
                    val parameter = entity.parameters.find { it.id == tag }
                    EntityField.EntityLink.EntityLinkSimple(
                        fieldID = fieldID,
                        entity = parameter,
                        entityClass = Parameter::class,
                        description = parameter?.name ?: ""
                    )
                }
            }
            is EntityFieldID.EntitiesListID -> EntityField.EntityLinksList(
                fieldID = fieldID,
                description = "parameters",
                entities = entity
                    .parameters
                    .map { param ->
                        EntityField.EntityLink.EntityLinkSimple(
                            fieldID = EntityFieldID.EntityID(
                                tag = param.id,
                                name = param.name
                            ),
                            description = param.description,
                            entity = param,
                            entityClass = Parameter::class
                        )
                    },
                entityClass = Parameter::class
            )
            is EntityFieldID.StringID -> EntityField.StringField(
                fieldID = fieldID,
                value = entity.name,
                description = "type's name"
            )
            else -> throw IllegalArgumentException("field with id: $fieldID was not found in entity: $entity")
        }
    }


    override fun mapIntoEntity(entity: ObjectType, field: EntityField): ObjectType {
        return when (field) {
            is EntityField.StringField -> {
                entity.copy(name = field.value)
            }
            is EntityField.EntityLink.EntityLinkSimple -> {
                when (field.fieldID.tag) {
                    tag_parent_type -> entity.copy(parentEntity = field.entity as? ObjectType)
                    else -> setParameter(entity, field)
                        ?: throw IllegalArgumentException("unknown tag ${field.fieldID.tag} for entity: $entity")
                }
            }
            is EntityField.EntityLinksList -> entity.copy(parameters = field.entities.map { it.entity }
                .filterIsInstance(Parameter::class.java))
            else -> throw IllegalArgumentException("field with id: ${field.fieldID} was not found in entity: $entity")
        }
    }

    private fun setParameter(objectType: ObjectType, field: EntityField.EntityLink): ObjectType? {
        val fieldID = field.fieldID
        val paramID = fieldID.tag ?: return null
        val newParameter = (field.entity as? Parameter) ?: return null
        val newParameters = objectType.parameters.replace(newValue = newParameter) {
            it.id == paramID
        }
        return objectType.copy(
            parameters = newParameters
        )
    }

    companion object {
        const val tag_parameters = "tag_parameters"
        const val tag_parent_type = "parent_object_type"
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

