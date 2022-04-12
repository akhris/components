package persistence.columnMappers

import domain.entities.Item
import domain.entities.fieldsmappers.EntityFieldID
import domain.entities.fieldsmappers.IDBColumnMapper
import domain.entities.fieldsmappers.ItemFieldsMapper
import persistence.dto.exposed.Tables

class ItemExposedColumnMapper : IDBColumnMapper<Item> {
    override fun getColumnName(fieldID: EntityFieldID): String? {
        return when (fieldID) {
            is EntityFieldID.EntityID -> when (fieldID.tag) {
                ItemFieldsMapper.tag_type -> Tables.Items.type.name
                else -> null
            }
            is EntityFieldID.EntitiesListID -> null
            is EntityFieldID.StringID -> Tables.Items.name.name
            else -> null
        }
    }
}