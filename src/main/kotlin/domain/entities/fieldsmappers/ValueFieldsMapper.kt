package domain.entities.fieldsmappers

import domain.entities.Parameter
import domain.entities.Value

class ValueFieldsMapper : BaseFieldsMapper<Value>() {

    override fun getFields(entity: Value): Map<EntityFieldColumn, Any?> {
        return mapOf(
            EntityFieldColumn.StringColumn() to entity.value,
            EntityFieldColumn.FloatColumn() to entity.factor,
            EntityFieldColumn.ParameterColumn(entity.parameter.id, entity.parameter.name) to entity.parameter
        )
    }


    override fun mapIntoEntity(entity: Value, field: EntityField): Value {
        return when (val column = field.fieldColumn) {
            is EntityFieldColumn.StringColumn -> entity.copy(value = (field as EntityField.StringField).value)
            is EntityFieldColumn.FloatColumn -> entity.copy(factor = (field as EntityField.FloatField).value)
            is EntityFieldColumn.ParameterColumn -> entity.copy(parameter = (field as EntityField.EntityLink).entity as Parameter)
            else -> throw IllegalArgumentException("field with column: $column was not found in entity: $entity")
        }
    }


}