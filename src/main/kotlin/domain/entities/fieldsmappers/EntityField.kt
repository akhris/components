package domain.entities.fieldsmappers

import com.akhris.domain.core.entities.IEntity
import utils.DateTimeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.reflect.KClass

/**
 * Entity's field class representing value of the entity field.
 */
sealed class EntityField {
    abstract val fieldID: EntityFieldID
    abstract val description: String

    /**
     * Represents String value of entity field
     */
    data class StringField(
        override val fieldID: EntityFieldID,
        override val description: String,
        val value: String
    ) :
        EntityField() {
        override fun toString(): String = value
    }

    /**
     * Represents Float value of entity field
     */
    data class FloatField(
        override val fieldID: EntityFieldID,
        override val description: String,
        val value: Float
    ) :
        EntityField() {
        override fun toString(): String = value.toString()
    }

    /**
     * Represents Boolean value of entity field
     */
    data class BooleanField(
        override val fieldID: EntityFieldID,
        override val description: String,
        val value: Boolean
    ) : EntityField() {
        override fun toString(): String = value.toString()
    }

    /**
     * Represents Long value of entity field
     */
    data class LongField(
        override val fieldID: EntityFieldID.LongID,
        override val description: String,
        val value: Long
    ) : EntityField() {
        override fun toString(): String = value.toString()
    }

    /**
     * Represents Date&Time value of entity field
     */
    data class DateTimeField(
        override val fieldID: EntityFieldID.DateTimeID,
        override val description: String,
        val value: LocalDateTime?
    ) : EntityField() {
        override fun toString(): String = value?.let { DateTimeConverter.dateTimeToString(it) }?:"no date/time"
    }

    /**
     * Represents EntityLink value of entity field
     */
    data class EntityLink constructor(
        override val fieldID: EntityFieldID,
        override val description: String = "",
        val entity: IEntity<*>?,
        val entityClass: KClass<out IEntity<*>>,
        val count: Long? = null
    ) : EntityField() {
        override fun toString(): String = entity?.toString() ?: description
    }

    /**
     * Represents EntityLink's list value of entity field
     */
    data class EntityLinksList constructor(
        override val fieldID: EntityFieldID,
        override val description: String,
        val entities: List<EntityLink>,
        val entityClass: KClass<out IEntity<*>>
    ) : EntityField() {
        override fun toString(): String = description
    }
}

fun EntityField.EntityLink.getName(): String {
    return entity?.toString()?.ifEmpty { fieldID.name } ?: ""
}