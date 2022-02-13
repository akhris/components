package domain.entities.fieldsmappers

// TODO: 13.02.2022 Add EntityListID to handle List<IEntity> fields
sealed class EntityFieldID {

    abstract val name: String
    abstract val tag: String

    data class EntityID(override val tag: String, override val name: String) : EntityFieldID()
    data class StringID(override val tag: String, override val name: String) : EntityFieldID()
    data class FloatID(override val tag: String, override val name: String) : EntityFieldID()
    data class BooleanID(override val tag: String, override val name: String) : EntityFieldID()
}