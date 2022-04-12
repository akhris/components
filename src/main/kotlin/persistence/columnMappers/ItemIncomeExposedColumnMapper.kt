package persistence.columnMappers

import domain.entities.ItemIncome
import domain.entities.fieldsmappers.EntityFieldID
import domain.entities.fieldsmappers.IDBColumnMapper
import domain.entities.fieldsmappers.ItemIncomeFieldsMapper
import persistence.dto.exposed.Tables

class ItemIncomeExposedColumnMapper : IDBColumnMapper<ItemIncome> {
    override fun getColumnName(fieldID: EntityFieldID): String? {
        return when (fieldID) {
            is EntityFieldID.EntityID -> {
                when (fieldID.tag) {
                    ItemIncomeFieldsMapper.tag_container -> Tables.ItemIncomes.container.name
                    ItemIncomeFieldsMapper.tag_supplier -> Tables.ItemIncomes.supplier.name
                    ItemIncomeFieldsMapper.tag_item -> Tables.ItemIncomes.item.name
                    else -> null
                }
            }
            is EntityFieldID.DateTimeID -> Tables.ItemIncomes.dateTime.name
            else -> null
        }
    }
}