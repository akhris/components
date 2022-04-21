package persistence.columnMappers

import domain.entities.ItemIncome
import domain.entities.fieldsmappers.EntityFieldID
import domain.entities.fieldsmappers.IDBColumnMapper
import domain.entities.fieldsmappers.ItemIncomeFieldsMapper
import org.jetbrains.exposed.sql.Column
import persistence.dto.exposed.Tables

class ItemIncomeExposedColumnMapper : IDBColumnMapper<ItemIncome> {
    override fun getColumn(fieldID: EntityFieldID): Column<Any>? {
        return when (fieldID) {
            is EntityFieldID.EntityID -> {
                when (fieldID.tag) {
                    ItemIncomeFieldsMapper.tag_container -> Tables.ItemIncomes.container
                    ItemIncomeFieldsMapper.tag_supplier -> Tables.ItemIncomes.supplier
                    ItemIncomeFieldsMapper.tag_item -> Tables.ItemIncomes.item
                    ItemIncomeFieldsMapper.tag_invoice -> Tables.ItemIncomes.invoice
                    else -> null
                }
            }
            is EntityFieldID.DateTimeID -> Tables.ItemIncomes.dateTime
            else -> null
        } as? Column<Any>
    }
}