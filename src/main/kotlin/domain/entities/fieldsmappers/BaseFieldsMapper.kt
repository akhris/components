package domain.entities.fieldsmappers

import com.akhris.domain.core.entities.IEntity
import domain.entities.Parameter
import domain.entities.Unit
import domain.entities.Value

abstract class BaseFieldsMapper<T : IEntity<*>> : IFieldsMapper<T> {

    abstract fun getFields(entity: T): Map<EntityFieldColumn, Any?>

    override fun getFieldByColumn(entity: T, fieldColumn: EntityFieldColumn): EntityField? {
        val fieldValue = getFields(entity)[fieldColumn]
//        val fieldValue = getFieldValueByColumn(entity, fieldColumn)

        return when (fieldColumn) {
            EntityFieldColumn.NameColumn -> {
                EntityField.StringField(
                    fieldColumn = fieldColumn,
                    description = "item's name",
                    value = (fieldValue as? String) ?: ""
                )
            }
            EntityFieldColumn.ObjectTypeColumn -> {
                EntityField.EntityLink(
                    fieldColumn = fieldColumn,
                    description = "item's object type",
                    entity = (fieldValue as? IEntity<out Any>)
                )
            }
            is EntityFieldColumn.ValueColumn -> {
                val value = fieldValue as? Value
                val descr = StringBuilder(value?.parameter?.name ?: "")
                value?.parameter?.unit?.let {
                    descr.append(", ${it.unit}")
                }
                EntityField.EntityLink(
                    fieldColumn = fieldColumn,
                    description = descr.toString(),
                    entity = value
                )
            }
            is EntityFieldColumn.ParameterColumn -> {
                val param = fieldValue as? Parameter
                EntityField.EntityLink(
                    fieldColumn = fieldColumn,
                    description = param?.description ?: "",
                    entity = param
                )
            }
            EntityFieldColumn.DescriptionColumn -> EntityField.StringField(
                fieldColumn = fieldColumn,
                description = "item's description",
                value = (fieldValue as? String) ?: ""
            )

            is EntityFieldColumn.UnitColumn -> {
                val unit = fieldValue as? Unit

                EntityField.EntityLink(
                    fieldColumn = fieldColumn,
                    description = "parameter unit",
                    entity = unit
                )
            }
            is EntityFieldColumn.FloatColumn -> {
                EntityField.FloatField(
                    fieldColumn = fieldColumn,
                    description = "factor",
                    value = (fieldValue as? Float) ?: 1f
                )
            }
            is EntityFieldColumn.StringColumn -> {
                EntityField.StringField(
                    fieldColumn = fieldColumn,
                    description = "value",
                    value = (fieldValue as? String) ?: ""
                )
            }
            is EntityFieldColumn.BooleanColumn -> {
                EntityField.BooleanField(
                    fieldColumn = fieldColumn,
                    description = "boolean",
                    value = (fieldValue as? Boolean) ?: false
                )
            }
            is EntityFieldColumn.ContainerColumn -> {
                EntityField.EntityLink(
                    fieldColumn = fieldColumn,
                    description = "item's object type",
                    entity = (fieldValue as? IEntity<out Any>)
                )
            }
        }
    }

    override fun getEntityColumns(entity: T): List<EntityFieldColumn> {
        return getFields(entity).keys.toList()
    }

}