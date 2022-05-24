package persistence.columnMappers

import domain.entities.Supplier
import domain.entities.fieldsmappers.EntityFieldID
import domain.entities.fieldsmappers.IDBColumnMapper
import domain.entities.fieldsmappers.SupplierFieldsMapper
import org.jetbrains.exposed.sql.Column
import persistence.dto.exposed.Tables

class SupplierExposedColumnMapper : IDBColumnMapper<Supplier> {
    override fun getColumn(fieldID: EntityFieldID): IDBColumnMapper.Result? {
        val column = when (fieldID.tag) {
            EntityFieldID.tag_name -> Tables.Suppliers.name
            EntityFieldID.tag_description -> Tables.Suppliers.description
            SupplierFieldsMapper.tag_url -> Tables.Suppliers.url
            SupplierFieldsMapper.tag_favorite -> Tables.Suppliers.isFavorite
            else -> null
        } as? Column<Any?>

        return column?.let { IDBColumnMapper.Result(column = column) }

    }
}