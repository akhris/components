package domain.entities.fieldsmappers

import com.akhris.domain.core.entities.IEntity
import java.time.LocalDateTime

/**
 * Base implementation of [IFieldsMapper] interface
 *
 * The way [IEntity] is mapped:
 * 1. Entity Renderer calls [IFieldsMapper.getEntityIDs] to get List of [EntityFieldID] for [IEntity] to render.
 * 2. For each [EntityFieldID] Renderer calls [IFieldsMapper.getFieldByID] to get [EntityField]
 * 3. Renderer renders [EntityField]
 *
 */
abstract class BaseFieldsMapper<T : IEntity<*>> : IFieldsMapper<T> {

    /**
     *
     */
    abstract fun getFieldParamsByFieldID(entity: T, fieldID: EntityFieldID): DescriptiveFieldValue

    /**
     * Read actual field value of [entity] for given [fieldID] or null
     */
    override fun getFieldByID(entity: T, fieldID: EntityFieldID): EntityField? {
        val fieldParams = getFieldParamsByFieldID(entity, fieldID)
// TODO: 5/18/22 why using DescriptiveFieldValue and not EntityField directly?
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
                    description = fieldParams.description,
                    isPlaceholder = (fieldParams as? DescriptiveFieldValue.CommonField)?.isAlternative ?: false
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
                val e = (getFieldParamsByFieldID(entity, fieldID).entity as? List<*>)
                val entitiesSize = e?.size ?: 0
                val entities = List(entitiesSize) { index ->
                    val entityID = EntityFieldID.EntityID(
                        tag = "${fieldID.tag}$index",
                        name = "${fieldID.name} #${index + 1}",
                        entityClass = fieldID.entityClass
                    )
                    val entityField = getFieldParamsByFieldID(entity, entityID)
                    mapEntity(entityID, entityField)
                }

                EntityField.EntityLinksList(
                    fieldID = fieldID,
                    entities = entities,
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


        return when (fieldParams) {
            is DescriptiveFieldValue.CommonField -> EntityField.EntityLink.EntityLinkSimple(
                fieldID = fieldID,
                description = fieldParams.description,
                entity = fieldParams.entity as? T
            )
            is DescriptiveFieldValue.CountableField -> EntityField.EntityLink.EntityLinkCountable(
                fieldID = fieldID,
                description = fieldParams.description,
                entity = fieldParams.entity as? T,
                count = fieldParams.count
            )
            is DescriptiveFieldValue.ValuableField -> EntityField.EntityLink.EntityLinkValuable(
                fieldID = fieldID,
                description = fieldParams.description,
                entity = fieldParams.entity as? T,
                value = fieldParams.value,
                factor = fieldParams.factor,
                unit = fieldParams.unit
            )
        }
    }


}

@Deprecated("Use direct mapping to EntityField")
sealed class DescriptiveFieldValue {
    abstract val entity: Any?
    abstract val description: String

    data class CommonField(
        override val entity: Any? = null,
        override val description: String = "",
        val isAlternative: Boolean = false
    ) :
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