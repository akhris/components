package persistence.columnMappers

import domain.entities.Invoice
import domain.entities.fieldsmappers.EntityFieldID
import domain.entities.fieldsmappers.IDBColumnMapper
import domain.entities.fieldsmappers.InvoiceFieldsMapper
import org.jetbrains.exposed.sql.Column
import persistence.dto.exposed.Tables

class InvoiceExposedColumnMapper : IDBColumnMapper<Invoice> {
    override fun getColumn(fieldID: EntityFieldID): IDBColumnMapper.Result? {
        val column = when (fieldID.tag) {
            InvoiceFieldsMapper.tag_supplier -> Tables.Invoices.supplier
            InvoiceFieldsMapper.tag_date -> Tables.Invoices.dateTime
            InvoiceFieldsMapper.tag_total_price -> Tables.Invoices.totalPrice
            InvoiceFieldsMapper.tag_currency -> Tables.Invoices.currency
            InvoiceFieldsMapper.tag_receiver -> Tables.Invoices.receiver
            InvoiceFieldsMapper.tag_order_id -> Tables.Invoices.orderID
            else -> null
        } as? Column<Any?>
        return column?.let { IDBColumnMapper.Result(column = column) }
    }
}