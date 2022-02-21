package domain.entities.fieldsmappers

import domain.entities.Container
import domain.entities.EntityCountable
import domain.entities.Item
import domain.entities.ItemOutcome

class ItemOutcomeFieldsMapper : BaseFieldsMapper<ItemOutcome>() {

    private val tag_item = "tag_item"
    private val tag_container = "tag_container"
    private val tag_quantity = "tag_quantity"
    private val tag_date_time = "tag_date_time"

    override fun getEntityIDs(entity: ItemOutcome): List<EntityFieldID> {
        return listOf(
            EntityFieldID.EntityID(tag = tag_item, name = "item", entityClass = Item::class),
            EntityFieldID.EntityID(tag = tag_container, name = "container", entityClass = Container::class),
//            EntityFieldID.LongID(tag = tag_quantity, name = "quantity"),
            EntityFieldID.DateTimeID(tag = tag_date_time, name = "date")
        )
    }

    override fun getFieldParamsByFieldID(entity: ItemOutcome, fieldID: EntityFieldID): DescriptiveFieldValue {
        return when (fieldID) {
            is EntityFieldID.EntityID -> {
                when (fieldID.tag) {
                    tag_item -> DescriptiveFieldValue(
                        entity.item?.entity,
                        description = "item that came",
                        count = entity.item?.count
                    )
                    tag_container -> DescriptiveFieldValue(
                        entity.container,
                        description = "container where item was put"
                    )
                    else -> throw IllegalArgumentException("field with tag: ${fieldID.tag} was not found in entity: $entity")
                }
            }
//            is EntityFieldID.LongID -> DescriptiveFieldValue(entity.quantity, description = "quantity of items")
            is EntityFieldID.DateTimeID -> DescriptiveFieldValue(entity.dateTime, description = "when items came")
            else -> throw IllegalArgumentException("field with id: $fieldID was not found in entity: $entity")
        }
    }

    override fun mapIntoEntity(entity: ItemOutcome, field: EntityField): ItemOutcome {
        return when (val fieldID = field.fieldID) {
            is EntityFieldID.EntityID -> {
                when (fieldID.tag) {
                    tag_item -> {
                        val item = (field as? EntityField.EntityLink)?.entity as? Item
                            ?: throw IllegalArgumentException("field with tag: ${fieldID.tag} was not found in entity: $entity")
                        val count = (field as? EntityField.EntityLink)?.count ?: 0L
                        entity.copy(item = EntityCountable(item, count))
                    }
                    tag_container -> entity.copy(container = (field as? EntityField.EntityLink)?.entity as? Container)
                    else -> throw IllegalArgumentException("field with tag: ${fieldID.tag} was not found in entity: $entity")
                }
            }
//            is EntityFieldID.LongID -> entity.copy(quantity = (field as? EntityField.LongField)?.value ?: 0L)
            is EntityFieldID.DateTimeID -> entity.copy(dateTime = (field as? EntityField.DateTimeField)?.value)
            else -> throw IllegalArgumentException("field with id: $fieldID was not found in entity: $entity")
        }
    }
}