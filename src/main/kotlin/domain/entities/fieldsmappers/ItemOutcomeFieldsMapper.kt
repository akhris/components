package domain.entities.fieldsmappers

import domain.entities.Container
import domain.entities.EntityCountable
import domain.entities.Item
import domain.entities.ItemOutcome

class ItemOutcomeFieldsMapper : BaseFieldsMapper<ItemOutcome>() {

    override fun getEntityIDs(): List<EntityFieldID> {
        return listOf(
            EntityFieldID.EntityID(tag = Companion.tag_item, name = "item", entityClass = Item::class),
            EntityFieldID.EntityID(tag = Companion.tag_container, name = "container", entityClass = Container::class),
//            EntityFieldID.LongID(tag = tag_quantity, name = "quantity"),
            EntityFieldID.DateTimeID(tag = Companion.tag_date_time, name = "date")
        )
    }

    override fun getFieldParamsByFieldID(entity: ItemOutcome, fieldID: EntityFieldID): DescriptiveFieldValue {
        return when (fieldID) {
            is EntityFieldID.EntityID -> {
                when (fieldID.tag) {
                    Companion.tag_item -> DescriptiveFieldValue.CountableField(
                        entity.item?.entity,
                        description = "item that came",
                        count = entity.item?.count
                    )
                    Companion.tag_container -> DescriptiveFieldValue.CommonField(
                        entity.container,
                        description = "container where item was put"
                    )
                    else -> throw IllegalArgumentException("field with tag: ${fieldID.tag} was not found in entity: $entity")
                }
            }
//            is EntityFieldID.LongID -> DescriptiveFieldValue(entity.quantity, description = "quantity of items")
            is EntityFieldID.DateTimeID -> DescriptiveFieldValue.CommonField(entity.dateTime, description = "when items came")
            else -> throw IllegalArgumentException("field with id: $fieldID was not found in entity: $entity")
        }
    }

    override fun mapIntoEntity(entity: ItemOutcome, field: EntityField): ItemOutcome {
        return when (val fieldID = field.fieldID) {
            is EntityFieldID.EntityID -> {
                when (fieldID.tag) {
                    Companion.tag_item -> {
                        val item = (field as? EntityField.EntityLink.EntityLinkCountable)?.entity as? Item
                        val count = (field as? EntityField.EntityLink.EntityLinkCountable)?.count ?: 0L
                        entity.copy(item = item?.let { EntityCountable(it, count) })
                    }
                    Companion.tag_container -> entity.copy(container = (field as? EntityField.EntityLink)?.entity as? Container)
                    else -> throw IllegalArgumentException("field with tag: ${fieldID.tag} was not found in entity: $entity")
                }
            }
//            is EntityFieldID.LongID -> entity.copy(quantity = (field as? EntityField.LongField)?.value ?: 0L)
            is EntityFieldID.DateTimeID -> entity.copy(dateTime = (field as? EntityField.DateTimeField)?.value)
            else -> throw IllegalArgumentException("field with id: $fieldID was not found in entity: $entity")
        }
    }

    companion object {
        const val tag_item = "tag_item"
        const val tag_container = "tag_container"
        const val tag_date_time = "tag_date_time"
    }
}