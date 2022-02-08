package strings

class RUStringsProvider : IStringsProvider {
    override fun getString(strings: Strings): String {
        return when (strings) {
            Strings.navitem_database_title -> "источник данных"
            Strings.navitem_warehouse_title -> "склад"
            Strings.navitem_datatypes_title -> "типы данных"
            Strings.navitem_income_title -> "пополнение"
            Strings.navitem_outcome_title -> "изъятие"
            Strings.navitem_projects_title -> "проекты"
            Strings.navitem_settings_title -> "настройки"
            Strings.navitem_places_title -> "места"
        }
    }
}