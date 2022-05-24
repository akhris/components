package domain.entities.fieldsmappers

import domain.entities.Supplier

class SupplierFieldsMapper : IFieldsMapper<Supplier> {

    override fun getEntityIDs(): List<EntityFieldID> {
        return listOf(
            EntityFieldID(tag = EntityFieldID.tag_name, name = "name"),
            EntityFieldID(tag = EntityFieldID.tag_description, name = "description"),
            EntityFieldID(tag = tag_url, name = "url"),
            EntityFieldID(tag = tag_favorite, name = "is favorite")
        )
    }

    override fun getFieldByID(entity: Supplier, fieldID: EntityFieldID): EntityField {
        return when (fieldID.tag) {
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

            tag_favorite -> EntityField.BooleanField(
                fieldID = fieldID,
                description = "marked as favorite",
                value = entity.isFavorite
            )
            else -> throw IllegalArgumentException("field with id: $fieldID was not found in entity: $entity")
        }
    }


    override fun mapIntoEntity(entity: Supplier, field: EntityField): Supplier {
        return when (field) {
            is EntityField.BooleanField -> entity.copy(isFavorite = field.value)
            is EntityField.DateTimeField -> TODO()
            is EntityField.StringField -> {
                when (field.fieldID.tag) {
                    "tag_name" -> entity.copy(name = field.value)
                    "tag_description" -> entity.copy(description = field.value)
                    "tag_url" -> entity.copy(url = field.value)
                    else -> throw IllegalArgumentException("unknown tag ${field.fieldID.tag} for entity: $entity")
                }
            }
            else -> throw IllegalArgumentException("field with column: ${field.fieldID} was not found in entity: $entity")
        }
    }

    companion object {
        const val tag_url = "tag_url"
        const val tag_favorite = "tag_favorite"
    }


}