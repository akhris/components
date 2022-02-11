package domain.entities.fieldsmappers

import domain.entities.Supplier

class SupplierFieldsMapper : BaseFieldsMapper<Supplier>() {

    override fun getFields(entity: Supplier): Map<EntityFieldColumn, Any> {
        return mapOf(
            EntityFieldColumn.NameColumn to entity.name,
            EntityFieldColumn.DescriptionColumn to entity.description,
            EntityFieldColumn.StringColumn("url") to entity.url,
            EntityFieldColumn.BooleanColumn("favorite") to entity.isFavorite
        )
    }

    override fun mapIntoEntity(entity: Supplier, field: EntityField): Supplier {
        return when (val column = field.fieldColumn) {
            is EntityFieldColumn.BooleanColumn -> entity.copy(isFavorite = (field as EntityField.BooleanField).value)
            EntityFieldColumn.DescriptionColumn -> entity.copy(description = (field as EntityField.StringField).value)
            EntityFieldColumn.NameColumn -> entity.copy(name = (field as EntityField.StringField).value)
            is EntityFieldColumn.StringColumn -> entity.copy(url = (field as EntityField.StringField).value)
            else -> throw IllegalArgumentException("field with column: $column was not found in entity: $entity")
        }
    }


}