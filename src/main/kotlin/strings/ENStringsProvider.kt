package strings

class ENStringsProvider: IStringsProvider {

    override fun getString(strings: Strings): String {
        return when(strings){
            Strings.navitem_database_title -> "data source"
            Strings.navitem_warehouse_title -> "warehouse"
            Strings.navitem_datatypes_title -> "data types"
            Strings.navitem_income_title -> "income"
            Strings.navitem_outcome_title -> "outcome"
            Strings.navitem_projects_title -> "projects"
            Strings.navitem_settings_title -> "settings"
            Strings.navitem_places_title -> "places"
        }
    }

}