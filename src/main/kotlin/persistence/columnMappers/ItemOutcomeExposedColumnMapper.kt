package persistence.columnMappers

import domain.entities.ItemOutcome
import domain.entities.fieldsmappers.EntityFieldID
import domain.entities.fieldsmappers.IDBColumnMapper
import domain.entities.fieldsmappers.ItemOutcomeFieldsMapper
import org.jetbrains.exposed.sql.Column
import persistence.dto.exposed.Tables

class ItemOutcomeExposedColumnMapper : IDBColumnMapper<ItemOutcome> {
    override fun getColumn(fieldID: EntityFieldID): IDBColumnMapper.Result? {
        val column = when (fieldID) {
            is EntityFieldID.EntityID -> {
                when (fieldID.tag) {
                    ItemOutcomeFieldsMapper.tag_item -> Tables.ItemOutcomes.item
                    ItemOutcomeFieldsMapper.tag_container -> Tables.ItemOutcomes.container
                    else -> null
                }
            }
            is EntityFieldID.DateTimeID -> Tables.ItemOutcomes.dateTime
            else -> null
        } as? Column<Any?>
        return column?.let { IDBColumnMapper.Result(column = column) }
    }
}