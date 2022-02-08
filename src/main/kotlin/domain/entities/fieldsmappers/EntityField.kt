package domain.entities.fieldsmappers

import com.akhris.domain.core.entities.IEntity

sealed class EntityField(
    open val tag: String,
    open val name: String,
    open val description: String
) {
    data class StringField(
        override val tag: String,
        override val name: String,
        override val description: String,
        val value: String
    ) :
        EntityField(tag, name, description)

    data class FloatField(
        override val tag: String,
        override val name: String,
        override val description: String,
        val value: Float
    ) :
        EntityField(tag, name, description)

    //non-changeable field like ID
    data class CaptionField(
        override val tag: String,
        override val name: String,
        override val description: String,
        val caption: String
    ) :
        EntityField(tag, name, description)

    data class BooleanField(
        override val tag: String,
        override val name: String,
        override val description: String,
        val value: Boolean
    ) : EntityField(tag, name, description)

    data class EntityLink(
        override val tag: String,
        override val name: String,
        override val description: String,
        val entity: IEntity<out Any>?
    ) : EntityField(tag, name, description)

    data class EntityLinksList(
        override val tag: String,
        override val name: String,
        override val description: String,
        val entities: List<EntityLink>
    ) : EntityField(tag, name, description)
}