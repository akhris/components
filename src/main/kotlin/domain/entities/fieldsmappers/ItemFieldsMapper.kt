package domain.entities.fieldsmappers

import domain.entities.Item
import domain.entities.ObjectType
import domain.entities.Value

class ItemFieldsMapper : BaseFieldsMapper<Item>() {

    private val tag_values = "tag_values"
    private val tag_type = "tag_type"


    override fun getEntityIDs(entity: Item): List<EntityFieldID> {
        return listOf(
            EntityFieldID.StringID(tag = "tag_name", name = "name"),
            EntityFieldID.EntityID(tag = tag_type, name = "object type"),
            EntityFieldID.EntitiesListID(
                tag = tag_values,
                name = "values",
                entitiesIDs = entity.values.mapIndexed { index, v ->
                    EntityFieldID.EntityID(tag = "${tag_values}${index}", name = "value ${index + 1}")
                })
        )
    }

    override fun getFieldParamsByFieldID(entity: Item, fieldID: EntityFieldID): DescriptiveFieldValue {
        return when (fieldID) {
            is EntityFieldID.EntityID -> when (fieldID.tag) {
                tag_type -> DescriptiveFieldValue(value = entity.type, description = "type of the object")
                else -> {
                    val valueParameterIndex = fieldID.tag.substring(startIndex = tag_values.length).toIntOrNull() ?: -1
                    val value = entity.values.getOrNull(valueParameterIndex)
                    DescriptiveFieldValue(value, description = value?.parameter?.description ?: "")
                }
            }
            is EntityFieldID.EntitiesListID -> DescriptiveFieldValue(
                value = entity.values,
                description = "values"
            )
            is EntityFieldID.StringID -> DescriptiveFieldValue(value = entity.name, description = "name")
            else -> throw IllegalArgumentException("field with id: $fieldID was not found in entity: $entity")
        }
    }


    override fun mapIntoEntity(entity: Item, field: EntityField): Item {
        return when (val fieldID = field.fieldID) {
            is EntityFieldID.StringID -> entity.copy(name = (field as EntityField.StringField).value)
            is EntityFieldID.EntityID -> {
                when (fieldID.tag) {
                    "tag_type" -> entity.copy(type = ((field as EntityField.EntityLink).entity as ObjectType))
                    else -> setValue(entity, field)
                }
            }
            is EntityFieldID.EntitiesListID -> entity.copy(values = (field as EntityField.EntityLinksList).entities.mapNotNull { it.entity } as List<Value>)
            else -> throw IllegalArgumentException("field with fieldID: $fieldID was not found in entity: $entity")
        }
    }

    private fun setValue(item: Item, field: EntityField): Item {
        val fieldID = field.fieldID
        val valueParameterIndex = fieldID.tag.substring(startIndex = tag_values.length).toIntOrNull() ?: -1
        return item.copy(values = item.values.mapIndexed { index, v ->
            if (index == valueParameterIndex) {
                ((field as? EntityField.EntityLink)?.entity as? Value) ?: v
            } else v
        })
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
