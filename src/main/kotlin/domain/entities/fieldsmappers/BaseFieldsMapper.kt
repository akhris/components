package domain.entities.fieldsmappers

import com.akhris.domain.core.entities.IEntity

abstract class BaseFieldsMapper<T : IEntity<*>> : IFieldsMapper<T> {

    abstract fun getFields(entity: T): Map<EntityFieldID, Any?>

    override fun getFieldByID(entity: T, fieldID: EntityFieldID): EntityField? {
        val fieldValue = getFields(entity)[fieldID]
//        val fieldValue = getFieldValueByColumn(entity, fieldColumn)

        return when (fieldID) {
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
            is EntityFieldID.EntityID -> {
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