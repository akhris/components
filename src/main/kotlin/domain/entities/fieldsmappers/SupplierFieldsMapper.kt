package domain.entities.fieldsmappers

import domain.entities.Supplier

class SupplierFieldsMapper : IFieldsMapper<Supplier> {

    override fun getEntityIDs(): List<EntityFieldID> {
        return listOf(
            EntityFieldID.StringID(tag = EntityFieldID.tag_name, name = "name"),
            EntityFieldID.StringID(tag = EntityFieldID.tag_description, name = "description"),
            EntityFieldID.StringID(tag_url, name = "url"),
            EntityFieldID.BooleanID(tag_favorite, "is favorite")
        )
    }

    override fun getFieldByID(entity: Supplier, fieldID: EntityFieldID): EntityField {
        return when (fieldID) {
            is EntityFieldID.StringID -> {
                when (fieldID.tag) {
                    EntityFieldID.tag_name -> {
                        EntityField.StringField(
                            fieldID = fieldID,
                            description = "supplier's name",
                            value = entity.name
                        )
                    }
                    EntityFieldID.tag_description -> {
                        EntityField.StringField(
                            fieldID = fieldID,
                            description = "description",
                            value = entity.description
                        )
                    }
                    tag_url -> {
                        EntityField.StringField(
                            fieldID = fieldID,
                            description = "supplier's url",
                            value = entity.url
                        )
                    }
                    else -> {
                        throw IllegalArgumentException("field with tag: ${fieldID.tag} was not found in entity: $entity")
                    }
                }
            }
            is EntityFieldID.BooleanID -> EntityField.BooleanField(
                    fieldID = fieldID,
                    description = "marked as favorite",
                    value = entity.isFavorite
                )
            else -> throw IllegalArgumentException("field with id: $fieldID was not found in entity: $entity")
        }
    }


    override fun mapIntoEntity(entity: Supplier, field: EntityField): Supplier {
        return when (val fieldID = field.fieldID) {
            is EntityFieldID.BooleanID -> entity.copy(isFavorite = (field as EntityField.BooleanField).value)
            is EntityFieldID.StringID -> {
                when (fieldID.tag) {
                    "tag_name" -> entity.copy(name = (field as EntityField.StringField).value)
                    "tag_description" -> entity.copy(description = (field as EntityField.StringField).value)
                    "tag_url" -> entity.copy(url = (field as EntityField.StringField).value)
                    else -> throw IllegalArgumentException("unknown tag ${fieldID.tag} for entity: $entity")
                }
            }
            else -> throw IllegalArgumentException("field with column: $fieldID was not found in entity: $entity")
        }
    }

    companion object {
        const val tag_url = "tag_url"
        const val tag_favorite = "tag_favorite"
    }


}