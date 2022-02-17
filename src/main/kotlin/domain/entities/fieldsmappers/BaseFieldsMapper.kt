package domain.entities.fieldsmappers

import com.akhris.domain.core.entities.IEntity
import domain.entities.EntityCountable
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
            is EntityFieldID.EntityID -> mapEntity(fieldID, fieldParams)
            is EntityFieldID.EntitiesListID -> {
                EntityField.EntityLinksList(
                    fieldID = fieldID,
                    entities = fieldID.entitiesIDs.map { entityID ->
                        val entityField = getFieldParamsByFieldID(entity, entityID)
                        mapEntity(entityID, entityField)
                    },
                    description = fieldParams.description,
                    entityClass = fieldID.entityClass
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

    private fun mapEntity(
        fieldID: EntityFieldID.EntityID,
        fieldParams: DescriptiveFieldValue
    ): EntityField.EntityLink {

        val entity: T?
        val count: Long?

        when (val e = fieldParams.value) {
            is IEntity<*> -> {
                entity = e as T
                count = null
            }
            is EntityCountable<*> -> {
                entity = e.entity as T
                count = e.count
            }
            else -> {
                entity = null
                count = null
            }
        }



        return EntityField.EntityLink(
            fieldID = fieldID,
            entity = entity,
            entityClass = fieldID.entityClass,
            description = fieldParams.description,
            count = count
        )

    }

}


data class DescriptiveFieldValue constructor(val value: Any? = null, val description: String = "")