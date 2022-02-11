package domain.entities.fieldsmappers

import com.akhris.domain.core.entities.IEntity

sealed class EntityField(
    open val description: String
) {
    abstract val fieldColumn: EntityFieldColumn

    data class StringField(
        override val fieldColumn: EntityFieldColumn,
        override val description: String,
        val value: String
    ) :
        EntityField(description)

    data class URLField(
        override val fieldColumn: EntityFieldColumn,
        override val description: String,
        val url: String
    ) :
        EntityField(description)

    data class FloatField(
        override val fieldColumn: EntityFieldColumn,
        override val description: String,
        val value: Float
    ) :
        EntityField(description)

    //non-changeable field like ID
    data class CaptionField(
        override val fieldColumn: EntityFieldColumn,
        override val description: String,
        val caption: String
    ) :
        EntityField(description)

    data class BooleanField(
        override val fieldColumn: EntityFieldColumn,
        override val description: String,
        val value: Boolean
    ) : EntityField(description)

    data class FavoriteField(
        override val fieldColumn: EntityFieldColumn,
        override val description: String = "",
        val isFavorite: Boolean
    ) : EntityField(description)

    data class EntityLink(
        override val fieldColumn: EntityFieldColumn,
        override val description: String,
        val entity: IEntity<out Any>?
    ) : EntityField(description)

    data class EntityLinksList(
        override val fieldColumn: EntityFieldColumn,
        override val description: String,
        val entities: List<EntityLink>
    ) : EntityField(description)
}