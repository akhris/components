package domain.entities.fieldsmappers

import domain.entities.Parameter
import domain.entities.Unit

class ParameterFieldsMapper : BaseFieldsMapper<Parameter>() {


    override fun getFields(entity: Parameter): Map<EntityFieldID, Any?> {
        return listOfNotNull(
            EntityFieldID.NameID to entity.name,
            EntityFieldID.DescriptionID to entity.description,
            entity.unit?.let { EntityFieldID.UnitID(it.id, it.unit) to entity.unit }
        ).toMap()
    }


    override fun mapIntoEntity(entity: Parameter, field: EntityField): Parameter {
        return when (val column = field.fieldID) {
            EntityFieldID.NameID -> entity.copy(name = (field as EntityField.StringField).value)
            EntityFieldID.DescriptionID -> entity.copy(description = (field as EntityField.StringField).value)
            is EntityFieldID.UnitID -> {
                val unit = (field as? EntityField.EntityLink)?.entity as? Unit
                entity.copy(unit = unit)
            }
            else -> throw IllegalArgumentException("field with column: $column was not found in entity: $entity")
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