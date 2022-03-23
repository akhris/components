package domain.entities.fieldsmappers

import domain.entities.Supplier

class SupplierFieldsMapper : BaseFieldsMapper<Supplier>() {

    private val tag_url = "tag_url"
    private val tag_favorite = "tag_favorite"

    override fun getEntityIDs(entity: Supplier): List<EntityFieldID> {
        return listOf(
            EntityFieldID.StringID(tag = EntityFieldID.tag_name, name = "name"),
            EntityFieldID.StringID(tag = EntityFieldID.tag_description, name = "description"),
            EntityFieldID.StringID(tag_url, name = "url"),
            EntityFieldID.BooleanID(tag_favorite, "is favorite")
        )
    }


    override fun getFieldParamsByFieldID(entity: Supplier, fieldID: EntityFieldID): DescriptiveFieldValue {
        return when (fieldID) {
            is EntityFieldID.StringID -> {
                when (fieldID.tag) {
                    EntityFieldID.tag_name -> DescriptiveFieldValue.CommonField(
                        entity = entity.name,
                        description = "supplier's name"
                    )
                    EntityFieldID.tag_description -> DescriptiveFieldValue.CommonField(
                        entity = entity.description,
                        description = "description"
                    )
                    tag_url -> DescriptiveFieldValue.CommonField(entity = entity.url, description = "supplier's url")
                    else -> throw IllegalArgumentException("field with tag: ${fieldID.tag} was not found in entity: $entity")
                }
            }
            is EntityFieldID.BooleanID -> DescriptiveFieldValue.CommonField(entity = entity.isFavorite, "marked as favorite")
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


}