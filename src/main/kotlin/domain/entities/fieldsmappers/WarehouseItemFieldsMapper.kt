package domain.entities.fieldsmappers

import domain.entities.Container
import domain.entities.EntityCountable
import domain.entities.Item
import domain.entities.WarehouseItem

class WarehouseItemFieldsMapper : IFieldsMapper<WarehouseItem> {

    override fun getEntityIDs(): List<EntityFieldID> {
        return listOf(
            EntityFieldID.EntityID(
                tag = Companion.tag_item,
                name = "item",
                isReadOnly = true
            ),
            EntityFieldID.EntityID(
                tag = Companion.tag_container,
                name = "container",
                isReadOnly = true
            )
        )
    }

    override fun getFieldByID(entity: WarehouseItem, fieldID: EntityFieldID): EntityField {
        return when (fieldID) {
            is EntityFieldID.EntityID -> {
                when (fieldID.tag) {
                    tag_container -> EntityField.EntityLink.EntityLinkSimple(
                        fieldID = fieldID,
                        entity = entity.container,
                        entityClass = Container::class,
                        description = "container where item was put"
                    )
                    tag_item -> EntityField.EntityLink.EntityLinkCountable(
                        fieldID = fieldID,
                        entity = entity.item?.entity,
                        entityClass = Item::class,
                        count = entity.item?.count,
                        description = "item in warehouse"
                    )
                    else -> throw IllegalArgumentException("field with tag: ${fieldID.tag} was not found in entity: $entity")
                }
            }
            else -> throw IllegalArgumentException("field with id: $fieldID was not found in entity: $entity")
        }
    }

    override fun mapIntoEntity(entity: WarehouseItem, field: EntityField): WarehouseItem {
        return when (val fieldID = field.fieldID) {
            is EntityFieldID.EntityID -> {
                when (fieldID.tag) {
                    tag_item -> {
                        val item = (field as? EntityField.EntityLink.EntityLinkCountable)?.entity as? Item
                            ?: throw IllegalArgumentException("field with tag: ${fieldID.tag} was not found in entity: $entity")
                        val count = (field as? EntityField.EntityLink.EntityLinkCountable)?.count ?: 0L
                        entity.copy(item = EntityCountable(item, count))
                    }
                    tag_container -> entity.copy(container = (field as? EntityField.EntityLink)?.entity as? Container)
                    else -> throw IllegalArgumentException("field with tag: ${fieldID.tag} was not found in entity: $entity")
                }
            }
            else -> throw IllegalArgumentException("field with id: $fieldID was not found in entity: $entity")
        }
    }

    companion object {
        const val tag_container = "tag_container"
        const val tag_item = "tag_item"
    }
}