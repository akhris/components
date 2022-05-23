package domain.entities.fieldsmappers

import domain.entities.*

class ItemIncomeFieldsMapper : IFieldsMapper<ItemIncome> {

    override fun getEntityIDs(): List<EntityFieldID> {
        return listOf(
            EntityFieldID.EntityID(tag = tag_item, name = "item"),
            EntityFieldID.EntityID(tag = tag_container, name = "container"),
            EntityFieldID.DateTimeID(name = "date"),
            EntityFieldID.EntityID(tag = tag_supplier, name = "supplier"),
            EntityFieldID.EntityID(tag = tag_invoice, name = "invoice")
        )
    }

    override fun getFieldByID(entity: ItemIncome, fieldID: EntityFieldID): EntityField {
        return when (fieldID) {
            is EntityFieldID.EntityID -> {
                when (fieldID.tag) {
                    tag_container -> EntityField.EntityLink.EntityLinkSimple(
                        fieldID = fieldID,
                        description = "container where item was put",
                        entity = entity.container,
                        entityClass = Container::class
                    )
                    tag_supplier -> EntityField.EntityLink.EntityLinkSimple(
                        fieldID = fieldID,
                        description = "where items came from",
                        entity = entity.supplier,
                        entityClass = Supplier::class
                    )
                    tag_item -> EntityField.EntityLink.EntityLinkCountable(
                        fieldID = fieldID,
                        entity = entity.item?.entity,
                        count = entity.item?.count,
                        description = "item that came",
                        entityClass = Item::class
                    )
                    tag_invoice -> EntityField.EntityLink.EntityLinkSimple(
                        fieldID = fieldID,
                        description = "invoice",
                        entity = entity.invoice,
                        entityClass = Invoice::class
                    )
                    else -> throw IllegalArgumentException("field with tag: ${fieldID.tag} was not found in entity: $entity")
                }
            }
            is EntityFieldID.DateTimeID -> EntityField.DateTimeField(
                fieldID = fieldID,
                value = entity.dateTime,
                description = "when items came"
            )
            else -> throw IllegalArgumentException("field with id: $fieldID was not found in entity: $entity")
        }
    }

    override fun mapIntoEntity(entity: ItemIncome, field: EntityField): ItemIncome {
        return when (val fieldID = field.fieldID) {
            is EntityFieldID.EntityID -> {
                when (fieldID.tag) {
                    tag_item -> {
                        val item = (field as? EntityField.EntityLink)?.entity as? Item
                        val count = (field as? EntityField.EntityLink.EntityLinkCountable)?.count ?: 0L
                        entity.copy(item = item?.let { EntityCountable(it, count) })
                    }
                    tag_container -> entity.copy(container = (field as? EntityField.EntityLink)?.entity as? Container)
                    tag_supplier -> entity.copy(supplier = (field as? EntityField.EntityLink)?.entity as? Supplier)
                    tag_invoice -> entity.copy(invoice = (field as? EntityField.EntityLink)?.entity as? Invoice)
                    else -> throw IllegalArgumentException("field with tag: ${fieldID.tag} was not found in entity: $entity")
                }
            }
            is EntityFieldID.DateTimeID -> entity.copy(dateTime = (field as? EntityField.DateTimeField)?.value)
            else -> throw IllegalArgumentException("field with id: $fieldID was not found in entity: $entity")
        }
    }

    companion object {
        const val tag_item = "tag_item"
        const val tag_container = "tag_container"
        const val tag_supplier = "tag_supplier"
        const val tag_invoice = "tag_invoice"
    }
}