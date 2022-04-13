package persistence.columnMappers

import domain.entities.Item
import domain.entities.fieldsmappers.EntityFieldID
import domain.entities.fieldsmappers.IDBColumnMapper
import domain.entities.fieldsmappers.ItemFieldsMapper
import org.jetbrains.exposed.sql.Column
import persistence.dto.exposed.Tables

class ItemExposedColumnMapper : IDBColumnMapper<Item> {
    override fun getColumn(fieldID: EntityFieldID): Column<Any>? {
        return when (fieldID) {
            is EntityFieldID.EntityID -> {

                when (fieldID.tag) {
                    ItemFieldsMapper.tag_type -> Tables.Items.type
                    else -> null
                }
            }
            is EntityFieldID.EntitiesListID -> null
            is EntityFieldID.StringID -> Tables.Items.name
            else -> null
        } as? Column<Any>
    }
}