package domain.entities.fieldsmappers

import domain.entities.Container
import domain.entities.EntityCountable
import domain.entities.Item
import domain.entities.WarehouseItem

class WarehouseItemFieldsMapper : BaseFieldsMapper<WarehouseItem>() {

    override fun getEntityIDs(): List<EntityFieldID> {
        return listOf(
            EntityFieldID.EntityID(tag = Companion.tag_item, name = "item", entityClass = Item::class, isReadOnly = true),
            EntityFieldID.EntityID(
                tag = Companion.tag_container,
                name = "container",
                entityClass = Container::class,
                isReadOnly = true
            )
        )
    }

    override fun getFieldParamsByFieldID(entity: WarehouseItem, fieldID: EntityFieldID): DescriptiveFieldValue {
        return when (fieldID) {
            is EntityFieldID.EntityID -> {
                when (fieldID.tag) {
                    Companion.tag_container -> DescriptiveFieldValue.CommonField(
                        entity.container,
                        description = "container where item was put"
                    )
                    Companion.tag_item -> DescriptiveFieldValue.CountableField(
                        entity.item?.entity,
                        description = "item in warehouse",
                        count = entity.item?.count
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
                    Companion.tag_item -> {
                        val item = (field as? EntityField.EntityLink.EntityLinkCountable)?.entity as? Item
                            ?: throw IllegalArgumentException("field with tag: ${fieldID.tag} was not found in entity: $entity")
                        val count = (field as? EntityField.EntityLink.EntityLinkCountable)?.count ?: 0L
                        entity.copy(item = EntityCountable(item, count))
                    }
                    Companion.tag_container -> entity.copy(container = (field as? EntityField.EntityLink)?.entity as? Container)
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