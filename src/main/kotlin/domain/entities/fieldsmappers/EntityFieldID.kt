package domain.entities.fieldsmappers

sealed class EntityFieldID(
    val name: String,
    val tag: String? = null
) {

    object NameID : EntityFieldID(name = "name")
    object DescriptionID : EntityFieldID("description")
    object ObjectTypeID : EntityFieldID("object type")
    class ValueID(val valueID: String, parameterName: String) :
        EntityFieldID(tagWithValueID(valueID), parameterName) {
        companion object {
            private const val tag_const_part = "tag_value_"
            fun tagWithValueID(valueID: String) = "$tag_const_part$valueID"
            fun parseValueID(columnID: String) = columnID.substring(tag_const_part.length)
        }
    }

    class StringID(tag: String? = null) : EntityFieldID(name = "value", tag = tag)
    class FloatID(tag: String? = null) : EntityFieldID(name = "factor", tag = tag)
    class BooleanID(tag: String? = null) : EntityFieldID(name = "boolean", tag = tag)
    data class ContainerID(val containerID: String, val tag1: String? = null) :
        EntityFieldID(name = "parent container", tag = tag1)

    class ParameterID(val paramID: String, parameterName: String) :
        EntityFieldID(tagWithParameterID(paramID), parameterName) {
        companion object {
            private const val tag_const_part = "tag_parameter_"
            fun tagWithParameterID(paramID: String) = "$tag_const_part$paramID"
            fun parseParameterID(columnID: String) = columnID.substring(tag_const_part.length)
        }
    }

    class UnitID(val unitID: String, unitName: String) :
        EntityFieldID(tagWithUnitID(unitID), unitName) {
        companion object {
            private const val tag_const_part = "tag_unit_"
            fun tagWithUnitID(unitID: String) = "$tag_const_part$unitID"
            fun parseUnitID(columnID: String) = columnID.substring(tag_const_part.length)
        }
    }


}