package strings

class ENStringsProvider : IStringsProvider {

    override fun getString(strings: Strings): String {
        return when (strings) {
            Strings.navitem_database_title -> "data source"
            Strings.navitem_warehouse_title -> "warehouse"
            Strings.navitem_datatypes_title -> "data types"
            Strings.navitem_income_title -> "income"
            Strings.navitem_outcome_title -> "outcome"
            Strings.navitem_projects_title -> "projects"
            Strings.navitem_settings_title -> "settings"
            Strings.navitem_places_title -> "places"
            Strings.TypesOfData.items_title -> "items"
            Strings.TypesOfData.parameters_title -> "parameters"
            Strings.TypesOfData.types_title -> "types"
            Strings.TypesOfData.units_title -> "units"
            Strings.TypesOfData.containers_title -> "containers"
            Strings.TypesOfData.items_description -> "Types of objects that are stored in database (resistors, capacitors, ICs,...) and their parameters sets"
            Strings.TypesOfData.parameters_description -> "Parameters that objects can have (resistivity, maximum applied voltage, weight, size,...)"
            Strings.TypesOfData.types_description -> "Units that object parameters values can have (volts, amps, meters,...)"
            Strings.TypesOfData.units_description -> "Items that are stored (not related to quantities)"
            Strings.TypesOfData.containers_description -> "Boxes, shelves, rooms, and other places that contains objects"
            Strings.TypesOfData.suppliers_description -> "Shops and other places where objects are got from"
            Strings.TypesOfData.suppliers_title -> "suppliers"
        }
    }

}