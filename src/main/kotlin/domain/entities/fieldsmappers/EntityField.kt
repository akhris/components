package domain.entities.fieldsmappers

import com.akhris.domain.core.entities.IEntity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

sealed class EntityField {
    abstract val fieldID: EntityFieldID
    abstract val description: String

    data class StringField(
        override val fieldID: EntityFieldID,
        override val description: String,
        val value: String
    ) :
        EntityField() {
        override fun toString(): String = value
    }

    data class FloatField(
        override val fieldID: EntityFieldID,
        override val description: String,
        val value: Float
    ) :
        EntityField() {
        override fun toString(): String = value.toString()
    }

    data class BooleanField(
        override val fieldID: EntityFieldID,
        override val description: String,
        val value: Boolean
    ) : EntityField() {
        override fun toString(): String = value.toString()
    }


    data class LongField(
        override val fieldID: EntityFieldID.LongID,
        override val description: String,
        val value: Long
    ) : EntityField() {
        override fun toString(): String = value.toString()
    }

    data class DateTimeField(
        override val fieldID: EntityFieldID.DateTimeID,
        override val description: String,
        val value: LocalDateTime?
    ) : EntityField() {
        override fun toString(): String = value?.format(DateTimeFormatter.BASIC_ISO_DATE) ?: "no date"
    }

    data class EntityLink<T : IEntity<*>>(
        override val fieldID: EntityFieldID,
        override val description: String,
        val entity: T?,
//        val entityClass: KClass<T>,
        val count: Long? = null
    ) : EntityField() {
        override fun toString(): String = entity?.toString() ?: description
    }
//
//    data class EntityCountableLink(
//        override val fieldID: EntityFieldID,
//        override val description: String,
//        val entity: IEntity<out Any>?,
//        val count: Long
//    ) : EntityField(){
//        override fun toString(): String  = entity?.toString()?:description
//    }

    data class EntityLinksList<T : IEntity<*>>(
        override val fieldID: EntityFieldID,
        override val description: String,
        val entities: List<EntityLink<T>>
    ) : EntityField() {
        override fun toString(): String = description
    }
}

fun EntityField.EntityLink<IEntity<*>>.getName(): String {
    return entity?.toString()?.ifEmpty { fieldID.name } ?: ""
}