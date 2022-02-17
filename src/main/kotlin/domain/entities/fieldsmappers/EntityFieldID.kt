package domain.entities.fieldsmappers

import com.akhris.domain.core.entities.IEntity
import kotlin.reflect.KClass


sealed class EntityFieldID {

    abstract val name: String
    abstract val tag: String

    data class EntityID constructor(
        override val tag: String,
        override val name: String,
        val entityClass: KClass<out IEntity<*>>
    ) :
        EntityFieldID()

//    data class EntityCountableID(
//        override val tag: String,
//        override val name: String
//    ) :
//        EntityID(tag, name)

    data class EntitiesListID(
        override val tag: String,
        override val name: String,
        val entitiesIDs: List<EntityID> = listOf(),
        val entityClass: KClass<out IEntity<*>>
    ) : EntityFieldID()

    data class StringID(
        override val tag: String,
        override val name: String
    ) :
        EntityFieldID()

    data class DateTimeID(
        override val tag: String,
        override val name: String
    ) :
        EntityFieldID()

    data class FloatID(
        override val tag: String,
        override val name: String
    ) :
        EntityFieldID()

    data class LongID(
        override val tag: String,
        override val name: String
    ) :
        EntityFieldID()

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