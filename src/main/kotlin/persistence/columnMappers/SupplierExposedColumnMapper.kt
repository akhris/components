package persistence.columnMappers

import domain.entities.Supplier
import domain.entities.fieldsmappers.EntityFieldID
import domain.entities.fieldsmappers.IDBColumnMapper
import domain.entities.fieldsmappers.SupplierFieldsMapper
import persistence.dto.exposed.Tables

class SupplierExposedColumnMapper : IDBColumnMapper<Supplier> {
    override fun getColumnName(fieldID: EntityFieldID): String? {
        return when (fieldID) {
            is EntityFieldID.StringID -> {
                when (fieldID.tag) {
                    EntityFieldID.tag_name -> Tables.Suppliers.name.name
                    EntityFieldID.tag_description -> Tables.Suppliers.description.name
                    SupplierFieldsMapper.tag_url -> Tables.Suppliers.url.name
                    else -> null
                }
            }
            is EntityFieldID.BooleanID -> Tables.Suppliers.isFavorite.name
            else -> null
        }
    }
}