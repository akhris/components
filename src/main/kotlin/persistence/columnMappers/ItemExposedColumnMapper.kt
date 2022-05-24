package persistence.columnMappers

import domain.entities.Item
import domain.entities.fieldsmappers.EntityFieldID
import domain.entities.fieldsmappers.IDBColumnMapper
import domain.entities.fieldsmappers.ItemFieldsMapper
import org.jetbrains.exposed.sql.Column
import persistence.dto.exposed.Tables

class ItemExposedColumnMapper : IDBColumnMapper<Item> {
    override fun getColumn(fieldID: EntityFieldID): IDBColumnMapper.Result? {
        val column = when (fieldID.tag) {
            ItemFieldsMapper.tag_type -> Tables.Items.type
            ItemFieldsMapper.tag_values -> null
            EntityFieldID.tag_name -> Tables.Items.name
            else -> null
        } as? Column<Any?>
        return column?.let { IDBColumnMapper.Result(column = column) }
    }
}