package domain.entities.fieldsmappers

import com.akhris.domain.core.entities.IEntity
import domain.entities.Parameter
import domain.entities.Unit
import domain.entities.Value

abstract class BaseFieldsMapper<T : IEntity<*>> : IFieldsMapper<T> {

    abstract fun getFields(entity: T): Map<EntityFieldID, Any?>

    override fun getFieldByID(entity: T, fieldID: EntityFieldID): EntityField? {
        val fieldValue = getFields(entity)[fieldID]
//        val fieldValue = getFieldValueByColumn(entity, fieldColumn)

        return when (fieldID) {
            EntityFieldID.NameID -> {
                EntityField.StringField(
                    fieldID = fieldID,
                    description = "item's name",
                    value = (fieldValue as? String) ?: ""
                )
            }
            EntityFieldID.ObjectTypeID -> {
                EntityField.EntityLink(
                    fieldID = fieldID,
                    description = "item's object type",
                    entity = (fieldValue as? IEntity<out Any>)
                )
            }
            is EntityFieldID.ValueID -> {
                val value = fieldValue as? Value
                val descr = StringBuilder(value?.parameter?.name ?: "")
                value?.parameter?.unit?.let {
                    descr.append(", ${it.unit}")
                }
                EntityField.EntityLink(
                    fieldID = fieldID,
                    description = descr.toString(),
                    entity = value
                )
            }
            is EntityFieldID.ParameterID -> {
                val param = fieldValue as? Parameter
                EntityField.EntityLink(
                    fieldID = fieldID,
                    description = param?.description ?: "",
                    entity = param
                )
            }
            EntityFieldID.DescriptionID -> EntityField.StringField(
                fieldID = fieldID,
                description = "item's description",
                value = (fieldValue as? String) ?: ""
            )

            is EntityFieldID.UnitID -> {
                val unit = fieldValue as? Unit

                EntityField.EntityLink(
                    fieldID = fieldID,
                    description = "parameter unit",
                    entity = unit
                )
            }
            is EntityFieldID.FloatID -> {
                EntityField.FloatField(
                    fieldID = fieldID,
                    description = "factor",
                    value = (fieldValue as? Float) ?: 1f
                )
            }
            is EntityFieldID.StringID -> {
                EntityField.StringField(
                    fieldID = fieldID,
                    description = "value",
                    value = (fieldValue as? String) ?: ""
                )
            }
            is EntityFieldID.BooleanID -> {
                EntityField.BooleanField(
                    fieldID = fieldID,
                    description = "boolean",
                    value = (fieldValue as? Boolean) ?: false
                )
            }
            is EntityFieldID.ContainerID -> {
                EntityField.EntityLink(
                    fieldID = fieldID,
                    description = "item's object type",
                    entity = (fieldValue as? IEntity<out Any>)
                )
            }
        }
    }

    override fun getEntityColumns(entity: T): List<EntityFieldID> {
        return getFields(entity).keys.toList()
    }

}