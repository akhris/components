package persistence.columnMappers

import domain.entities.ItemOutcome
import domain.entities.fieldsmappers.EntityFieldID
import domain.entities.fieldsmappers.IDBColumnMapper
import domain.entities.fieldsmappers.ItemOutcomeFieldsMapper
import persistence.dto.exposed.Tables

class ItemOutcomeExposedColumnMapper : IDBColumnMapper<ItemOutcome> {
    override fun getColumnName(fieldID: EntityFieldID): String? {
        return when (fieldID) {
            is EntityFieldID.EntityID -> {
                when (fieldID.tag) {
                    ItemOutcomeFieldsMapper.tag_item -> Tables.ItemOutcomes.item.name
                    ItemOutcomeFieldsMapper.tag_container -> Tables.ItemOutcomes.container.name
                    else -> null
                }
            }
            is EntityFieldID.DateTimeID -> Tables.ItemOutcomes.dateTime.name
            else -> null
        }
    }
}