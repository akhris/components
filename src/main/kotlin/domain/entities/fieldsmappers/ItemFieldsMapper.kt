package domain.entities.fieldsmappers

import domain.entities.*
import domain.valueobjects.Factor
import utils.replaceOrAdd

class ItemFieldsMapper : IFieldsMapper<Item> {

    override fun getEntityIDs(): List<EntityFieldID> = listOf(
        EntityFieldID.StringID(tag = "tag_name", name = "name"),
        EntityFieldID.EntityID(tag = tag_type, name = "object type"),
        EntityFieldID.EntitiesListID(
            tag = tag_values,
            name = "values"
        )
    )

    override fun getFieldByID(entity: Item, fieldID: EntityFieldID): EntityField {
        return when (fieldID) {
            is EntityFieldID.EntityID -> when (val tag = fieldID.tag) {
                tag_type -> {
                    EntityField.EntityLink.EntityLinkSimple(
                        fieldID = fieldID,
                        description = "type of the object",
                        entity = entity.type,
                        entityClass = ObjectType::class
                    )
                }
                null -> throw IllegalArgumentException("tag cannot be null for fieldID: $fieldID")
                else -> {
                    //tag == parameter id
                    val value = entity.getAllValues().find { it.first.entity.id == tag }?.first
                    EntityField.EntityLink.EntityLinkValuable(
                        fieldID = fieldID,
                        description = value?.entity?.description ?: "",
                        entity = value?.entity,
                        entityClass = Parameter::class,
                        value = value?.value,
                        factor = value?.factor,
                        unit = value?.entity?.unit?.unit
                    )
                }
            }
            is EntityFieldID.EntitiesListID -> {
                EntityField.EntityLinksList(
                    fieldID = fieldID,
                    entities = entity.getAllValues().map { (ev, ot) ->
                        val entityID = EntityFieldID.EntityID(
                            tag = ev.entity.id,
                            name = ev.entity.name
                        )

                        val description = StringBuilder()

                        description.append(ev.entity.description)

                        if (ot != null) {
                            if (ev.entity.description.isNotEmpty()) {
                                description.appendLine()
                            }
                            description.append("parameter of ${ot.name}")
                        }

                        EntityField.EntityLink.EntityLinkValuable(
                            fieldID = entityID,
                            description = description.toString(),
                            entity = ev.entity,
                            entityClass = Parameter::class,
                            value = ev.value,
                            factor = ev.factor,
                            unit = ev.entity.unit?.unit
                        )
                    },
                    description = "parameters values",
                    entityClass = Parameter::class
                )
            }
            is EntityFieldID.StringID -> {
                val name = entity.name.ifEmpty {
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
                }

                EntityField.StringField(
                    fieldID = fieldID,
                    value = name,
                    description = "name",
                    isPlaceholder = entity.name.isEmpty()
                )
            }
            else -> throw IllegalArgumentException("field with id: $fieldID was not found in entity: $entity")
        }
    }

    private fun Item.getAllValues(): List<Pair<EntityValuable<Parameter>, ObjectType?>> {
        val allParameters = type?.getParameters() ?: listOf()
        val allValues =
            allParameters
                .map { p ->
                    val paramInValues = values.find { it.entity.id == p.first.id }
                    (paramInValues ?: EntityValuable(p.first)) to p.second
                }.plus(values.filterNot { v -> allParameters.find { it.first.id == v.entity.id } != null }
                    .map { it to null })
        return allValues
    }

    /**
     * Get all parameters for this object type and parent's object type (recursive - see [ObjectType.iterate])
     */
    private fun ObjectType.getParameters(): List<Pair<Parameter, ObjectType>> {
        val params = mutableListOf<Pair<Parameter, ObjectType>>()
        this.iterate { ot ->
            params.addAll(ot.parameters.map { it to ot })
        }
        return params
    }


    override fun mapIntoEntity(entity: Item, field: EntityField): Item {
        return when (field) {
            is EntityField.StringField -> {
                entity.copy(name = field.value)
            }
            is EntityField.EntityLinksList -> {
                entity.copy(values = field
                    .entities
                    .mapNotNull { link ->
                        (link.entity as? Parameter)?.let { p ->
                            EntityValuable(
                                p,
                                value = (link as? EntityField.EntityLink.EntityLinkValuable)?.value,
                                factor = (link as? EntityField.EntityLink.EntityLinkValuable)?.factor
                            )
                        }
                    })
            }
            is EntityField.EntityLink.EntityLinkSimple -> {
                entity.copy(type = field.entity as? ObjectType)
            }
            is EntityField.EntityLink.EntityLinkValuable -> {
                setItemValue(entity, field)
                    ?: throw IllegalArgumentException("unknown fieldID: ${field.fieldID} for entity: $entity")
            }
            else -> throw IllegalArgumentException("field with fieldID: ${field.fieldID} was not found in entity: $entity")
        }
    }


    private fun setItemValue(item: Item, field: EntityField.EntityLink.EntityLinkValuable): Item? {
        val fieldID = field.fieldID
        val parameterID = fieldID.tag ?: return null

        val newValue = (field.entity as? Parameter)?.let {
            EntityValuable(
                entity = it,
                value = field.value,
                factor = field.factor
            )
        }
            ?: return null

        return item.copy(
            values = item.values.replaceOrAdd(
                newValue = newValue
            ) {
                it.entity.id == parameterID
            }
        )
    }

    companion object {
        const val tag_values = "tag_values"
        const val tag_type = "tag_type"

    }


}
