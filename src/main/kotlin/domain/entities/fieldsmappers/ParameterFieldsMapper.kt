package domain.entities.fieldsmappers

import domain.entities.Parameter
import domain.entities.Unit

class ParameterFieldsMapper : BaseFieldsMapper<Parameter>() {


    override fun getFields(entity: Parameter): Map<EntityFieldID, Any?> {
        return listOf(
            EntityFieldID.StringID(tag = "tag_name", name = "name") to entity.name,
            EntityFieldID.StringID(tag = "tag_description", name = "description") to entity.description,
            EntityFieldID.EntityID(tag = "tag_unit", name = "unit") to entity.unit
        ).toMap()
    }


    override fun mapIntoEntity(entity: Parameter, field: EntityField): Parameter {
        return when (val fieldID = field.fieldID) {
            is EntityFieldID.StringID -> {
                when (fieldID.tag) {
                    "tag_name" -> entity.copy(name = (field as EntityField.StringField).value)
                    "tag_description" -> entity.copy(description = (field as EntityField.StringField).value)
                    else -> throw IllegalArgumentException("unknown tag ${fieldID.tag} for entity: $entity")
                }

            }
            is EntityFieldID.EntityID -> {
                val unit = (field as? EntityField.EntityLink)?.entity as? Unit
                entity.copy(unit = unit)
            }
            else -> throw IllegalArgumentException("field with column: $fieldID was not found in entity: $entity")
        }
    }
}


//    override fun mapFields(entity: Any): List<EntityField> {
//        return when (entity) {
//            is Parameter -> mapParameterFields(entity)
//            else -> throw IllegalArgumentException("$this cannot map $entity fields, use another mapper")
//        }
//    }


//    private fun mapParameterFields(param: Parameter): List<EntityField> {
//        return listOfNotNull(
//            EntityField.StringField(
//                tag = "parameter_field_name",
//                name = "name",
//                description = "parameter name",
//                value = param.name
//            ), EntityField.StringField(
//                tag = "parameter_field_description",
//                name = "description",
//                description = "parameter description",
//                value = param.description
//            ),
//
//            EntityField.EntityLink(
//                tag = "parameter_unit",
//                name = param.unit?.unit ?: "unit",
//                description = "parameter unit",
//                entity = param.unit
//            ),
//            EntityField.CaptionField(
//                tag = "parameter_field_id",
//                name = "UUID",
//                description = "unique id of the parameter",
//                caption = param.id
//            )
//        )
//    }