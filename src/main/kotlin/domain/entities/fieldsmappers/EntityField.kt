package domain.entities.fieldsmappers

import com.akhris.domain.core.entities.IEntity
import java.time.LocalDateTime

sealed class EntityField {
    abstract val fieldID: EntityFieldID
    abstract val description: String

    data class StringField(
        override val fieldID: EntityFieldID,
        override val description: String,
        val value: String
    ) :
        EntityField()

    data class URLField(
        override val fieldID: EntityFieldID,
        override val description: String,
        val url: String
    ) :
        EntityField()

    data class FloatField(
        override val fieldID: EntityFieldID,
        override val description: String,
        val value: Float
    ) :
        EntityField()

    //non-changeable field like ID
    data class CaptionField(
        override val fieldID: EntityFieldID,
        override val description: String,
        val caption: String
    ) :
        EntityField()

    data class BooleanField(
        override val fieldID: EntityFieldID,
        override val description: String,
        val value: Boolean
    ) : EntityField()

    data class FavoriteField(
        override val fieldID: EntityFieldID,
        override val description: String,
        val isFavorite: Boolean
    ) : EntityField()

    data class LongField(
        override val fieldID: EntityFieldID.LongID,
        override val description: String,
        val value: Long
    ) : EntityField()

    data class DateTimeField(
        override val fieldID: EntityFieldID.DateTimeID,
        override val description: String,
        val value: LocalDateTime?
    ) : EntityField()

    data class EntityLink(
        override val fieldID: EntityFieldID,
        override val description: String,
        val entity: IEntity<out Any>?
    ) : EntityField()

    data class EntityLinksList(
        override val fieldID: EntityFieldID,
        override val description: String,
        val entities: List<EntityLink>
    ) : EntityField()
}

fun EntityField.EntityLink.getName(): String {
    return entity?.toString()?.ifEmpty { fieldID.name } ?: ""
}