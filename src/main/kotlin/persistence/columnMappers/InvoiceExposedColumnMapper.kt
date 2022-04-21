package persistence.columnMappers

import domain.entities.Invoice
import domain.entities.fieldsmappers.EntityFieldID
import domain.entities.fieldsmappers.IDBColumnMapper
import domain.entities.fieldsmappers.InvoiceFieldsMapper
import org.jetbrains.exposed.sql.Column
import persistence.dto.exposed.Tables

class InvoiceExposedColumnMapper: IDBColumnMapper<Invoice> {
    override fun getColumn(fieldID: EntityFieldID): Column<Any>? {
        return when (fieldID) {
            is EntityFieldID.EntityID -> Tables.Invoices.supplier
            is EntityFieldID.DateTimeID -> Tables.Invoices.dateTime
            is EntityFieldID.FloatID -> Tables.Invoices.totalPrice
            is EntityFieldID.StringID -> when(fieldID.tag){
                InvoiceFieldsMapper.tag_currency->Tables.Invoices.currency
                InvoiceFieldsMapper.tag_receiver->Tables.Invoices.receiver
                InvoiceFieldsMapper.tag_order_id->Tables.Invoices.orderID
                else->null
            }
            else -> null
        } as? Column<Any>
    }
}