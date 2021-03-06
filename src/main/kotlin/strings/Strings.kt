@file:Suppress("ClassName")

package strings

sealed interface Strings {
    object navitem_database_title : Strings
    object navitem_warehouse_title : Strings
    object navitem_datatypes_title : Strings
    object navitem_income_title : Strings
    object navitem_outcome_title : Strings
    object navitem_projects_title : Strings
    object navitem_settings_title : Strings
    object navitem_places_title : Strings
    sealed interface TypesOfData : Strings {
        object types_title : TypesOfData
        object types_description : TypesOfData
        object parameters_title : TypesOfData
        object parameters_description : TypesOfData
        object units_title : TypesOfData
        object units_description : TypesOfData
        object items_title : TypesOfData
        object items_description : TypesOfData
        object containers_title : TypesOfData
        object containers_description : TypesOfData
    }
}