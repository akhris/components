package domain.entities.fieldsmappers

import com.akhris.domain.core.entities.IEntity
import utils.DateTimeConverter
import java.time.LocalDateTime
import kotlin.reflect.KClass

/**
 * Entity's field class representing value of the entity field.
 */
sealed class EntityField {
    abstract val fieldID: EntityFieldID
    abstract val descriptionID: String

    /**
     * Represents String value of entity field
     */
    data class StringField(
        override val fieldID: EntityFieldID,
        override val descriptionID: String,
        val value: String,
        val isPlaceholder: Boolean = false
    ) :
        EntityField() {
        override fun toString(): String = value
    }

    /**
     * Represents Float value of entity field
     */
    data class FloatField(
        override val fieldID: EntityFieldID,
        override val descriptionID: String,
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
        override val descriptionID: String,
        val value: Boolean
    ) : EntityField() {
        override fun toString(): String = value.toString()
    }

    /**
     * Represents Long value of entity field
     */
    data class LongField(
        override val fieldID: EntityFieldID,
        override val descriptionID: String,
        val value: Long
    ) : EntityField() {
        override fun toString(): String = value.toString()
    }

    /**
     * Represents Date&Time value of entity field
     */
    data class DateTimeField(
        override val fieldID: EntityFieldID,
        override val descriptionID: String,
        val value: LocalDateTime?
    ) : EntityField() {
        override fun toString(): String = value?.let { DateTimeConverter.dateTimeToString(it) } ?: "no date/time"
    }

    /**
     * Represents EntityLink value of entity field
     */

    sealed class EntityLink : EntityField() {
        abstract val entity: IEntity<*>?
        abstract override val fieldID: EntityFieldID
        abstract val entityClass: KClass<out IEntity<*>>    //is needed to decide what entity to add when null

        data class EntityLinkSimple(
            override val fieldID: EntityFieldID,
            override val descriptionID: String = "",
            override val entity: IEntity<*>?,
            override val entityClass: KClass<out IEntity<*>>
        ) : EntityLink() {
            override fun toString(): String = entity?.toString() ?: descriptionID
        }

        data class EntityLinkCountable(
            override val fieldID: EntityFieldID,
            override val descriptionID: String = "",
            override val entity: IEntity<*>?,
            override val entityClass: KClass<out IEntity<*>>,
            val count: Long? = null
        ) : EntityLink() {
            override fun toString(): String = entity?.toString() ?: descriptionID
        }

        data class EntityLinkValuable constructor(
            override val fieldID: EntityFieldID,
            override val descriptionID: String = "",
            override val entity: IEntity<*>?,
            override val entityClass: KClass<out IEntity<*>>,
            val value: String? = null,
            val factor: Int? = null,
            val unit: String? = null
        ) : EntityLink() {
            override fun toString(): String = entity?.toString() ?: descriptionID
        }

        //used for filtering

    }

    /**
     * Represents EntityLink's list value of entity field
     */
    data class EntityLinksList constructor(
        override val fieldID: EntityFieldID,
        override val descriptionID: String,
        val entities: List<EntityLink>,
        val entityClass: KClass<out IEntity<*>>
    ) : EntityField() {
        override fun toString(): String = descriptionID
    }
}

fun EntityField.EntityLink.getName(): String {
    return entity?.toString()?.ifEmpty { fieldID.name } ?: ""
}