package domain.entities.fieldsmappers

import domain.entities.Container
import domain.entities.EntityCountable
import domain.entities.Item
import domain.entities.ItemOutcome

class ItemOutcomeFieldsMapper : IFieldsMapper<ItemOutcome> {

    override fun getEntityIDs(): List<EntityFieldID> {
        return listOf(
            EntityFieldID(tag = tag_item, name = "item"),
            EntityFieldID(tag = tag_container, name = "container"),
            EntityFieldID(tag = EntityFieldID.tag_date_time, name = "date")
        )
    }

    override fun getFieldByID(entity: ItemOutcome, fieldID: EntityFieldID): EntityField {
        return when (fieldID.tag) {
            tag_item -> EntityField.EntityLink.EntityLinkCountable(
                fieldID = fieldID,
                entity = entity.item?.entity,
                entityClass = Item::class,
                count = entity.item?.count,
                description = "item that came"
            )
            tag_container -> EntityField.EntityLink.EntityLinkSimple(
                fieldID = fieldID,
                entity = entity.container,
                entityClass = Container::class,
                description = "container where item was put"
            )
            EntityFieldID.tag_date_time -> EntityField.DateTimeField(
                fieldID = fieldID,
                value = entity.dateTime,
                description = "when items came"
            )
            else -> throw IllegalArgumentException("field with id: $fieldID was not found in entity: $entity")
        }
    }

    override fun mapIntoEntity(entity: ItemOutcome, field: EntityField): ItemOutcome {
        return when (field) {
            is EntityField.DateTimeField -> entity.copy(dateTime = (field as? EntityField.DateTimeField)?.value)
            is EntityField.EntityLink -> {
                when (field.fieldID.tag) {
                    tag_item -> {
                        val item = (field as? EntityField.EntityLink.EntityLinkCountable)?.entity as? Item
                        val count = (field as? EntityField.EntityLink.EntityLinkCountable)?.count ?: 0L
                        entity.copy(item = item?.let { EntityCountable(it, count) })
                    }
                    tag_container -> entity.copy(container = (field as? EntityField.EntityLink)?.entity as? Container)
                    else -> throw IllegalArgumentException("field with tag: ${field.fieldID.tag} was not found in entity: $entity")
                }
            }
            else -> throw IllegalArgumentException("field with id: ${field.fieldID} was not found in entity: $entity")
        }
    }

    companion object {
        const val tag_item = "tag_item"
        const val tag_container = "tag_container"
    }
}