package domain.entities.fieldsmappers

import com.akhris.domain.core.entities.IEntity
import kotlin.reflect.KClass

/**
 * FieldID - identification of the entity's field.
 */
sealed class EntityFieldID {

    abstract val name: String
    abstract val tag: String

    /**
     * Field represents another Entity of class [entityClass]
     */
    data class EntityID constructor(
        override val tag: String,
        override val name: String,
        val entityClass: KClass<out IEntity<*>>
    ) :
        EntityFieldID()

    /**
     * Field represents list of Entities of class [entityClass]
     */
    data class EntitiesListID(
        override val tag: String,
        override val name: String,
        val entitiesIDs: List<EntityID> = listOf(),
        val entityClass: KClass<out IEntity<*>>
    ) : EntityFieldID()


    /**
     * Field represents String value
     */
    data class StringID(
        override val tag: String,
        override val name: String
    ) :
        EntityFieldID()

    /**
     * Field represents Date/Time value
     */
    data class DateTimeID(
        override val tag: String,
        override val name: String
    ) :
        EntityFieldID()

    /**
     * Field represents Float value
     */
    data class FloatID(
        override val tag: String,
        override val name: String
    ) :
        EntityFieldID()

    /**
     * Field represents Long value
     */
    data class LongID(
        override val tag: String,
        override val name: String
    ) :
        EntityFieldID()

    /**
     * Field represents Boolean value
     */
    data class BooleanID(
        override val tag: String,
        override val name: String
    ) :
        EntityFieldID()


    companion object {
        const val tag_name = "tag_name"
        const val tag_description = "tag_description"
    }
}