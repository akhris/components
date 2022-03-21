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
                        EntityField.EntityLink(
                            fieldID = entityID,
                            entity = entityField.entity as? T,
                            entityClass = fieldID.entityClass,
                            description = entityField.description,
                            count = entityField.count
                        )
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

        val entity: T? = fieldParams.entity as? T
        val count: Long? = fieldParams.count

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


        return EntityField.EntityLink(
            fieldID = fieldID,
            entity = entity,
            entityClass = fieldID.entityClass,
            description = fieldParams.description,
            count = count
        )

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

    data class ValuableField(
        override val entity: Any? = null,
        override val description: String = "",
        val value: String? = null,
        val factor: Float? = null
    ) :
        DescriptiveFieldValue()


}