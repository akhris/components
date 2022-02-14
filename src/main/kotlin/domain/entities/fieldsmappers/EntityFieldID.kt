package domain.entities.fieldsmappers


sealed class EntityFieldID {

    abstract val name: String
    abstract val tag: String

    data class EntityID(
        override val tag: String,
        override val name: String
    ) :
        EntityFieldID()

    data class EntitiesListID(
        override val tag: String,
        override val name: String,
        val entitiesIDs: List<EntityID> = listOf()
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


    companion object{
        const val tag_name = "tag_name"
        const val tag_description = "tag_description"
    }
}