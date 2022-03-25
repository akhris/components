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
        object suppliers_title : TypesOfData
        object suppliers_description : TypesOfData
        object projects_title : TypesOfData
        object projects_description : TypesOfData
        object itemIncome_title : TypesOfData
        object itemIncome_description : TypesOfData
        object itemOutcome_title : TypesOfData
        object itemOutcome_description : TypesOfData
        object warehouseItem_title : TypesOfData
        object warehouseItem_description : TypesOfData
    }
}