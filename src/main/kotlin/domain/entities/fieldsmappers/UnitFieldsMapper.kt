package domain.entities.fieldsmappers

import com.akhris.domain.core.entities.IEntity
import domain.entities.Unit

class UnitFieldsMapper : IFieldsMapper {
    override fun mapFields(entity: Any): List<EntityField> {
        return when (entity) {
            is Unit -> mapUnitFields(entity)
            else -> throw IllegalArgumentException("$this cannot map $entity fields, use another mapper")
        }
    }

    override fun <T : IEntity<*>> mapIntoEntity(entity: T, field: EntityField): T {
        return when (entity) {
            is Unit -> mapIntoUnit(entity, field) as T
            else -> throw IllegalArgumentException("$this cannot map $entity fields, use another mapper")
        }
    }

    private fun mapUnitFields(unit: Unit): List<EntityField> {
        return listOf(
            EntityField.StringField(
                tag = "unit_field_unit",
                name = "unit",
                description = "value measured units",
                value = unit.unit
            ),
            EntityField.BooleanField(
                tag = "unit_field_is_multipliable",
                name = "is multipliable",
                description = "if value can be prefixed with multipliers (kilo, micro, nano,...)",
                value = unit.isMultipliable
            ),
            EntityField.CaptionField(
                tag = "unit_field_id",
                name = "UUID",
                description = "unique id of the unit",
                caption = unit.id
            )
        )
    }

    private fun mapIntoUnit(unit: Unit, field: EntityField): Unit {
        return when (field.tag) {
            "unit_field_unit" -> unit.copy(unit = (field as EntityField.StringField).value)
            "unit_field_is_multipliable" -> unit.copy(isMultipliable = (field as EntityField.BooleanField).value)
            else -> throw IllegalArgumentException("field with tag: ${field.tag} was not found in entity: $unit")
        }
    }
}