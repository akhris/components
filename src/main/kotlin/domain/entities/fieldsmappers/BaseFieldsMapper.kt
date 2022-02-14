package domain.entities.fieldsmappers

import com.akhris.domain.core.entities.IEntity
import java.time.LocalDateTime

abstract class BaseFieldsMapper<T : IEntity<*>> : IFieldsMapper<T> {

    abstract fun getFieldParamsByFieldID(entity: T, fieldID: EntityFieldID): DescriptiveFieldValue

    override fun getFieldByID(entity: T, fieldID: EntityFieldID): EntityField? {
        val fieldParams = getFieldParamsByFieldID(entity, fieldID)

        return when (fieldID) {
            is EntityFieldID.FloatID -> {
                EntityField.FloatField(
                    fieldID = fieldID,
                    value = (fieldParams.value as? Float) ?: 1f,
                    description = fieldParams.description
                )
            }
            is EntityFieldID.StringID -> {
                EntityField.StringField(
                    fieldID = fieldID,
                    value = (fieldParams.value as? String) ?: "",
                    description = fieldParams.description
                )
            }
            is EntityFieldID.BooleanID -> {
                EntityField.BooleanField(
                    fieldID = fieldID,
                    value = (fieldParams.value as? Boolean) ?: false,
                    description = fieldParams.description
                )
            }
            is EntityFieldID.EntityID -> {
                EntityField.EntityLink(
                    fieldID = fieldID,
                    entity = (fieldParams.value as? IEntity<out Any>),
                    description = fieldParams.description
                )
            }
            is EntityFieldID.EntitiesListID -> {
                EntityField.EntityLinksList(
                    fieldID = fieldID,
                    entities = fieldID.entitiesIDs.map { entityID ->
                        val entityField = getFieldParamsByFieldID(entity, entityID)
                        EntityField.EntityLink(
                            fieldID = entityID,
                            entity = entityField.value as? IEntity<out Any>,
                            description = entityField.description
                        )
                    },
                    description = fieldParams.description
                )
            }
            is EntityFieldID.DateTimeID -> EntityField.DateTimeField(
                fieldID = fieldID,
                description = fieldParams.description,
                value = fieldParams.value as? LocalDateTime
            )
            is EntityFieldID.LongID -> EntityField.LongField(
                fieldID = fieldID,
                description = fieldParams.description,
                value = fieldParams.value as? Long ?: 0L
            )
        }
    }


}

data class DescriptiveFieldValue(val value: Any? = null, val description: String = "")
//data class DescriptiveEntity(val entity: IEntity<out Any>, val name: String, val description: String)