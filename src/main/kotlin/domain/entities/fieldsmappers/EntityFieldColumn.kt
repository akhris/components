package domain.entities.fieldsmappers

sealed class EntityFieldColumn(
    val name: String,
    val tag: String? = null
) {

    object NameColumn : EntityFieldColumn(name = "name")
    object DescriptionColumn : EntityFieldColumn("description")
    object ObjectTypeColumn : EntityFieldColumn("object type")
    class ValueColumn(val valueID: String, parameterName: String) :
        EntityFieldColumn(tagWithValueID(valueID), parameterName) {
        companion object {
            private const val tag_const_part = "tag_value_"
            fun tagWithValueID(valueID: String) = "$tag_const_part$valueID"
            fun parseValueID(columnID: String) = columnID.substring(tag_const_part.length)
        }
    }

    class StringColumn(tag: String? = null) : EntityFieldColumn(name = "value", tag = tag)
    class FloatColumn(tag: String? = null) : EntityFieldColumn(name = "factor", tag = tag)
    class BooleanColumn(tag: String? = null) : EntityFieldColumn(name = "boolean", tag = tag)
    class ContainerColumn(val containerID: String, tag: String? = null) : EntityFieldColumn(name = "factor", tag = tag)

    class ParameterColumn(val paramID: String, parameterName: String) :
        EntityFieldColumn(tagWithParameterID(paramID), parameterName) {
        companion object {
            private const val tag_const_part = "tag_parameter_"
            fun tagWithParameterID(paramID: String) = "$tag_const_part$paramID"
            fun parseParameterID(columnID: String) = columnID.substring(tag_const_part.length)
        }
    }

    class UnitColumn(val unitID: String, unitName: String) :
        EntityFieldColumn(tagWithUnitID(unitID), unitName) {
        companion object {
            private const val tag_const_part = "tag_unit_"
            fun tagWithUnitID(unitID: String) = "$tag_const_part$unitID"
            fun parseUnitID(columnID: String) = columnID.substring(tag_const_part.length)
        }
    }


}