package domain.entities.fieldsmappers

import domain.entities.Supplier

class SupplierFieldsMapper : BaseFieldsMapper<Supplier>() {

    override fun getFields(entity: Supplier): Map<EntityFieldID, Any> {
        return mapOf(
            EntityFieldID.NameID to entity.name,
            EntityFieldID.DescriptionID to entity.description,
            EntityFieldID.StringID("url") to entity.url,
            EntityFieldID.BooleanID("favorite") to entity.isFavorite
        )
    }

    override fun mapIntoEntity(entity: Supplier, field: EntityField): Supplier {
        return when (val column = field.fieldID) {
            is EntityFieldID.BooleanID -> entity.copy(isFavorite = (field as EntityField.BooleanField).value)
            EntityFieldID.DescriptionID -> entity.copy(description = (field as EntityField.StringField).value)
            EntityFieldID.NameID -> entity.copy(name = (field as EntityField.StringField).value)
            is EntityFieldID.StringID -> entity.copy(url = (field as EntityField.StringField).value)
            else -> throw IllegalArgumentException("field with column: $column was not found in entity: $entity")
        }
    }


}