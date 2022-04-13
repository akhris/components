package persistence.columnMappers

import domain.entities.Supplier
import domain.entities.fieldsmappers.EntityFieldID
import domain.entities.fieldsmappers.IDBColumnMapper
import domain.entities.fieldsmappers.SupplierFieldsMapper
import org.jetbrains.exposed.sql.Column
import persistence.dto.exposed.Tables

class SupplierExposedColumnMapper : IDBColumnMapper<Supplier> {
    override fun getColumn(fieldID: EntityFieldID): Column<Any>? {
        return when (fieldID) {
            is EntityFieldID.StringID -> {
                when (fieldID.tag) {
                    EntityFieldID.tag_name -> Tables.Suppliers.name
                    EntityFieldID.tag_description -> Tables.Suppliers.description
                    SupplierFieldsMapper.tag_url -> Tables.Suppliers.url
                    else -> null
                }
            }
            is EntityFieldID.BooleanID -> Tables.Suppliers.isFavorite
            else -> null
        } as? Column<Any>
    }
}