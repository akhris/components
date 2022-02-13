package domain.entities.fieldsmappers

import domain.entities.Item
import domain.entities.ObjectType
import domain.entities.Value
import utils.replace

class ItemFieldsMapper : BaseFieldsMapper<Item>() {

    private val tag_const_part = "tag_value_"

    override fun getFields(entity: Item): Map<EntityFieldID, Any?> {
        return listOf(
            EntityFieldID.StringID(tag = "tag_name", name = "name") to entity.name,
            EntityFieldID.EntityID(tag = "tag_type", name = "object type") to entity.type
        ).plus(entity.values.map { v ->
            EntityFieldID.EntityID(tag = "$tag_const_part${v.parameter.id}", name = v.parameter.name) to v
        }).toMap()
    }


    override fun mapIntoEntity(entity: Item, field: EntityField): Item {
        return when (val fieldID = field.fieldID) {
            is EntityFieldID.StringID -> entity.copy(name = (field as EntityField.StringField).value)
            is EntityFieldID.EntityID -> {
                when (fieldID.tag) {
                    "tag_type" -> entity.copy(type = ((field as EntityField.EntityLink).entity as ObjectType))
                    else -> setValue(entity, field)
                } ?: throw IllegalArgumentException("unknown tag ${fieldID.tag} for entity: $entity")
            }
            else -> throw IllegalArgumentException("field with column: $fieldID was not found in entity: $entity")
        }
    }

    private fun setValue(item: Item, field: EntityField): Item? {
        val fieldID = field.fieldID
        val valueParameterID = fieldID.tag.substring(startIndex = tag_const_part.length)

        return item.copy(
            values = item.values.replace((field as EntityField.EntityLink).entity as Value) {
                it.parameter.id == valueParameterID
            }
        )
    }

}


//    override fun mapFields(entity: Any): List<EntityField> {
//        return when (entity) {
//            is Item -> mapItemFields(entity)
//            else -> throw IllegalArgumentException("$this cannot map $entity fields, use another mapper")
//        }
//    }

//    fun getItemColumns(item: Item): List<EntityColumn> {
//        val entityParams = listOfNotNull(
//            EntityColumn.NameColumn,
//            item.type?.let { EntityColumn.ObjectTypeColumn }
//        )
//
//        val valueParams = item.values.map {
//            EntityColumn.ValueColumn(valueID = it.id, it.parameter.id, it.parameter.name)
//        }
//
//        return entityParams.plus(valueParams)
//    }

//    fun getFieldByColumn(item: Item, column: EntityColumn): EntityField? {
//        return when (column) {
//            EntityColumn.NameColumn -> EntityField.StringField(
//                tag = column.tag,
//                name = column.name,
//                description = "item's name",
//                value = item.name
//            )
//            EntityColumn.ObjectTypeColumn -> {
//                EntityField.EntityLink(
//                    tag = column.tag,
//                    name = column.name,
//                    description = "item's object type",
//                    entity = item.type
//                )
//            }
//            is EntityColumn.ValueColumn -> {
//                val value = item.values.find {
//                    it.id == column.valueID
//                }
//                val descr = StringBuilder(value?.parameter?.name ?: "")
//                value?.parameter?.unit?.let {
//                    descr.append(", ${it.unit}")
//                }
//                EntityField.EntityLink(
//                    tag = column.tag,
//                    name = value?.value ?: "",
//                    description = descr.toString(),
//                    entity = value
//                )
//            }
//        }
//    }
//
//    private fun mapItemFields(item: Item): List<EntityField> {
//        return listOfNotNull(
//            EntityField.StringField(
//                tag = "item_field_name",
//                name = "name",
//                description = "item's name",
//                value = item.name
//            ),
//            EntityField.EntityLink(
//                tag = "item_field_object_type",
//                name = item.type?.name ?: "type",
//                description = "item's object type",
//                entity = item.type
//            ),
//            EntityField.EntityLinksList(
//                tag = "item_field_values",
//                name = "values",
//                description = "values of the item",
//                entities = item.values.mapIndexed { i, p ->
//                    val descr = StringBuilder(p.parameter.name)
//                    p.parameter.unit?.let {
//                        descr.append(", ${it.unit}")
//                    }
//                    EntityField.EntityLink(
//                        tag = "item_field_value_link$i",
//                        name = p.value,
//                        description = descr.toString(),
//                        entity = p
//                    )
//                }
//            ),
//            EntityField.CaptionField(
//                tag = "item_field_id",
//                name = "UUID",
//                description = "unique id of the item",
//                caption = item.id
//            )
//        )
//    }
