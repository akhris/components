package domain.entities.fieldsmappers

import domain.entities.Supplier

class SupplierFieldsMapper : BaseFieldsMapper<Supplier>() {

    override fun getFields(entity: Supplier): Map<EntityFieldID, Any> {
        return mapOf(
            EntityFieldID.StringID(tag = "tag_name", name = "name") to entity.name,
            EntityFieldID.StringID(tag = "tag_description", name = "description") to entity.description,
            EntityFieldID.StringID("tag_url", name = "url") to entity.url,
            EntityFieldID.BooleanID("tag_favorite", "is favorite") to entity.isFavorite
        )
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


}