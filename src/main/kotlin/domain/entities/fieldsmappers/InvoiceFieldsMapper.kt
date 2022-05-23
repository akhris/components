package domain.entities.fieldsmappers

import domain.entities.Invoice
import domain.entities.Supplier

class InvoiceFieldsMapper : IFieldsMapper<Invoice> {

    override fun getEntityIDs(): List<EntityFieldID> = listOf(
        EntityFieldID.StringID(tag = tag_order_id, name = "order id"),
        EntityFieldID.EntityID(name = "supplier"),
        EntityFieldID.StringID(tag = tag_receiver, name = "receiver"),
        EntityFieldID.DateTimeID(name = "date"),
        EntityFieldID.FloatID(name = "total price"),
        EntityFieldID.StringID(tag = tag_currency, name = "currency")
    )

    override fun getFieldByID(entity: Invoice, fieldID: EntityFieldID): EntityField {
        return when (fieldID) {
            is EntityFieldID.StringID -> when (val tag = fieldID.tag) {
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
                else -> {
                    throw IllegalArgumentException("fieldID $fieldID must have a tag")
                }
            }
            is EntityFieldID.EntityID -> EntityField.EntityLink.EntityLinkSimple(
                fieldID = fieldID,
                entity = entity.supplier,
                entityClass = Supplier::class,
                description = "supplier"
            )
            is EntityFieldID.DateTimeID -> EntityField.DateTimeField(
                fieldID = fieldID,
                value = entity.dateTime,
                description = "invoice date"
            )
            is EntityFieldID.FloatID -> EntityField.FloatField(
                fieldID = fieldID,
                value = entity.totalPrice,
                description = "total price"
            )
            else -> throw IllegalArgumentException("fieldID $fieldID is not found in $this")
        }
    }

    override fun mapIntoEntity(entity: Invoice, field: EntityField): Invoice {
        return when (val fieldID = field.fieldID) {
            is EntityFieldID.StringID -> when (fieldID.tag) {
                tag_order_id -> entity.copy(orderID = (field as? EntityField.StringField)?.value ?: "")
                tag_receiver -> entity.copy(receiver = (field as? EntityField.StringField)?.value ?: "")
                tag_currency -> entity.copy(currency = (field as? EntityField.StringField)?.value ?: "")
                else -> throw IllegalArgumentException("fieldID $fieldID must have a tag")
            }
            is EntityFieldID.EntityID -> entity.copy(supplier = (field as? EntityField.EntityLink)?.entity as? Supplier)
            is EntityFieldID.DateTimeID -> entity.copy(dateTime = (field as? EntityField.DateTimeField)?.value)
            is EntityFieldID.FloatID -> entity.copy(totalPrice = (field as? EntityField.FloatField)?.value ?: 0f)
            else -> throw IllegalArgumentException("fieldID $fieldID is not found in $this")
        }
    }

    companion object {
        const val tag_order_id = "tag_order_id"
        const val tag_receiver = "tag_receiver"
        const val tag_currency = "tag_currency"
    }
}