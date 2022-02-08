package domain.entities.fieldsmappers

import com.akhris.domain.core.entities.IEntity
import domain.entities.Item
import domain.entities.ObjectType
import domain.entities.Value

class ItemFieldsMapper : IFieldsMapper {
    override fun mapFields(entity: Any): List<EntityField> {
        return when (entity) {
            is Item -> mapItemFields(entity)
            else -> throw IllegalArgumentException("$this cannot map $entity fields, use another mapper")
        }
    }

    override fun <T : IEntity<*>> mapIntoEntity(entity: T, field: EntityField): T {
        return when (entity) {
            is Item -> mapIntoItem(entity, field) as T
            else -> throw IllegalArgumentException("$this cannot map $entity fields, use another mapper")
        }
    }


    private fun mapItemFields(item: Item): List<EntityField> {
        return listOfNotNull(
            EntityField.StringField(
                tag = "item_field_name",
                name = "name",
                description = "item's name",
                value = item.name
            ),
            EntityField.EntityLink(
                tag = "item_field_object_type",
                name = item.type?.name ?: "type",
                description = "item's object type",
                entity = item.type
            ),
            EntityField.EntityLinksList(
                tag = "item_field_values",
                name = "values",
                description = "values of the item",
                entities = item.values.mapIndexed { i, p ->
                    val descr = StringBuilder(p.parameter.name)
                    p.parameter.unit?.let {
                        descr.append(", ${it.unit}")
                    }
                    EntityField.EntityLink(
                        tag = "item_field_value_link$i",
                        name = p.value,
                        description = descr.toString(),
                        entity = p
                    )
                }
            ),
            EntityField.CaptionField(
                tag = "item_field_id",
                name = "UUID",
                description = "unique id of the item",
                caption = item.id
            )
        )
    }

    private fun mapIntoItem(item: Item, field: EntityField): Item {
        return when (field.tag) {
            "item_field_name" -> item.copy(name = (field as EntityField.StringField).value)
            "item_field_object_type" -> item.copy(type = ((field as EntityField.EntityLink).entity as ObjectType))
            "item_field_values" -> item.copy(values = (field as EntityField.EntityLinksList).entities as List<Value>)
            else -> throw IllegalArgumentException("field with tag: ${field.tag} was not found in entity: $item")
        }
    }
}