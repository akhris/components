package domain.entities.fieldsmappers

import domain.entities.EntityValuable
import domain.entities.Item
import domain.entities.ObjectType
import domain.entities.Parameter
import domain.valueobjects.Factor

class ItemFieldsMapper : BaseFieldsMapper<Item>() {

    override fun getEntityIDs(): List<EntityFieldID> {
        return listOf(
            EntityFieldID.StringID(tag = "tag_name", name = "name"),
            EntityFieldID.EntityID(tag = tag_type, name = "object type", entityClass = ObjectType::class),
            EntityFieldID.EntitiesListID(
                tag = tag_values,
                name = "values",
                entityClass = Parameter::class
            )
        )
    }

    override fun getFieldParamsByFieldID(entity: Item, fieldID: EntityFieldID): DescriptiveFieldValue {
        return when (fieldID) {
            is EntityFieldID.EntityID -> when (fieldID.tag) {
                tag_type -> DescriptiveFieldValue.CommonField(entity = entity.type, description = "type of the object")
                else -> {
                    val valueParameterIndex = fieldID.tag.substring(startIndex = tag_values.length).toIntOrNull() ?: -1
                    val item = entity.values.getOrNull(valueParameterIndex)

                    DescriptiveFieldValue.ValuableField(
                        entity = item?.entity,
                        description = item?.entity?.description ?: "",
                        value = item?.value,
                        factor = if (item?.entity?.unit?.isMultipliable == true) {
                            item.factor ?: 1
                        } else {
                            null
                        },
                        unit = item?.entity?.unit?.unit
                    )

                }
            }
            is EntityFieldID.EntitiesListID -> DescriptiveFieldValue.CommonField(
                entity = entity.values,
                description = "values"
            )
            is EntityFieldID.StringID -> {
                DescriptiveFieldValue.CommonField(
                    entity = entity.name.ifEmpty {
                        val builder = StringBuilder()
                        entity.type?.let {
                            builder.append(it.name)
                            builder.append(" ·")
                        }
                        entity.values.forEachIndexed { index, par ->
                            builder.append(" ")
                            builder.append(par.value)
                            if (index < entity.values.size - 1 && par.entity.unit != null)
                                builder.append(" ")
                            par.entity.unit?.let { u ->
                                if (u.isMultipliable && par.factor != null && par.factor != 1 && par.factor != 0)
                                    builder.append(Factor.parse(par.factor).prefix)
                                builder.append(u.unit)
                            }
                            if (index < entity.values.size - 1)
                                builder.append(" · ")
                        }
                        builder.toString()
                    },
                    description = "name",
                    isAlternative = entity.name.isEmpty()
                )
            }
            else -> throw IllegalArgumentException("field with id: $fieldID was not found in entity: $entity")
        }
    }


    override fun mapIntoEntity(entity: Item, field: EntityField): Item {
        return when (val fieldID = field.fieldID) {
            is EntityFieldID.StringID -> entity.copy(name = (field as EntityField.StringField).value)
            is EntityFieldID.EntityID -> {
                when (fieldID.tag) {
                    "tag_type" -> {
                        entity.copy(type = ((field as EntityField.EntityLink).entity as? ObjectType))
                    }
                    else -> setItem(entity, field)
                        ?: throw IllegalArgumentException("unknown fieldID: $fieldID for entity: $entity")
                }
            }
            is EntityFieldID.EntitiesListID -> entity.copy(values = (field as EntityField.EntityLinksList).entities.mapNotNull {
                (it.entity as? Parameter)?.let { p ->
                    EntityValuable(
                        entity = p,
                        value = (it as? EntityField.EntityLink.EntityLinkValuable)?.value,
                        factor = (it as? EntityField.EntityLink.EntityLinkValuable)?.factor
                    )
                }
            })
            else -> throw IllegalArgumentException("field with fieldID: $fieldID was not found in entity: $entity")
        }
    }

    private fun setItem(item: Item, field: EntityField): Item? {
        val fieldID = field.fieldID
        val valueIndex = fieldID.tag.substring(startIndex = tag_values.length).toIntOrNull() ?: return null
        if (item.values.getOrNull(valueIndex) == null) {
            return null
        }
        return item.copy(
            values = item.values.mapIndexedNotNull { index, itemValuable ->
                if (index == valueIndex) {
                    val valuableField =
                        (field as? EntityField.EntityLink.EntityLinkValuable) ?: return@mapIndexedNotNull null
                    EntityValuable(
                        valuableField.entity as Parameter,
                        valuableField.value,
                        factor = valuableField.factor
                    )
                } else itemValuable
            }
        )
    }

    companion object {
        const val tag_values = "tag_values"
        const val tag_type = "tag_type"

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
