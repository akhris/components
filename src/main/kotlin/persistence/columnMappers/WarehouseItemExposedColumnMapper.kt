package persistence.columnMappers

import domain.entities.WarehouseItem
import domain.entities.fieldsmappers.EntityFieldID
import domain.entities.fieldsmappers.IDBColumnMapper
import org.jetbrains.exposed.sql.Column

class WarehouseItemExposedColumnMapper : IDBColumnMapper<WarehouseItem> {
    override fun getColumn(fieldID: EntityFieldID): Column<Any>? {
        return null
//        return when (fieldID) {
//            is EntityFieldID.EntityID -> {
//                when (fieldID.tag) {
//                    WarehouseItemFieldsMapper.tag_container -> null
//                    WarehouseItemFieldsMapper.tag_item -> null
//                    else -> null
//                }
//            }
//            else -> null
//        } as? Column<Any>
    }
}