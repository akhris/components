package strings

import kotlinx.serialization.Serializable


@Serializable
class ENStringGetter : LocalStringGetter(
    language = "EN (default)",
    translations =
    StringsIDs.values().associate {
        it.name to when (it) {
            StringsIDs.navitem_database_title -> "data source"
            StringsIDs.navitem_warehouse_title -> "warehouse"
            StringsIDs.navitem_datatypes_title -> "data types"
            StringsIDs.navitem_income_title -> "income"
            StringsIDs.navitem_outcome_title ->  "outcome"
            StringsIDs.navitem_projects_title ->  "projects"
            StringsIDs.navitem_settings_title -> "settings"
            StringsIDs.navitem_places_title -> "places"
            StringsIDs.types_title -> "types"
            StringsIDs.types_description -> "Units that object parameters values can have (volts, amps, meters,...)"
            StringsIDs.parameters_title -> "parameters"
            StringsIDs.parameters_description -> "Parameters that objects can have (resistivity, maximum applied voltage, weight, size,...)"
            StringsIDs.units_title -> "units"
            StringsIDs.units_description -> "Units that object parameters values can have (volts, amps, meters,...)"
            StringsIDs.items_title -> "items"
            StringsIDs.items_description -> "Types of objects that are stored in database (resistors, capacitors, ICs,...) and their parameters sets"
            StringsIDs.containers_title -> "containers"
            StringsIDs.containers_description -> "Boxes, shelves, rooms, and other places that contains objects"
            StringsIDs.suppliers_title -> "suppliers"
            StringsIDs.suppliers_description -> "Sets of items"
            StringsIDs.projects_title -> "projects"
            StringsIDs.projects_description -> "Sets of items"
            StringsIDs.itemIncome_title -> "Items income"
            StringsIDs.itemIncome_description ->  "when items came in, from what supplier and what container was put in"
            StringsIDs.itemOutcome_title ->"Items outcome"
            StringsIDs.itemOutcome_description ->  "when items came out and from what container"
            StringsIDs.warehouseItem_title -> "Warehouse items"
            StringsIDs.warehouseItem_description -> "current state of warehouse"
        }
    }
)