package persistence.columnMappers

import domain.entities.WarehouseItem
import domain.entities.fieldsmappers.EntityFieldID
import domain.entities.fieldsmappers.IDBColumnMapper
import domain.entities.fieldsmappers.WarehouseItemFieldsMapper

class WarehouseItemExposedColumnMapper : IDBColumnMapper<WarehouseItem> {
    override fun getColumnName(fieldID: EntityFieldID): String? {
        return when (fieldID) {
            is EntityFieldID.EntityID -> {
                when (fieldID.tag) {
                    WarehouseItemFieldsMapper.tag_container -> null
                    WarehouseItemFieldsMapper.tag_item -> null
                    else -> null
                }
            }
            else -> null
        }
    }
}