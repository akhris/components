package domain.entities.fieldsmappers

import domain.entities.Invoice
import domain.entities.Supplier

class InvoiceFieldsMapper : IFieldsMapper<Invoice> {

    override fun getEntityIDs(): List<EntityFieldID> = listOf(
        EntityFieldID(tag = tag_order_id, name = "order id"),
        EntityFieldID(tag = tag_supplier, name = "supplier"),
        EntityFieldID(tag = tag_receiver, name = "receiver"),
        EntityFieldID(tag = tag_date, name = "date"),
        EntityFieldID(tag = tag_total_price, name = "total price"),
        EntityFieldID(tag = tag_currency, name = "currency")
    )

    override fun getFieldByID(entity: Invoice, fieldID: EntityFieldID): EntityField {
        return when (fieldID.tag) {

            tag_order_id -> {
                EntityField.StringField(
                    fieldID = fieldID,
                    value = entity.orderID,
                    description = "order id"
                )
            }
            tag_receiver -> {
                EntityField.StringField(
                    fieldID = fieldID,
                    value = entity.receiver,
                    description = "receiver of the items"
                )
            }
            tag_currency -> {
                EntityField.StringField(
                    fieldID = fieldID,
                    value = entity.currency,
                    description = "order currency"
                )
            }

            tag_supplier -> EntityField.EntityLink.EntityLinkSimple(
                fieldID = fieldID,
                entity = entity.supplier,
                entityClass = Supplier::class,
                description = "supplier"
            )
            tag_date -> EntityField.DateTimeField(
                fieldID = fieldID,
                value = entity.dateTime,
                description = "invoice date"
            )
            tag_total_price -> EntityField.FloatField(
                fieldID = fieldID,
                value = entity.totalPrice,
                description = "total price"
            )
            else -> throw IllegalArgumentException("fieldID $fieldID is not found in $this")
        }
    }

    override fun mapIntoEntity(entity: Invoice, field: EntityField): Invoice {
        return when (field) {
            is EntityField.StringField -> when (field.fieldID.tag) {
                tag_order_id -> entity.copy(orderID = (field as? EntityField.StringField)?.value ?: "")
                tag_receiver -> entity.copy(receiver = (field as? EntityField.StringField)?.value ?: "")
                tag_currency -> entity.copy(currency = (field as? EntityField.StringField)?.value ?: "")
                else -> throw IllegalArgumentException("fieldID ${field.fieldID} must have a tag")
            }
            is EntityField.DateTimeField -> entity.copy(dateTime = field.value)
            is EntityField.EntityLink -> entity.copy(supplier = field.entity as? Supplier)
            is EntityField.FloatField -> entity.copy(totalPrice = field.value)
            else -> throw IllegalArgumentException("fieldID ${field.fieldID} is not found in $this")
        }
    }

    companion object {
        const val tag_order_id = "tag_order_id"
        const val tag_supplier = "tag_supplier"
        const val tag_receiver = "tag_receiver"
        const val tag_currency = "tag_currency"
        const val tag_date = "tag_date"
        const val tag_total_price = "tag_total_price"
    }
}