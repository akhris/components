package domain.entities.fieldsmappers

import com.akhris.domain.core.entities.IEntity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.reflect.KClass

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

    data class EntityLink constructor(
        override val fieldID: EntityFieldID,
        override val description: String,
        val entity: IEntity<*>?,
        val entityClass: KClass<out IEntity<*>>,
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

    data class EntityLinksList(
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