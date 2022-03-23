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
                    value = (fieldParams.entity as? Float) ?: 1f,
                    description = fieldParams.description
                )
            }
            is EntityFieldID.StringID -> {
                EntityField.StringField(
                    fieldID = fieldID,
                    value = (fieldParams.entity as? String) ?: "",
                    description = fieldParams.description
                )
            }
            is EntityFieldID.BooleanID -> {
                EntityField.BooleanField(
                    fieldID = fieldID,
                    value = (fieldParams.entity as? Boolean) ?: false,
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
//                        mapEntity(entityID, entityField)
                    },
                    description = fieldParams.description,
                    entityClass = fieldID.entityClass
                )
            }
            is EntityFieldID.DateTimeID -> EntityField.DateTimeField(
                fieldID = fieldID,
                description = fieldParams.description,
                value = fieldParams.entity as? LocalDateTime
            )
            is EntityFieldID.LongID -> EntityField.LongField(
                fieldID = fieldID,
                description = fieldParams.description,
                value = fieldParams.entity as? Long ?: 0L
            )
        }
    }

    private fun mapEntity(
        fieldID: EntityFieldID.EntityID,
        fieldParams: DescriptiveFieldValue
    ): EntityField.EntityLink {


//        when (val e = fieldParams.value) {
//            is IEntity<*> -> {
//                entity = e as T
//                count = null
//            }
//            is EntityCountable<*> -> {
//                entity = e.entity as T
//                count = e.count
//            }
//            else -> {
//                entity = null
//                count = null
//            }
//        }

        return when (fieldParams) {
            is DescriptiveFieldValue.CommonField -> EntityField.EntityLink.EntityLinkSimple(
                fieldID = fieldID,
                description = fieldParams.description,
                entity = fieldParams.entity as? T,
                entityClass = fieldID.entityClass   //fixme maybe passing fieldID is sufficient?
            )
            is DescriptiveFieldValue.CountableField -> EntityField.EntityLink.EntityLinkCountable(
                fieldID = fieldID,
                description = fieldParams.description,
                entity = fieldParams.entity as? T,
                entityClass = fieldID.entityClass,
                count = fieldParams.count
            )
            is DescriptiveFieldValue.ValuableField -> EntityField.EntityLink.EntityLinkValuable(
                fieldID = fieldID,
                description = fieldParams.description,
                entity = fieldParams.entity as? T,
                entityClass = fieldID.entityClass,
                value = fieldParams.value,
                factor = fieldParams.factor,
                unit = fieldParams.unit
            )
        }
    }

}


sealed class DescriptiveFieldValue {
    abstract val entity: Any?
    abstract val description: String

    data class CommonField(override val entity: Any? = null, override val description: String = "") :
        DescriptiveFieldValue()

    data class CountableField(
        override val entity: Any? = null,
        override val description: String = "",
        val count: Long? = null
    ) :
        DescriptiveFieldValue()

    data class ValuableField constructor(
        override val entity: Any? = null,
        override val description: String = "",
        val value: String? = null,
        val factor: Int? = null,
        val unit: String? = null
    ) :
        DescriptiveFieldValue()


}