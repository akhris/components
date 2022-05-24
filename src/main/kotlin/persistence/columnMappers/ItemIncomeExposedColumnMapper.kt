package persistence.columnMappers

import domain.entities.ItemIncome
import domain.entities.fieldsmappers.EntityFieldID
import domain.entities.fieldsmappers.IDBColumnMapper
import domain.entities.fieldsmappers.ItemIncomeFieldsMapper
import org.jetbrains.exposed.sql.Column
import persistence.dto.exposed.Tables

class ItemIncomeExposedColumnMapper : IDBColumnMapper<ItemIncome> {
    override fun getColumn(fieldID: EntityFieldID): IDBColumnMapper.Result? {
        val column = when (fieldID.tag) {
            ItemIncomeFieldsMapper.tag_container -> Tables.ItemIncomes.container
            ItemIncomeFieldsMapper.tag_supplier -> Tables.ItemIncomes.supplier
            ItemIncomeFieldsMapper.tag_item -> Tables.ItemIncomes.item
            ItemIncomeFieldsMapper.tag_invoice -> Tables.ItemIncomes.invoice
            EntityFieldID.tag_date_time -> Tables.ItemIncomes.dateTime
            else -> null
        } as? Column<Any?>

        return column?.let { IDBColumnMapper.Result(column = column) }

    }
}