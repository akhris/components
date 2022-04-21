package persistence.datasources.exposed

import domain.entities.Invoice
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import persistence.columnMappers.ColumnMappersFactory
import persistence.dto.exposed.EntityInvoice
import persistence.dto.exposed.Tables
import persistence.mappers.toInvoice
import utils.set
import utils.toUUID

class InvoicesDao(columnMappersFactory: ColumnMappersFactory) : BaseUUIDDao<Invoice, EntityInvoice, Tables.Invoices>(
    table = Tables.Invoices,
    entityClass = EntityInvoice,
    columnMapper = columnMappersFactory.getColumnMapper(Invoice::class)
) {

    override fun mapToEntity(exposedEntity: EntityInvoice): Invoice = exposedEntity.toInvoice()


    override fun insertStatement(entity: Invoice): Tables.Invoices.(InsertStatement<Number>) -> Unit = {
        it[orderID] = entity.orderID
        it[supplier] = entity.supplier?.id?.toUUID()
        it[receiver] = entity.receiver
        it[dateTime] = entity.dateTime
        it[totalPrice] = entity.totalPrice
        it[currency] = entity.currency
    }

    override fun updateStatement(entity: Invoice): Tables.Invoices.(UpdateStatement) -> Unit = {
        it[orderID] = entity.orderID
        it[supplier] = entity.supplier?.id?.toUUID()
        it[receiver] = entity.receiver
        it[dateTime] = entity.dateTime
        it[totalPrice] = entity.totalPrice
        it[currency] = entity.currency
    }

}