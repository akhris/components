package domain.entities.fieldsmappers

import com.akhris.domain.core.entities.IEntity
import kotlin.reflect.KClass

/**
 * FieldID - descriptive identification of the entity's field without actual data value.
 */
sealed class EntityFieldID {

    abstract val name: String
    open val tag: String? = null
    abstract val isReadOnly: Boolean

    /**
     * Field represents another Entity of class [entityClass]
     */
    data class EntityID constructor(
        override val tag: String? = null,
        override val name: String,
        override val isReadOnly: Boolean = false,
        val entityClass: KClass<out IEntity<*>>
    ) :
        EntityFieldID()

    /**
     * Field represents list of Entities of class [entityClass]
     */
    data class EntitiesListID constructor(
        override val tag: String? = null,
        override val name: String,
        override val isReadOnly: Boolean = false,
        val entityClass: KClass<out IEntity<*>>
    ) : EntityFieldID()


    /**
     * Field represents String value
     */
    data class StringID(
        override val tag: String? = null,
        override val name: String,
        override val isReadOnly: Boolean = false
    ) :
        EntityFieldID()

    /**
     * Field represents Date/Time value
     */
    data class DateTimeID(
        override val tag: String? = null,
        override val name: String,
        override val isReadOnly: Boolean = false
    ) :
        EntityFieldID()

    /**
     * Field represents Float value
     */
    data class FloatID(
        override val tag: String? = null,
        override val name: String,
        override val isReadOnly: Boolean = false
    ) :
        EntityFieldID()

    /**
     * Field represents Long value
     */
    data class LongID(
        override val tag: String? = null,
        override val name: String,
        override val isReadOnly: Boolean = false
    ) :
        EntityFieldID()

    /**
     * Field represents Boolean value
     */
    data class BooleanID(
        override val tag: String? = null,
        override val name: String,
        override val isReadOnly: Boolean = false
    ) :
        EntityFieldID()


    companion object {
        const val tag_name = "tag_name"
        const val tag_description = "tag_description"
        const val tag_entity_id = "tag_entity_id"
    }
}