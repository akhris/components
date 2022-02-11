package domain.entities.fieldsmappers

import com.akhris.domain.core.entities.IEntity

sealed class EntityField(
    open val description: String
) {
    abstract val fieldID: EntityFieldID

    data class StringField(
        override val fieldID: EntityFieldID,
        override val description: String,
        val value: String
    ) :
        EntityField(description)

    data class URLField(
        override val fieldID: EntityFieldID,
        override val description: String,
        val url: String
    ) :
        EntityField(description)

    data class FloatField(
        override val fieldID: EntityFieldID,
        override val description: String,
        val value: Float
    ) :
        EntityField(description)

    //non-changeable field like ID
    data class CaptionField(
        override val fieldID: EntityFieldID,
        override val description: String,
        val caption: String
    ) :
        EntityField(description)

    data class BooleanField(
        override val fieldID: EntityFieldID,
        override val description: String,
        val value: Boolean
    ) : EntityField(description)

    data class FavoriteField(
        override val fieldID: EntityFieldID,
        override val description: String = "",
        val isFavorite: Boolean
    ) : EntityField(description)

    data class EntityLink(
        override val fieldID: EntityFieldID,
        override val description: String,
        val entity: IEntity<out Any>?
    ) : EntityField(description)

    data class EntityLinksList(
        override val fieldID: EntityFieldID,
        override val description: String,
        val entities: List<EntityLink>
    ) : EntityField(description)
}