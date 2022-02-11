package domain.entities.fieldsmappers

import domain.entities.ObjectType
import domain.entities.Parameter
import utils.replace

class ObjectTypeFieldsMapper : BaseFieldsMapper<ObjectType>() {


    override fun getFields(entity: ObjectType): Map<EntityFieldColumn, Any?> {
        return listOfNotNull(
            EntityFieldColumn.NameColumn to entity.name
        ).plus(
            entity.parameters.map {
                EntityFieldColumn.ParameterColumn(it.id, it.name) to it
            }
        ).toMap()
    }




    override fun mapIntoEntity(entity: ObjectType, field: EntityField): ObjectType {
        return when (val column = field.fieldColumn) {
            EntityFieldColumn.NameColumn -> entity.copy(name = (field as EntityField.StringField).value)
            is EntityFieldColumn.ParameterColumn -> entity.copy(parameters = entity.parameters.replace(((field as EntityField.EntityLink).entity as Parameter)) {
                it.id == column.paramID
            })
            else -> throw IllegalArgumentException("field with column: $column was not found in entity: $entity")
        }
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

