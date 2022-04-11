package domain.entities.fieldsmappers

import domain.entities.*

class ItemIncomeFieldsMapper : BaseFieldsMapper<ItemIncome>() {

    override fun getEntityIDs(): List<EntityFieldID> {
        return listOf(
            EntityFieldID.EntityID(tag = Companion.tag_item, name = "item", entityClass = Item::class),
            EntityFieldID.EntityID(tag = Companion.tag_container, name = "container", entityClass = Container::class),
//            EntityFieldID.LongID(tag = tag_quantity, name = "quantity"),
            EntityFieldID.DateTimeID(tag = Companion.tag_date_time, name = "date"),
            EntityFieldID.EntityID(tag = Companion.tag_supplier, name = "supplier", entityClass = Supplier::class)
        )
    }

    override fun getFieldParamsByFieldID(entity: ItemIncome, fieldID: EntityFieldID): DescriptiveFieldValue {
        return when (fieldID) {
            is EntityFieldID.EntityID -> {
                when (fieldID.tag) {
                    Companion.tag_container -> DescriptiveFieldValue.CommonField(
                        entity.container,
                        description = "container where item was put"
                    )
                    Companion.tag_supplier -> DescriptiveFieldValue.CommonField(entity.supplier, description = "where items came from")
                    Companion.tag_item -> DescriptiveFieldValue.CountableField(
                        entity.item?.entity,
                        description = "item that came",
                        count = entity.item?.count
                    )
                    else -> throw IllegalArgumentException("field with tag: ${fieldID.tag} was not found in entity: $entity")
                }
            }
            is EntityFieldID.DateTimeID -> DescriptiveFieldValue.CommonField(entity.dateTime, description = "when items came")
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
       const val tag_date_time = "tag_date_time"
       const val tag_supplier = "tag_supplier"
    }
}