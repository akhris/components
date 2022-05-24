package domain.entities.fieldsmappers

/**
 * FieldID - descriptive identification of the entity's field without actual data value.
 */
data class EntityFieldID(val tag: String? = null, val name: String = "", val isReadOnly: Boolean = false) {

    companion object {
        const val tag_name = "tag_name"
        const val tag_description = "tag_description"
        const val tag_entity_id = "tag_entity_id"
        const val tag_date_time = "tag_date_time"
    }
}